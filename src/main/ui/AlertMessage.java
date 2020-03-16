package ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertMessage {

    public static void display(String message, String title) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        VBox vbox = new VBox(20);

        Label label = new Label();
        label.setText(message);
        Button close = new Button("close");
        close.setOnAction(e -> stage.close());

        vbox.getChildren().addAll(label, close);
        vbox.setAlignment(Pos.CENTER);
        Scene layout = new Scene(vbox);
        stage.setScene(layout);
        stage.showAndWait();
    }
}
