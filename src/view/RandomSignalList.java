package view;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;

public class RandomSignalList extends Form {

	public RandomSignalList(ArrayList<String> harmonics) {
		super("Harmonic List");

		ListView<String> list = new ListView<String>();
		list.setPrefWidth(300);
		list.setPrefHeight(400);

		ObservableList<String> items =FXCollections.observableArrayList ();
		for(int i = 0; i< harmonics.size(); i++)
			items.add(harmonics.get(i));
		list.setItems(items);

		Scene scene = new Scene(list);
		this.setScene(scene);
		this.show();
	}

}
