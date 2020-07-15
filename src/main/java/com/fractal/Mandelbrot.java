package com.fractal;

import java.util.ArrayList;
import java.util.List;

public class Mandelbrot {

	public static void main(String[] args) {
		double xc, yc, size;
		if (args == null || args.length != 3) {
			xc = -0.5626783737400001;
			yc = 0.6567946173500001;
			size = 0.000000064;
		} else {
			xc = Double.parseDouble(args[0]);
			yc = Double.parseDouble(args[1]);
			size = Double.parseDouble(args[2]);
		}

		int n = 500; // create n-by-n image
		int max = 10000; // maximum number of iterations

		Picture picture = new Picture(n, n);

		List<Processing> processings = new ArrayList<Processing>();
		int divider = n / 50;
		for (int i = 0; i < 50; i++) {
			processings.add(new Processing(xc, yc, size, n, max, picture, divider * i, divider * (i + 1))); // Runnable
		}

		while (true) {
			List<Thread> threads = new ArrayList<Thread>();
			for (Processing process : processings) {
				threads.add(new Thread(process));
			}
			for (Thread thread : threads) {
				thread.start();
			}
			for (Thread thread : threads) {
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			picture.show();
			// events
			if (picture.getIsCtrl())
				size += size / 4;
			else if (picture.getIsEnter())
				size -= size / 4;
			if (picture.getIsLeft())
				xc -= size / 10;
			else if (picture.getIsRight())
				xc += size / 10;
			if (picture.getIsUp())
				yc += size / 10;
			else if (picture.getIsDown())
				yc -= size / 10;
			for (Processing process : processings) {
				process.set_coords(xc, yc, size);
			}
		}
	}
}