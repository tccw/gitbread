package ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.BreadRecipe;
import model.RecipeDevHistory;

public class ScaleByStage {
    private static final int HEIGHT = 75;
    private static final int WIDTH = 200;
    private Stage window;
    private VBox vbox;
    private Scene scene;
    private TextField weight;
    private RadioButton doughWeight;
    private Button scaleButton;
    private RadioButton flourWeight;
    private ToggleGroup scaleOptions;
    private BreadRecipe recipe;

    public void display(RecipeDevHistory activeRecipeHistory) {
        recipe = (BreadRecipe) (activeRecipeHistory.getActiveCommit().getRecipeVersion());
        initializeRadioButtons();
        vbox.getChildren().addAll(this.doughWeight, this.flourWeight, this.weight, this.scaleButton);
        vbox.setAlignment(Pos.CENTER_LEFT);
        window.setScene(scene);
        window.showAndWait();

    }

    private void initializeRadioButtons() {
        window = new Stage();
        vbox = new VBox(20);
        this.scaleButton = new Button("Scale");
        window.setTitle("Scale Recipe");
        this.doughWeight = new RadioButton("dough weight");
        this.flourWeight = new RadioButton("flour weight");
        this.scaleOptions = new ToggleGroup();
        this.scaleOptions.getToggles().addAll(doughWeight, flourWeight);
        this.weight = new TextField();
        this.weight.setPromptText("Weight in grams");
        this.scene = new Scene(vbox);
        setScaleButton();
    }

    private void setScaleButton() {
        this.scaleButton.setOnMouseClicked(e -> {
            if (this.scaleOptions.getSelectedToggle().equals(this.doughWeight)) {
                recipe.scaleByDoughWeight(Integer.parseInt(this.weight.getText()));
            } else {
                recipe.scaleByFlourWeight(Integer.parseInt(this.weight.getText()));
            }
            window.close();
        });
    }


}
