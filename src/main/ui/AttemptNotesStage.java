package ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class AttemptNotesStage {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final String[] labelList = new String[] {"Crumb", "Crust", "Flavor", "Other Notes"};
    private Stage stage;
    private VBox vbox;
    private Scene scene;
    private TextArea crumbNote;
    private TextArea crustNote;
    private TextArea flavorNote;
    private TextArea otherNotes;
    private List<TextArea> textFieldList;
    private List<Label> labels;

    public void display() {
        initialize();
    }

    private void initialize() {
        stage = new Stage();
        textFieldList = new ArrayList<TextArea>();
        vbox = new VBox(30);
        vbox.setPadding(new Insets(20));

        for (int i = 0; i < 4; i++) {
//            vbox.getChildren().add(new TextArea());
            labels.add(new Label(labelList[i]));
        }
        crumbNote = textFieldList.get(0);
        crustNote = textFieldList.get(1);
        flavorNote = textFieldList.get(2);
        otherNotes = textFieldList.get(3);
        scene = new Scene(vbox, HEIGHT, WIDTH);
    }

}
