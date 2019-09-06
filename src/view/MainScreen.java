package view;

import actions.HistogramEvent;
import actions.RandomSignalEvenet;
import actions.SonogramEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class MainScreen extends Form {

	private static MainScreen instance = null;
	private double[] z;
	public TextField directory;
	public TextField sirinaT;
	public ComboBox<String> comboBox;
	public TextField sliceSize;
	public TextField harmonicNumber;
	
	public static MainScreen get() {
		if (instance == null)
			new MainScreen();
		return instance;
	}

	public MainScreen() {
		super("Prepoznavanje Govora Domaci1");
		instance = this;
		

		this.setHeight(600);
		this.setWidth(400);
		this.setResizable(false);

		GridPane pane = new GridPane();

		pane.setStyle("-fx-background-color: #d6eaff;");
		pane.setVgap(7);
		pane.setHgap(7);
		pane.setPadding(new Insets(50, 10, 10, 70));
		pane.setMinWidth(400);
		pane.setMinHeight(200);

		Label label1 = new Label("\t\tUnesite putanju do .wav fajla");
		pane.add(label1, 0, 1);

		directory = new TextField("D:/tones/20000hz.wav");
		directory.setAlignment(Pos.BASELINE_CENTER);
		directory.setMinWidth(260);
		pane.add(directory, 0, 2);

		Label label2 = new Label("\t\tSirina prozora u ms:");
		label2.setPadding(new Insets(15, 0, 0, 25));
		pane.add(label2, 0, 3);

		sirinaT = new TextField("15");
		sirinaT.setAlignment(Pos.BASELINE_CENTER);
		pane.add(sirinaT, 0, 4);

		ObservableList<String> options = FXCollections.observableArrayList("None", "Hanning", "Hamming");
		comboBox = new ComboBox<String>(options);
		comboBox.setValue("None");
		comboBox.setTranslateX(80);
		pane.add(comboBox, 0, 5);
		

		Button wavBtn = new Button("Generate Histogram");
		wavBtn.setTranslateY(10);
		wavBtn.setTranslateX(70);
		pane.add(wavBtn, 0, 6);

		wavBtn.setOnAction(new HistogramEvent()); //HISTOGRAM ACTION
		
		Label label3 = new Label("\t\t\tSlice size:");
		label3.setPadding(new Insets(15, 0, 0, 25));
		label3.setTranslateY(20);
		pane.add(label3, 0, 7);
		
		sliceSize = new TextField("100");
		sliceSize.setAlignment(Pos.BASELINE_CENTER);
		sliceSize.setMinWidth(260);
		sliceSize.setTranslateY(20);
		pane.add(sliceSize, 0, 8);
		
		Button sonBtn = new Button("Generate Sonogram");
		sonBtn.setTranslateY(30);
		sonBtn.setTranslateX(70);
		pane.add(sonBtn, 0, 9);

		sonBtn.setOnAction(new SonogramEvent());
		
		Button randGen = new Button("Generisi random zvucni signal");
		randGen.setTranslateY(80);
		randGen.setTranslateX(45);
		pane.add(randGen, 0, 10);
		
		randGen.setOnAction(new RandomSignalEvenet());
		
		Label label4 = new Label("Number of harmonics:");
		label4.setPadding(new Insets(15, 0, 0, 25));
		label4.setTranslateY(65);
		label4.setTranslateX(50);
		pane.add(label4, 0, 11);

		harmonicNumber = new TextField("5");
		harmonicNumber.setAlignment(Pos.BASELINE_CENTER);
		harmonicNumber.setMinWidth(260);
		harmonicNumber.setTranslateY(60);
		pane.add(harmonicNumber, 0, 12);
		
		setPane(pane);
	}

	public double[] getZ() {
		return z;
	}

	public void setZ(double[] z) {
		this.z = z;
	}

}
