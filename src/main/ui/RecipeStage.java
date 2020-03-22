package ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private static final int HEIGHT = 700;
    private Button add;
    private TextField[] fields;
    private Label[] labels;
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
        borderPane.setPadding(new Insets(5));
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Add Recipe");
        window.setMinWidth(400);
        elementSetup(isNewRecipe);
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

    private void elementSetup(boolean isNewRecipe) {
        makeFields();
        makeButtons(isNewRecipe);
        placeUIElements();
    }

    private void setFieldsToCurrentCommit() {
        BreadRecipe recipeToEdit = (BreadRecipe) (this.activeHistory.getActiveCommit().getRecipeVersion());
        fields[0].setEditable(false);
        fields[0].setText("Title"); //TODO: set the title as the key of the recipe
        fields[2].setText(String.valueOf(recipeToEdit.getWaterFraction() * 100));
        fields[3].setText(String.valueOf(recipeToEdit.getSaltFraction() * 100));
        fields[4].setText(String.valueOf(recipeToEdit.getSugarFraction() * 100));
        fields[5].setText(String.valueOf(recipeToEdit.getFatFraction() * 100));
        fields[6].setText(String.valueOf(recipeToEdit.getYeastFraction() * 100));
        fields[7].setText(String.valueOf(recipeToEdit.getCookTemp()));
        fields[8].setText(String.valueOf(recipeToEdit.getCookTime()));
        fields[9].setText(String.valueOf(recipeToEdit.getPrepTime()));
        this.instructions.setText(recipeToEdit.getInstructions());
    }

    private void makeFields() {
        String[] promptText = new String[]{"Recipe name", "Flour weight%", "Water weight%", "Salt weight%",
                "Sugar weight%", "Fat weight%", "Yeast weight%", "Bake temp [deg. F]", "Bake time [mins]",
                "Prep time [mins]"};
        fields = new TextField[promptText.length];
        labels = new Label[promptText.length];
//        PercentageStringConverter percent = new PercentageStringConverter(new DecimalFormat("##%"));
        for (int i = 0; i < promptText.length; i++) {
            fields[i] = new TextField();
            fields[i].setPromptText(promptText[i]);
            labels[i] = new Label(promptText[i]);
        }
        fields[1].setText("100%");
        fields[1].setEditable(false);
    }

    private void placeUIElements() {
        for (int i = 0; i < labels.length; i++) {
            buttonArea.getChildren().add(labels[i]);
            buttonArea.getChildren().add(fields[i]);
        }
        buttonArea.getChildren().add(add);
        buttonArea.setSpacing(10);
    }

    private void makeButtons(boolean isNewRecipe) {
        add = new Button("Add Recipe");

        add.setOnAction(e -> {
            boolean notDouble = false;
            for (int i = 2; i < fields.length; i++) {
                if (!isDouble(fields[i])) {
                    AlertStage.display("Please check that all fields are numbers.", "NumberFormatException");
                    notDouble = true;
                    break;
                }
            }
            if (!notDouble) {
                makeCommit(isNewRecipe);
            }
        });
    }

    private void makeCommit(boolean isNewRecipe) {
        BreadRecipe recipe = new BreadRecipe(1000);
        recipe.setWaterFraction(Double.parseDouble(fields[2].getText()) / 100);
        recipe.setSaltFraction(Double.parseDouble(fields[3].getText()) / 100);
        recipe.setSugarFraction(Double.parseDouble(fields[4].getText()) / 100);
        recipe.setFatFraction(Double.parseDouble(fields[5].getText()) / 100);
        recipe.setYeastFraction(Double.parseDouble(fields[6].getText()) / 100);
        recipe.setCookTemp(Integer.parseInt(fields[7].getText()));
        recipe.setCookTime(Integer.parseInt(fields[8].getText()));
        recipe.setPrepTime(Integer.parseInt(fields[9].getText()));
        recipe.setInstructions(instructions.getText());
        try {
            if (!isNewRecipe) {
                this.activeHistory.commit(recipe);
            } else {
                this.collection.add(fields[0].getText(), new RecipeDevHistory(recipe));
            }
            window.close();
        } catch (NoSuchAlgorithmException event) {
            event.printStackTrace();
        }
    }

    private boolean isDouble(TextField input) {
        try {
            double field = Double.parseDouble(input.getText()); // if this line executes return true
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
