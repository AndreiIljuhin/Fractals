package com.fractal;

import java.awt.Color;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Processing implements Runnable {
	private double xc;
	private double yc;
	private double size; //  field of vision
	private final int n; // create n-by-n image
	private final int max; // maximum number of iterations
	private final Picture picture;
	private final int startI;
	private final int endI;
	private final int pal;
	private final CyclicBarrier bar;

	Processing(double xc, double yc, double size, int n, int max, Picture picture, int startI, int endI,
			CyclicBarrier bar) {
		this.xc = xc;
		this.yc = yc;
		this.size = size;
		this.n = n;
		this.max = max;
		this.picture = picture;
		this.startI = startI;
		this.endI = endI;
		pal = 16581375 / max;
		this.bar = bar;
	}

	public void set_coords(double xc, double yc, double size) {
		this.xc = xc;
		this.yc = yc;
		this.size = size;
	}

	private int mand(Complex z0, int max) { // recursion in a loop
		Complex z = z0;
		for (int t = 1; t < max; t++) {
			if (z.abs() > 2.0)
				return t;
			z = z.mult(z).plus(z0);
		}
		return max;
	}

	public void run() {
		try {
			while (true) {

				for (int i = startI; i < endI; i++) {
					for (int j = 0; j < n; j++) {
						double x0 = xc - size / 2 + size * i / n;
						double y0 = yc - size / 2 + size * j / n;
						Complex z0 = new Complex(x0, y0);
						int iter = mand(z0, max);
						int col = 16581375 - pal * iter;

						Color color = new Color((byte) (col >>> 16) & 0xFF, (byte) (col >>> 8) & 0xFF,
								(byte) col & 0xFF);
						picture.set(i, n - 1 - j, color);
					}
				}
				
				bar.await(); // waiting for the other threads and image rendering
			}
		} catch (InterruptedException e) {
		} catch (BrokenBarrierException e) {
		}
		System.out.printf("%s finished... \n", Thread.currentThread().getName());
	}
}
