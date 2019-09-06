package actions;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import processing.Calculator;
import processing.WavFileReader;
import view.MainScreen;
import view.Sonogram;

public class SonogramEvent implements EventHandler<ActionEvent> {

	long fsempl;

	@Override
	public void handle(ActionEvent event) {
		String file = MainScreen.get().directory.getText();
		int sliceSize = Integer.parseInt(MainScreen.get().sliceSize.getText());

		ArrayList<Double> parametri = calculateParameters(file, sliceSize);
		new Sonogram(parametri);
	}

	public ArrayList<Double> calculateParameters(String file, int sliceSize) {
		ArrayList<Double> parametri = new ArrayList<>();

		ArrayList<double[]> m = matrica(file, sliceSize);
		double sampleCounter = 0;

		for (int i = 0; i < m.size(); i++) {
			double[] z = m.get(i);
			double T = ((double)z.length-1.0)/(double)fsempl;

			double max = Double.MIN_VALUE;
			for (int j = 1; j < z.length; j++) {
				if (max < z[j])
					max = z[j];
			}

			
			for (int j = 1; j <= z.length / 2; j++) {
				sampleCounter += 2;
				double t = sampleCounter/(double)fsempl;
				int hz = (int) (j / T);
				
				double magnituda = z[j] * (1.0 / max);

				
				parametri.add((double) hz);
				parametri.add(t);
				parametri.add(magnituda);
			}

		}

		return parametri;
	}

	public ArrayList<double[]> matrica(String file, int sliceSize) {
		ArrayList<double[]> output = new ArrayList<>();
		Calculator calculator = new Calculator();
		WavFileReader reader = new WavFileReader(file);

		fsempl = reader.getSampleRate();
		ArrayList<Double> frames = reader.getFrames();
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


}
