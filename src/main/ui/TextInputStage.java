package ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TextInputStage {
    private static final int HEIGHT = 150;
    private static final int WIDTH = 250;
    private static String returnString;

    public static String display(String title, String message, String promptText) {
        Stage stage = new Stage();
        VBox vbox = new VBox(20);
        Scene scene = new Scene(vbox, WIDTH, HEIGHT);
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                returnString = textField.getText();
                stage.close();
            }
        });
        vbox.getChildren().add(new Text(message));
        vbox.getChildren().add(textField);
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        vbox.setAlignment(Pos.CENTER);
        stage.showAndWait();
        return returnString;
    }

}

