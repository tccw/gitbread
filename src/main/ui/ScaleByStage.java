package ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ScaleByStage {
    private static final int HEIGHT = 75;
    private static final int WIDTH = 200;
    private Stage window;
    private VBox vbox;
    private Scene scene;
    private TextField weight;
    private RadioButton doughWeight;
    private RadioButton flourWeight;
    private ToggleGroup scaleOptions;

    public void display() {
        initializeRadioButtons();
//        vbox.getChildren().add(0,this.doughWeight);
//        vbox.getChildren().add(1, this.flourWeight)
        vbox.getChildren().addAll(this.doughWeight, this.flourWeight, this.weight);
        vbox.setAlignment(Pos.CENTER_LEFT);
        window.setScene(scene);
        window.showAndWait();

    }

    private void initializeRadioButtons() {
        window = new Stage();
        vbox = new VBox(20);
        window.setTitle("Scale Recipe");
        this.doughWeight = new RadioButton("dough weight");
        this.flourWeight = new RadioButton("flour weight");
        this.scaleOptions = new ToggleGroup();
        this.scaleOptions.getToggles().addAll(doughWeight, flourWeight);
        this.weight = new TextField();
        this.weight.setPromptText("Weight in grams");
        this.scene = new Scene(vbox);
    }
}
