package ui;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import model.RecipeDevCollection;
import model.RecipeDevHistory;
import persistence.steganography.Steganos;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static persistence.Reader.*;


// following thenewboston tutorial as reference
// https://www.youtube.com/watch?v=FLkOX4Eez6o&list=PL6gx4Cwl9DGBzfXLWLSYVy8EbTdpGbUIG
public class GitBreadGUI extends Application {
    ToggleButton darkModeToggle;
    Button scaleButton;
    Button loadButton;

    RecipeDevCollection activeCollection;
    ListView<String> recipeListView;
    ObservableList<String> items;
    TextArea instructionsListView;

    FlowPane flow;
    GridPane gridPane;

    private static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    boolean darkMode = false;

    public static void main(String[] args) {
        launch(args);
    }

    // start contains the main JavaFX code for running and handling the application
    @Override
    public void start(Stage primaryStage) throws Exception {
        activeCollection = new RecipeDevCollection();
        primaryStage.setTitle("GitBread");
        fieldAndButtons();
        flow = addFlowPane();
        gridPane = new GridPane();

        recipeListView = new ListView<String>();
        instructionsListView = new TextArea();
        instructionsListView.setWrapText(true);
        items = FXCollections.observableArrayList();

        //make tooltips
        darkModeToggle.setTooltip(new Tooltip("Toggle DarkMode"));
        setFlowPaneTooltips();

        primaryStage.sizeToScene();
        primaryStage.setMinHeight(HEIGHT);
        primaryStage.setMinWidth(WIDTH);
//        gridPane.setGridLinesVisible(true);
        gridPane.setPadding(new Insets(10, 20, 20, 20));
        gridPane.add(flow, 0, 0, 5, 10);
        gridPane.add(darkModeToggle, 13, 0, 1, 1);
        gridPane.add(recipeListView, 0, 2, 4, 10);
        gridPane.add(instructionsListView, 4, 2, 10, 10);
        gridPane.setHgap(20);
        gridPane.setVgap(10);
        Scene scene = new Scene(gridPane, WIDTH, HEIGHT);
        scene.getStylesheets().add("./ui/gitbreadlightstyle.css");
        primaryStage.setScene(scene);
        // Image courtesy of Freepik on https://www.flaticon.com/free-icon/agronomy_1188035
        primaryStage.getIcons().add(new Image("file:./data/icons/wheatcolor512.png"));
        primaryStage.show();

        ImageView recipeListPlaceHolder = new ImageView(new Image("file:./data/icons/sharing/recipecollection.png"));
        recipeListPlaceHolder.setOpacity(0.5);
        recipeListView.setPlaceholder(recipeListPlaceHolder);
        recipeListView.setMinWidth(204);
        recipeListView.setMaxWidth(204);
        recipeListView.setOnDragOver(event -> {
                    if (event.getDragboard().hasFiles()) {
                        event.acceptTransferModes(TransferMode.ANY);
                    }
                    event.consume();
                }
        );
        recipeListView.setOnDragDropped(event -> {
            List<File> files = event.getDragboard().getFiles();
            try {
                if (files.get(0).getName().contains(".png") || files.get(0).getName().contains(".PNG")) {
                    Steganos encoder = new Steganos();
                    activeCollection = loadRecipeCollectionJson(encoder.decode(files.get(0)));
                    addItemsListView();
                } else {
                    throw new IOException();
                }
            } catch (IOException e) {
                System.err.println("Problem loading the collection.");
            }
        });

        flow.getChildren().get(0).setOnMouseClicked(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("./data/recipecollections"));
            fileChooser.setTitle("Load Recipe Collection");
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                openFile(file);
            }
        });

        flow.getChildren().get(1).setOnMouseClicked(e -> {
            RecipeStage stage = new RecipeStage();
            stage.display(activeCollection);
            addItemsListView();
            recipeListView.refresh();
        });

        darkModeToggle.setOnAction(e -> {
            if (!darkMode) {
                scene.getStylesheets().add("./ui/gitbreaddarkstyle.css");
                darkMode = true;
            } else {
                scene.getStylesheets().remove("./ui/gitbreaddarkstyle.css");
                scene.getStylesheets().add("./ui/gitbreadlightstyle.css");
                darkMode = false;
            }
        });

        recipeListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    instructionsListView.setText(activeCollection.get(newValue)
                            .getActiveCommit()
                            .getRecipeVersion()
                            .toString());
                }
            }
        });


    }

    private FlowPane addFlowPane() {
        int numImages = 4;
        FlowPane flow = new FlowPane();
        flow.setPadding(new Insets(5, 0, 5, 0));
        flow.setVgap(2);
        flow.setHgap(4);
        Button[] pages = new Button[numImages];
        String[] urls = new String[]{"file:./data/icons/sharing/recipecollection32.png",
                "file:./data/icons/sharing/addrecipe32.png",
                "file:./data/icons/sharing/exportrecipecollectionshare32.png",
                "file:./data/icons/sharing/exportrecipe32.png"};
        for (int i = 0; i < numImages; i++) {
            pages[i] = new Button();
            pages[i].setGraphic(new ImageView(new Image(urls[i])));
        }
        for (int i = 0; i < numImages; i++) {
            flow.getChildren().add(pages[i]);
        }
        return flow;
    }

    private void setFlowPaneTooltips() {
        Tooltip.install(flow.getChildren().get(0), new Tooltip("Load recipe books!"));
        Tooltip.install(flow.getChildren().get(1), new Tooltip("Load recipes!"));
        Tooltip.install(flow.getChildren().get(2), new Tooltip("Export recipe books!"));
        Tooltip.install(flow.getChildren().get(3), new Tooltip("Export recipes!"));
    }

    private void openFile(File file) {
        try {
            System.out.println("loadin' up");
            activeCollection = loadRecipeCollectionFile(file);
            addItemsListView();
        } catch (IOException e) {
            System.err.println("Error loading the file.");
        }
    }

    private void addItemsListView() {
        recipeListView.getItems().clear();
        for (Map.Entry<String, RecipeDevHistory> entry : activeCollection.getCollection().entrySet()) {
            items.add(entry.getKey());
        }
        recipeListView.setItems(items);
    }

    //EFFECTS
    private void fieldAndButtons() {
        darkModeToggle = new ToggleButton();
        ImageView toggleView = new ImageView(new Image("file:./data/icons/buttons/moonbyfreepik24.png"));
        toggleView.setFitHeight(24);
        toggleView.setFitWidth(24);
        darkModeToggle.setGraphic(toggleView);
//        scaleButton = new Button();
//        loadButton = new Button();
//        scaleButton.setText("Save");
//        loadButton.setText("Load");
    }

    //EFFECTS: initialize the layout for the primaryStage
    private void setUpGUI() {
        //set up the layout
    }
}
