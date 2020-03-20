package ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.RecipeDevHistory;

import java.security.NoSuchAlgorithmException;

public class MergeStage {
    private static final int HEIGHT = 150;
    private static final int WIDTH = 250;
    private Stage stage = new Stage();
    private VBox vbox = new VBox(20);
    private Scene scene = new Scene(vbox, WIDTH, HEIGHT);
    private ChoiceBox<String> choiceBox = new ChoiceBox<>();
    private Button mergeButton = new Button("merge");

    public void display(RecipeDevHistory activeHistory) {
        stage.setTitle("Merge dialog");
        stage.initModality(Modality.APPLICATION_MODAL);
        choiceBox.getItems().addAll(activeHistory.getBranches());
        setUpScene(activeHistory);
        mergeButtonAction(activeHistory);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void setUpScene(RecipeDevHistory activeHistory) {
        vbox.getChildren().add(new Label(activeHistory.getCurrentBranch()));
        vbox.getChildren().add(choiceBox);
        vbox.getChildren().add(mergeButton);
        vbox.setAlignment(Pos.CENTER);
    }

    private void mergeButtonAction(RecipeDevHistory activeHistory) {
        mergeButton.setOnMouseClicked(e -> {
            try {
                activeHistory.merge(choiceBox.getValue());
                System.out.println(choiceBox.getValue());
                stage.close();
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }
        });
    }




}
