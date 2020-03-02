package ui;


import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import model.RecipeCollection;
import model.RecipeHistory;
import persistence.Reader;
import persistence.steganography.Steganos;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;


// following thenewboston tutorial as reference
// https://www.youtube.com/watch?v=FLkOX4Eez6o&list=PL6gx4Cwl9DGBzfXLWLSYVy8EbTdpGbUIG
public class GitBreadGUI extends Application {
    ToggleButton toggle;
    Button saveButton;
    Button loadButton;
    TextField flourFraction;
    TextField waterFraction;
    TextField saltFraction;
    TextField sugarFraction;
    TextField fatFraction;
    TextField yeastFraction;
    private static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public static void main(String[] args) {
        launch(args);
    }

    // start contains the main JavaFX code for running and handling the application
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("GitBread");
        fieldAndButtons();
        Label label = new Label("Flour fraction");

        ListView<String> list = new ListView<String>();
        final ImageView target = new ImageView();
        target.setFitWidth(64);
        target.setFitHeight(64);
        target.setImage(new Image("file:./data/icons/buttons/breadbakingbyfreepik64.png"));
        // lambda implementation of the event handler. This works because there is only a single method in the
        // EventHandler interface.
        saveButton.setOnAction(e -> System.out.println("This will save"));
        loadButton.setOnAction(e -> System.out.println("This will load"));
        target.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getDragboard().hasFiles()) {
                    event.acceptTransferModes(TransferMode.ANY);
                }
                event.consume();
            }
        });
//        target.setOnDragDropped(new EventHandler<DragEvent>() {
//            @Override
//            public void handle(DragEvent event) {
//                List<File> files = event.getDragboard().getFiles();
//                try {
//                    Image img = new Image(new FileInputStream(files.get(0)));
//                    target.setImage(img);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        target.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                List<File> files = event.getDragboard().getFiles();
                try {
                    Steganos encoder = new Steganos();
                    RecipeCollection imageShareCol = Reader.loadRecipeCollectionJson(encoder.decode(files.get(0)));
                    ObservableList<String> items = FXCollections.observableArrayList();
                    for (Map.Entry<String, RecipeHistory> entry : imageShareCol.getCollection().entrySet()) {
                        items.add(entry.getKey());
                    }
                    list.setItems(items);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        });

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.add(flourFraction, 3, 0, 1, 1);
        gridPane.add(waterFraction, 3, 1, 1, 1);
        gridPane.add(saltFraction, 3, 2, 1, 1);
        gridPane.add(sugarFraction, 3, 3, 1, 1);
        gridPane.add(fatFraction, 3, 4, 1, 1);
        gridPane.add(yeastFraction, 3, 5, 1, 1);
        gridPane.add(saveButton, 8, 8, 1, 1);
        gridPane.add(toggle, 12, 9, 1, 1);
        gridPane.add(loadButton, 8, 9, 1, 1);
        gridPane.add(label, 4, 0, 1, 1);
        gridPane.add(target, 1, 12, 1, 1);
        gridPane.add(list, 0, 0, 2, 10);
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        Scene scene = new Scene(gridPane, WIDTH, HEIGHT);
//        scene.getStylesheets().add("./ui/gitbreadstyle.css");
        primaryStage.setScene(scene);
        // Image courtesy of Freepik on https://www.flaticon.com/free-icon/agronomy_1188035
        primaryStage.getIcons().add(new Image("file:./data/icons/wheatcolor512.png"));
        primaryStage.show();


    }

    //EFFECTS
    private void fieldAndButtons() {
        toggle = new ToggleButton();
        ImageView toggleView = new ImageView(new Image("file:./data/icons/buttons/calculator512.png"));
        toggleView.setFitHeight(32);
        toggleView.setFitWidth(32);
        toggle.setGraphic(toggleView);
        saveButton = new Button();
        loadButton = new Button();
        saveButton.setText("Save");
        loadButton.setText("Load");
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

    //EFFECTS: initialize the layout for the primaryStage
    private void setUpGUI() {
        //set up the layout
    }

//    @Override
//    public void handle(ActionEvent event) {
//        if(event.getSource()==button) { // the source can be different buttons
//            System.out.println("Thanks for clicking!");
//        }
//    }
}
