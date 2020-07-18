package com.fractal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CyclicBarrier;

public class Mandelbrot {
	private static double xc, yc, size;

	public static void main(String[] args) {
		if (args == null || args.length != 3) {
			xc = -0.5626783737400001;
			yc = 0.6567946173500001;
			size = 0.000000064;
		} else {
			xc = Double.parseDouble(args[0]);
			yc = Double.parseDouble(args[1]);
			size = Double.parseDouble(args[2]);
		}

		int n = 504; // create n-by-n image
		int max = 10000; // maximum number of iterations

		Picture picture = new Picture(n, n);

		List<Processing> processings = new ArrayList<Processing>(12);
		ExecutorService taskExecutor = Executors.newFixedThreadPool(12);

		CyclicBarrier bar = new CyclicBarrier(12, () -> { // executed at the end of the loop iteration
			picture.show();
			// events
			if (picture.get_isCtrl())
				size += size / 4;
			else if (picture.get_isEnter())
				size -= size / 4;
			if (picture.get_isLeft())
				xc -= size / 10;
			else if (picture.get_isRight())
				xc += size / 10;
			if (picture.get_isUp())
				yc += size / 10;
			else if (picture.get_isDown())
				yc -= size / 10;
			for (Processing process : processings) {
				process.set_coords(xc, yc, size);
			}
		});

		int diviser = n / 12;
		for (int i = 0; i < 12; i++) {
			processings.add(new Processing(xc, yc, size, n, max, picture, diviser * i, diviser * (i + 1), bar));
		}

		// start of image rendering
		for (Processing process : processings) {
			taskExecutor.execute(process);
		}

		// end of image rendering
//		taskExecutor.shutdownNow();

	}
}