package ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.BreadRecipe;
import model.Recipe;
import model.RecipeDevCollection;
import model.RecipeDevHistory;

import java.security.NoSuchAlgorithmException;

public class RecipeStage {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private Button add;
    private TextField name;
    private TextField flourFraction;
    private TextField waterFraction;
    private TextField saltFraction;
    private TextField sugarFraction;
    private TextField fatFraction;
    private TextField yeastFraction;
    private TextField[] fields;
    private Stage window;
    private BorderPane borderPane;
    private Scene scene;
    private VBox buttonArea;
    private TextArea instructions;
    private RecipeDevCollection current;

    //EFFECTS: display the recipe entry stage.
    public void display(RecipeDevCollection current) {
        this.current = current;
        initializeWindows();
        borderPane.setPadding(new Insets(15,15,15,15));
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Add Recipe");
        window.setMinWidth(400);
        buttonSetup();
        borderPane.setLeft(buttonArea);
        borderPane.setCenter(instructions);
        window.setScene(scene);
        window.showAndWait();
    }

    private void initializeWindows() {
        window = new Stage();
        borderPane = new BorderPane();
        scene = new Scene(borderPane, WIDTH, HEIGHT);
        buttonArea = new VBox();
        instructions = new TextArea();
        instructions.setWrapText(true);
    }

    private void buttonSetup() {
        makeFields();
        makeButtons();
        placeUIElements();
    }

    private void makeFields() {
        fields = new TextField[]{name, waterFraction, saltFraction,sugarFraction,fatFraction,yeastFraction};
        name = new TextField();
        name.setPromptText("Recipe name");
        flourFraction = new TextField();
        flourFraction.setText("100%");
        flourFraction.setEditable(false);
        waterFraction = new TextField();
        saltFraction = new TextField();
        sugarFraction = new TextField();
        fatFraction = new TextField();
        yeastFraction = new TextField();
        waterFraction.setPromptText("Water weight%");
        saltFraction.setPromptText("Salt weight%");
        sugarFraction.setPromptText("Sugar weight%");
        fatFraction.setPromptText("Fat weight%");
        yeastFraction.setPromptText("Yeast weight%");
    }

    private void placeUIElements() {
        buttonArea.getChildren().addAll(
                name,
                flourFraction,
                waterFraction,
                saltFraction,
                sugarFraction,
                fatFraction,
                yeastFraction,
                add);
        buttonArea.setSpacing(10);
    }

    private void makeButtons() {
        add = new Button("Add Recipe");
        add.setOnAction(e -> {
            BreadRecipe firstVersion = new BreadRecipe(1000);
            firstVersion.setWaterFraction(Double.parseDouble(waterFraction.getText()) / 100);
            firstVersion.setSaltFraction(Double.parseDouble(saltFraction.getText()) / 100);
            firstVersion.setSugarFraction(Double.parseDouble(sugarFraction.getText()) / 100);
            firstVersion.setFatFraction(Double.parseDouble(fatFraction.getText()) / 100);
            firstVersion.setYeastFraction(Double.parseDouble(yeastFraction.getText()) / 100);
            firstVersion.setInstructions(instructions.getText());
            firstVersion.scaleByDoughWeight(5000);
            try {
                this.current.add(name.getText(), new RecipeDevHistory(firstVersion));
                window.close();
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }
        });
    }

}
