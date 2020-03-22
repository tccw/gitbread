package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmStage {
    public static final int WIDTH = 300;
    public static final int HEIGHT = 100;
    private static boolean answer;

    public static boolean display(String message, String title) {
        Stage stage = new Stage();
        stage.setMinWidth(WIDTH);
        stage.setMinHeight(HEIGHT);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(20));
        Label label = new Label(message);
        Button yes = new Button("Yes");
        Button no = new Button("No");
        yes.setOnMouseClicked(e -> {
            answer = true;
            stage.close();
        });
        no.setOnMouseClicked(e -> {
            answer = false;
            stage.close();
        });

        vbox.getChildren().addAll(label, yes, no);
        vbox.setAlignment(Pos.CENTER);
        stage.setScene(new Scene(vbox));
        stage.showAndWait();

        return answer;
    }


}
