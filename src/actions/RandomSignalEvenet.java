package actions;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import processing.Calculator;
import view.Histogram;
import view.MainScreen;
import view.RandomSignalList;
import view.Sonogram;

public class RandomSignalEvenet implements EventHandler<ActionEvent> {

	private int time = 1;
	private static int fsampl = 44100;
	private static double T;
	private static double fmax;
	private static double fmin;

	int SampleCount;

	int sliceSize;

	ArrayList<String> harmonics = new ArrayList<>();
	double[] x;
	double[] y;

	double[] z;

	ArrayList<Integer> frequencys = new ArrayList<>();
	
	
	@Override
	public void handle(ActionEvent event) {
		harmonics = new ArrayList<>();
		frequencys = new ArrayList<>();

		generateHistogram();

		generateSonogram();
		
		new RandomSignalList(harmonics);
	}

	public void generateSonogram() {
		sliceSize = Integer.parseInt(MainScreen.get().sliceSize.getText());
		ArrayList<Double> parametri = calculateParameters(sliceSize);
		new Sonogram(parametri);
	}

	public ArrayList<Double> calculateParameters(int sliceSize) {
		ArrayList<Double> parametri = new ArrayList<>();

		ArrayList<double[]> m = matrica(sliceSize);
		double sampleCounter = 0;

		for (int i = 0; i < m.size(); i++) {
			double[] z = m.get(i);
			double T = ((double) z.length - 1.0) / (double) fsampl;

			double max = Double.MIN_VALUE;
			for (int j = 1; j < z.length; j++) {
				if (max < z[j])
					max = z[j];
			}

			for (int j = 1; j <= z.length / 2; j++) {
				sampleCounter += 2;
				double t = sampleCounter / (double) fsampl;
				int hz = (int) (j / T);

				double magnituda = z[j] * (1.0 / max);

				parametri.add((double) hz);
				parametri.add(t);
				parametri.add(magnituda);
			}

		}

		return parametri;
	}

	public ArrayList<double[]> matrica(int sliceSize) {
		ArrayList<double[]> output = new ArrayList<>();
		Calculator calculator = new Calculator();

		ArrayList<Double> frames = new ArrayList<>();
		
		SampleCount = (int) (fsampl * time);
		double[] tmp = new double[SampleCount];

		for (int i = 0; i < frequencys.size(); i++) {
			int f = frequencys.get(i);
			double twoPif = 2.0 * Math.PI * (double) f;

			for (int sample = 0; sample < tmp.length; sample++) {
				double time = (double) sample / fsampl;
				tmp[sample] += Math.sin(twoPif * time);
			}
		}
		
		for(int i = 0; i<tmp.length; i++)
			frames.add(tmp[i]);	
			
		
		//-------------------------------------------------------------
		ArrayList<ArrayList<Double>> slices = new ArrayList<>();
		ArrayList<Double> slice = new ArrayList<>();

		for (int i = 0; i < frames.size(); i++) {
			if (i % sliceSize == 0) {
				slices.add(slice);
				slice = new ArrayList<>();
			}
			slice.add(frames.get(i));
		}

		for (int i = 0; i < slices.size(); i++) {
			ArrayList<Double> s = slices.get(i);
			double[] x = new double[s.size()];
			double[] y = new double[s.size()];

			for (int j = 0; j < s.size(); j++) {
				x[j] = s.get(j);
				y[j] = 0;
			}

			calculator.dft(x, y);
			double[] z = calculator.calcMagnitude(x, y);
			output.add(z);
		}

		return output;
	}

	public void generateHistogram() {
		T = Double.parseDouble(MainScreen.get().sirinaT.getText()) / 1000;
		fmax = fsampl / 2;
		fmin = 1 / T;

		SampleCount = (int) (fsampl * T);

		x = new double[SampleCount];
		y = new double[SampleCount];
		Arrays.fill(x, 0);
		Arrays.fill(y, 0);
		int hNum = Integer.parseInt(MainScreen.get().harmonicNumber.getText());
		
		for (int i = 0; i < hNum; i++) {
			int f = (int) random(fmin, fmax);
			frequencys.add(f);
			double twoPif = 2.0 * Math.PI * (double) f;

			String str = "sin(2 * PI * " + f + " * (sample/" + time + "))";
			harmonics.add(str);

			for (int sample = 0; sample < x.length; sample++) {
				double time = (double) sample / fsampl;
				x[sample] += Math.sin(twoPif * time);
			}
		}

		if (MainScreen.get().comboBox.getValue().toString().equals("Hanning")) {
			x = HanningWindow(x);
		}

		if (MainScreen.get().comboBox.getValue().toString().equals("Hamming")) {
			x = HammingWindow(x);
		}

		Calculator calc = new Calculator();
		calc.dft(x, y);

		z = calc.calcMagnitude(x, y);
		
		new Histogram(z, fsampl, T, fmax, fmin, SampleCount);
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

	public double random(double min, double max) {
		double diff = max - min;
		return min + Math.random() * diff;
	}

}
