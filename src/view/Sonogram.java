package view;

import java.util.ArrayList;

import actions.ExportEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Circle;

public class Sonogram extends Form {

	public Sonogram(ArrayList<Double> parametri) {
		super("Sonogram");
		this.setResizable(false);
		ScrollPane scp = new ScrollPane();

		NumberAxis xAxis = new NumberAxis();
		xAxis.setLabel("Time");

		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Frequency");

		ScatterChart<String, Number> scatterChart = new ScatterChart(xAxis, yAxis);
		scatterChart.setAnimated(false);

		XYChart.Series series = new XYChart.Series();
		ArrayList<XYChart.Data<Number, Number>> dataList = new ArrayList<>();

		int i = 0;
		while (i < parametri.size()) {
			double hz = parametri.get(i++);
			double t = parametri.get(i++);
			double magnitude = parametri.get(i++);

			XYChart.Data<Number, Number> data;
			data = new XYChart.Data(t, hz);

			Circle dot = new Circle(5);
			dot.setStyle("-fx-fill: black;");
			data.setNode(dot);
			data.getNode().setOpacity(magnitude);

			dataList.add(data);
		}

		series.getData().addAll(dataList);

		scatterChart.getData().add(series);
		scatterChart.setMinWidth(800);
		scatterChart.setMinHeight(600);
		scatterChart.setLegendVisible(false);
		scp.setContent(scatterChart);

		// ZOOM
		// -------------------------------------------------------------------------------------------
		final double SCALE_DELTA = 1.1;
		scatterChart.setOnScroll(new EventHandler<ScrollEvent>() {
			public void handle(ScrollEvent event) {
				event.consume();

				if (event.getDeltaY() == 0) {
					return;
				}

				double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA : 1 / SCALE_DELTA;

				scatterChart.setMinWidth(scatterChart.getWidth() * scaleFactor);
				scatterChart.setMinHeight(scatterChart.getHeight() * scaleFactor);

				if (scatterChart.getMinWidth() < 800)
					scatterChart.setMinWidth(800);
				if (scatterChart.getMinHeight() < 600)
					scatterChart.setMinHeight(600);

				scp.layout();
				scp.setVvalue(1.0);
			}
		});

		// -------------------------------------------------------------------------------------------

		

		
		Scene scene = new Scene(scp, 800, 600);
		
		scp.setOnKeyPressed(new ExportEvent(scatterChart, this));
		this.setScene(scene);
		show();
	}

}
