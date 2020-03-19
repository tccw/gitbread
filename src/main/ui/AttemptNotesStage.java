package ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.Attempt;

import java.util.List;
import java.util.ArrayList;

public class AttemptNotesStage {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;
    private static final String[] labelList = new String[]{"Crumb", "Crust", "Flavor", "Other Notes"};
    private Stage stage;
    private VBox vbox;
    private Scene scene;
    private Button add;

    public void display(Attempt attempt) {
        initialize();
        setButtonAction(attempt);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void initialize() {
        stage = new Stage();
        stage.setTitle("Attempt notes");
        add = new Button("add");
        stage.initModality(Modality.APPLICATION_MODAL);
        List<TextArea> textFieldList = new ArrayList<TextArea>();
        vbox = new VBox(20);
        vbox.setPadding(new Insets(20));

        for (int i = 0; i < 4; i++) {
            TextArea tempTextArea = new TextArea();
            tempTextArea.setWrapText(true);
            vbox.getChildren().add(new Label(labelList[i]));
            vbox.getChildren().add(tempTextArea);

        }
        vbox.getChildren().add(add);
        scene = new Scene(vbox, WIDTH, HEIGHT);
    }

    private void setButtonAction(Attempt attempt) {
        add.setOnMouseClicked(e -> {
            ArrayList<TextArea> fields = new ArrayList<>();
            for (int i = 1; i < vbox.getChildren().size(); i += 2) {
                fields.add((TextArea) (vbox.getChildren().get(i)));
            }
            attempt.setResultNotes(fields.get(0).getText(),
                    fields.get(1).getText(),
                    fields.get(2).getText(),
                    fields.get(3).getText());
            stage.close();
        });
    }

}
