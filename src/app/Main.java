package app;

import javafx.stage.Stage;
import view.MainScreen;
import javafx.application.Application;

public class Main extends Application {

	public void start(Stage primaryStage) {
		MainScreen.get();
	}

	public static void main(String[] args) {
		launch(args);
		System.out.println("Finished..");
	}

}
