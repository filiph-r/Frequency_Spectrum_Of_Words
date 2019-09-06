package view;

import java.util.ArrayList;

import actions.ExportEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.DepthTest;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;

public class Histogram extends Form {
	double[] z;
	int fsampl;
	double T;
	double fmax;
	double fmin;
	int brSampl;

	public Histogram(double[] z, int fsampl, double T, double fmax, double fmin, int brSampl) {
		super("Histogram");
		this.z = z;
		this.fsampl = fsampl;
		this.T = T;
		this.fmax = fmax;
		this.fmin = fmin;
		this.brSampl = brSampl;
		this.setResizable(false);

		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		final BarChart<String, Number> bc = new BarChart<>(xAxis, yAxis);
		bc.setAnimated(false);
		bc.setTitle(MainScreen.get().comboBox.getValue().toString());
		xAxis.setLabel("Frequency");
		yAxis.setLabel("Magnitude");

		XYChart.Series series = new XYChart.Series();
		series.setName("Magnitude");

		double max = Double.MIN_VALUE;
		for (int i = 1; i < z.length; i++) {
			if (max < z[i])
				max = z[i];
		}

		ArrayList<XYChart.Data<String, Number>> lista = new ArrayList<>();
		for (int i = 1; i <= z.length / 2; i++) {
			Integer x = (int) (i / T);
			z[i] = z[i] * (100.0 / max);
			final XYChart.Data<String, Number> data = new XYChart.Data(x.toString() + "Hz", z[i]);

			data.nodeProperty().addListener(new ChangeListener<Node>() {
				@Override
				public void changed(ObservableValue<? extends Node> ov, Node oldNode, Node newNode) {
					if (newNode != null) {
						newNode.setStyle("-fx-bar-fill: navy;");
					}
				}
			});
			lista.add(data);

		}
		series.getData().addAll(lista);

		bc.setCategoryGap(0.0);
		bc.setBarGap(0);
		bc.setVerticalGridLinesVisible(false);
		bc.setBackground(Background.EMPTY);
		bc.setLegendVisible(false);
		bc.setDepthTest(DepthTest.DISABLE);
		
		ScrollPane scp = new ScrollPane();
		scp.setContent(bc);
		Scene scene = new Scene(scp, 800, 600);
		bc.setMinWidth(800);
		bc.setMinHeight(600);
		bc.getData().addAll(series);

		// ZOOM
		// -------------------------------------------------------------------------------------------
		final double SCALE_DELTA = 1.1;
		bc.setOnScroll(new EventHandler<ScrollEvent>() {
			public void handle(ScrollEvent event) {
				event.consume();

				if (event.getDeltaY() == 0) {
					return;
				}

				double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA : 1 / SCALE_DELTA;

				bc.setMinWidth(bc.getWidth() * scaleFactor);
				bc.setMinHeight(bc.getHeight() * scaleFactor);

				if (bc.getMinWidth() < 800)
					bc.setMinWidth(800);
				if (bc.getMinHeight() < 600)
					bc.setMinHeight(600);

				scp.layout();
				scp.setVvalue(1.0);
			}
		});

		// -------------------------------------------------------------------------------------------

		// EXPORT
		scp.setOnKeyPressed(new ExportEvent(bc, this));
		this.setScene(scene);
		show();
	}
}
