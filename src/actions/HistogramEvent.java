package actions;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import processing.Calculator;
import processing.Complex;
import processing.WavFileReader;
import view.Histogram;
import view.MainScreen;

public class HistogramEvent implements EventHandler<ActionEvent> {

	private static int fsampl;
	private static double T;
	private static double fmax;
	private static double fmin;

	static int brSampl;

	@Override
	public void handle(ActionEvent event) {
		String sirina = MainScreen.get().sirinaT.getText();
		String directory = MainScreen.get().directory.getText();

		if (directory.equals(""))
			return;
		try {
			Double.parseDouble(sirina);
		} catch (Exception e) {
			return;
		}

		MainScreen.get().setZ(histogram(directory, Double.parseDouble(sirina)));
		new Histogram(MainScreen.get().getZ(), fsampl, T, fmax, fmin, brSampl);
	}

	public static double[] histogram(String file, double prozor) {
		prozor = prozor / 1000.0;
		Calculator calculator = new Calculator();
		WavFileReader reader = new WavFileReader(file);
		System.out.println("Frames catched: " + reader.getFrames().size());
		System.out.println("Frekvencija Semplovanja: " + reader.getSampleRate());

		ArrayList<Double> frames = reader.getFrames();

		fsampl = (int) reader.getSampleRate();
		T = prozor;

		double maxT = (((double) frames.size() / (double) fsampl) / (double) reader.getNumChannels());
		if (T > maxT)
			T = maxT;

		fmax = fsampl / 2;
		fmin = 1 / T;

		brSampl = (int) (fsampl * T);
		double[] x = new double[brSampl];
		double[] y = new double[brSampl];

		for (int i = 0; i < brSampl; i++) {
			x[i] = frames.get(i);
			y[i] = 0;
		}

		if (MainScreen.get().comboBox.getValue().toString().equals("Hanning")) {
			x = HanningWindow(x);
		}

		if (MainScreen.get().comboBox.getValue().toString().equals("Hamming")) {
			x = HammingWindow(x);
		}

		int option = 1;

		if (option == 1)
			calculator.dft(x, y);
		else {
			Complex[] k = calculator.fft(x, y);
			x = new double[k.length];
			y = new double[k.length];

			for (int i = 0; i < k.length; i++) {
				x[i] = k[i].re();
				y[i] = k[i].im();
			}
		}
		
		double[] z = calculator.calcMagnitude(x, y);

		return z;
	}

	public static double[] HammingWindow(double[] signal_in) {
		int size = signal_in.length;
		for (int i = 0; i < size; i++) {

			signal_in[i] = (signal_in[i] * (0.54 - 0.46 * Math.cos(2.0 * Math.PI * i / size-1)));
		}
		return signal_in;
	}

	public static double[] HanningWindow(double[] signal_in) {

		int size = signal_in.length;
		for (int i = 0; i < size; i++) {

			signal_in[i] = (signal_in[i] * 0.5 * (1.0 - Math.cos(2.0 * Math.PI * i / size-1)));
		}
		return signal_in;
	}

}
