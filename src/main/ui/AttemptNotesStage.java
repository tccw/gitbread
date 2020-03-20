package ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.Attempt;
import model.RecipeDevHistory;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

public class AttemptNotesStage {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;
    private static final String[] LABEL_LIST = new String[]{"Crumb", "Crust", "Flavor", "Other Notes"};
    private static final File INITIAL_DIRECTORY = new File("./data/images/attemptphotos");
    private File photoPath;
    private Stage stage;
    private VBox vbox;
    private Scene scene;
    private Button selectPhoto;
    private Button add;

    public void display(Stage primaryStage, Attempt attempt) {
        initialize();
        setButtonAction(primaryStage, attempt);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void initialize() {
        stage = new Stage();
        stage.setTitle("Attempt notes");
        selectPhoto = new Button("select photo");
        photoPath = null;
        add = new Button("add");
        stage.initModality(Modality.APPLICATION_MODAL);
        List<TextArea> textFieldList = new ArrayList<TextArea>();
        vbox = new VBox(20);
        vbox.setPadding(new Insets(20));

        for (int i = 0; i < 4; i++) {
            TextArea tempTextArea = new TextArea();
            tempTextArea.setWrapText(true);
            vbox.getChildren().add(new Label(LABEL_LIST[i]));
            vbox.getChildren().add(tempTextArea);

        }
        vbox.getChildren().add(selectPhoto);
        vbox.getChildren().add(add);
        scene = new Scene(vbox, WIDTH, HEIGHT);
    }

    private void setButtonAction(Stage primaryStage, Attempt attempt) {
        add.setOnMouseClicked(e -> {
            ArrayList<TextArea> fields = new ArrayList<>();
            for (int i = 1; i < vbox.getChildren().size() - 2; i += 2) {
                fields.add((TextArea) (vbox.getChildren().get(i)));
            }
            attempt.setResultNotes(fields.get(0).getText(),
                    fields.get(1).getText(),
                    fields.get(2).getText(),
                    fields.get(3).getText());
            if (photoPath != null) {
                attempt.setPhotoPath(photoPath);
            }
            stage.close();

        });

        //TODO: make path relative.
        selectPhoto.setOnMouseClicked(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(INITIAL_DIRECTORY);
            FileChooser.ExtensionFilter extensionFilter =
                    new FileChooser.ExtensionFilter("Image files (*.png)", "*.png", "*.jpg");
            fileChooser.getExtensionFilters().add(extensionFilter);
            photoPath = fileChooser.showOpenDialog(primaryStage);
//            photoPath = getRelativePath(fileChooser.showOpenDialog(primaryStage));
        });

    }

    private File getRelativePath(File file) {
        String path = String.valueOf(file);
        char[] pathCharArray = path.toCharArray();
        char c = pathCharArray[pathCharArray.length - 1];
        StringBuilder fileName = new StringBuilder();
        int i = 1;
        while (c != '\\') {
            fileName.append(c);
            c = pathCharArray[pathCharArray.length - 1 - i];
            i++;
        }
        URL url = getClass().getResource(fileName.reverse().toString());
        return new File(url.getPath());
    }

}
