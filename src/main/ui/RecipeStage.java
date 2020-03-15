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
import model.RecipeDevCollection;
import model.RecipeDevHistory;

import java.security.NoSuchAlgorithmException;

public class RecipeStage {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private Button add;
    private TextField[] fields;
    private Stage window;
    private BorderPane borderPane;
    private Scene scene;
    private VBox buttonArea;
    private TextArea instructions;
    private RecipeDevCollection collection;
    private RecipeDevHistory activeHistory;

    //EFFECTS: display the recipe entry stage.
    public void display(RecipeDevCollection collection, RecipeDevHistory activeHistory, Boolean isNewRecipe) {
        this.collection = collection;
        this.activeHistory = activeHistory;
        initializeWindows();
        borderPane.setPadding(new Insets(15));
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Add Recipe");
        window.setMinWidth(400);
        elementSetup();
        if (!isNewRecipe) {
            setFieldsToCurrentCommit();
        }
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

    private void elementSetup() {
        makeFields();
        makeButtons();
        placeUIElements();
    }

    private void setFieldsToCurrentCommit() {
        BreadRecipe recipeToEdit = (BreadRecipe) (this.activeHistory.getActiveCommit().getRecipeVersion());
        fields[0].setEditable(false);
        fields[0].setText(); //TODO: set the title as the key of the recipe
        fields[1].setText(String.valueOf(recipeToEdit.getWaterFraction() * 100));
        fields[2].setText(String.valueOf(recipeToEdit.getSaltFraction() * 100));
        fields[3].setText(String.valueOf(recipeToEdit.getSugarFraction() * 100));
        fields[4].setText(String.valueOf(recipeToEdit.getFatFraction() * 100));
        fields[5].setText(String.valueOf(recipeToEdit.getYeastFraction() * 100));
        fields[6].setText(String.valueOf(recipeToEdit.getCookTemp() * 100));
        fields[7].setText(String.valueOf(recipeToEdit.getCookTime() * 100));
        fields[8].setText(String.valueOf(recipeToEdit.getPrepTime() * 100));
        this.instructions.setText(recipeToEdit.getInstructions());
    }

    private void makeFields() {
        String[] promptText = new String[]{"Recipe name", "", "Water weight%", "Salt weight%", "Sugar weight%",
                "Fat weight%", "Yeast weight%", "Bake temp [deg. F]", "Bake time [mins]", "Prep time [mins]"};
        fields = new TextField[promptText.length];
        for (int i = 0; i < promptText.length; i++) {
            fields[i] = new TextField();
            fields[i].setPromptText(promptText[i]);
        }
        fields[1].setText("100%");
        fields[1].setEditable(false);
    }

    private void placeUIElements() {
        for (TextField field : fields) {
            buttonArea.getChildren().add(field);
        }
        buttonArea.getChildren().add(add);
        buttonArea.setSpacing(10);
    }

    private void makeButtons() {
        add = new Button("Add Recipe");
        add.setOnAction(e -> {
            BreadRecipe firstVersion = new BreadRecipe(1000);
            firstVersion.setWaterFraction(Double.parseDouble(fields[2].getText()) / 100);
            firstVersion.setSaltFraction(Double.parseDouble(fields[3].getText()) / 100);
            firstVersion.setSugarFraction(Double.parseDouble(fields[4].getText()) / 100);
            firstVersion.setFatFraction(Double.parseDouble(fields[5].getText()) / 100);
            firstVersion.setYeastFraction(Double.parseDouble(fields[6].getText()) / 100);
            firstVersion.setCookTemp(Integer.parseInt(fields[7].getText()));
            firstVersion.setCookTime(Integer.parseInt(fields[8].getText()));
            firstVersion.setPrepTime(Integer.parseInt(fields[9].getText()));
            firstVersion.setInstructions(instructions.getText());
//            firstVersion.scaleByDoughWeight(900);
            try {
                this.collection.add(fields[0].getText(), new RecipeDevHistory(firstVersion));
                window.close();
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }
        });
    }

}
