package processing;

import java.util.ArrayList;

public class Calculator {

	public Complex[] fft(double[] x1, double[] y1) {
		ArrayList<Double> tmp = new ArrayList<>();
		for (int i = 0; i < x1.length; i++)
			tmp.add(x1[i]);

		while (!isPowerOfTwo(tmp.size())) {
			tmp.add(0.0);
		}
		
		Complex[] x = new Complex[tmp.size()];
		for (int i = 0; i < tmp.size(); i++) {
			x[i] = new Complex(tmp.get(i), 0.0);
		}

		Complex[] y = FFT.fft(x);

		return y;
	}

	public void dft(double[] x1, double[] y1) {
		int dir = 1;
		int i, k;
		double arg;
		double cosarg, sinarg;
		int m = x1.length;

		double[] x2 = new double[m];
		double[] y2 = new double[m];

		for (i = 0; i < m; i++) {
			x2[i] = 0;
			y2[i] = 0;
			arg = -dir * 2.0 * 3.141592654 * (double) i / (double) m;

			for (k = 0; k < m; k++) {
				cosarg = Math.cos(k * arg);
				sinarg = Math.sin(k * arg);
				x2[i] += (x1[k] * cosarg - y1[k] * sinarg);
				y2[i] += (x1[k] * sinarg + y1[k] * cosarg);
			}
		}

		if (dir == 1) {
			for (i = 0; i < m; i++) {
				x1[i] = x2[i] / (double) m;
				y1[i] = y2[i] / (double) m;
			}
		} else {
			for (i = 0; i < m; i++) {
				x1[i] = x2[i];
				y1[i] = y2[i];
			}
		}

	}

	public double[] calcMagnitude(double[] x, double[] y) {
		double[] z = new double[x.length];
		for (int i = 0; i < z.length; i++) {

			z[i] = Math.sqrt(x[i] * x[i] + y[i] * y[i]);
		}

		return z;
	}

	public boolean isPowerOfTwo(int n) {
		boolean isPower = false;
		int temp = n;

		while (temp >= 2) {
			if (temp % 2 == 0) {
				isPower = true;

			} else {
				isPower = false;
				break;
			}
			temp = temp / 2;
		}
		return isPower;
	}

}
