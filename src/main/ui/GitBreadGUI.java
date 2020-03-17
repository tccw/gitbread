package ui;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import model.RecipeDevCollection;
import model.RecipeDevHistory;
import persistence.steganography.Steganos;

import java.io.File;
import java.io.IOException;
import java.time.Clock;
import java.util.List;
import java.util.Map;

import static persistence.Reader.*;


// following thenewboston tutorial as reference
// https://www.youtube.com/watch?v=FLkOX4Eez6o&list=PL6gx4Cwl9DGBzfXLWLSYVy8EbTdpGbUIG
public class GitBreadGUI extends Application {
    private Clock clock = Clock.systemDefaultZone();
    private static final String[] topRecipeBarIcons = new String[]{"file:./data/icons/sharing/recipecollection32.png",
            "file:./data/icons/sharing/addrecipe32.png",
            "file:./data/icons/sharing/exportrecipecollectionshare32.png",
            "file:./data/icons/sharing/exportrecipe32.png"};
    private static final String[] bottomRecipeBarIcons = new String[]{
            "file:./data/icons/buttons/mixingbyfreepik.png",
            "file:./data/icons/buttons/scalebysmashicons.png",
            "file:./data/icons/buttons/versionbysmashicons.png",
            "file:./data/icons/buttons/agronomy.png"};
    ToggleButton darkModeToggle;

    RecipeDevCollection activeCollection;
    RecipeDevHistory activeRecipeHistory;
    ListView<String> recipeListView;
    ObservableList<String> items;
    TextArea instructionsTextArea;
    Label infoLabel;

    FlowPane flowTopRow;
    FlowPane flowBottomRow;
    GridPane gridPane;
    Scene scene;

    private static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    boolean darkMode = false;

    public static void main(String[] args) {
        launch(args);
    }

    // start contains the main JavaFX code for running and handling the application
    @Override
    public void start(Stage primaryStage) {
        initialize(primaryStage);
        setTooltips();
        gridPaneLayoutSetup(primaryStage);
        recipeListViewSetup();
        setUpButtonActions(primaryStage);
    }

    private void setUpButtonActions(Stage primaryStage) {
        // Open a file browser to load a JSON text file of a recipe collection
        loadJsonButton(primaryStage);

        // Open the add recipe stage
        newRecipeButton();

        // save the recipe collection as a sharable PNG.
        saveAsImageButton(primaryStage);

        // log an attempt with the active recipe version
        //TODO: fix or deal with loading issue where active commit is loaded as a separate object. Attempts don't work
        //      properly until the branch is reselected.
        logAttemptButton();

        // scale and re-display the active recipe in the instructions TextArea
        scaleRecipeButton();

        // make a new branch
        newBranchButton();

        //make a new commit to the current recipe and branch
        newCommitToCurrentBranchButton();

        darkModeToggle();

        recipeListViewListener();
    }

    private void recipeListViewListener() {
        recipeListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                activeRecipeHistory = activeCollection.get(newValue);
                updateTextArea();
                infoLabel.setText(String.format("Attempted count: %1$d :: Modified count %2$d",
                        activeRecipeHistory.totalAttempts(), activeRecipeHistory.getCommits().size() - 1));
            }
        });
    }

    private void darkModeToggle() {
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
    }

    private void newCommitToCurrentBranchButton() {
        flowBottomRow.getChildren().get(3).setOnMouseClicked(e -> {
            if (activeRecipeHistory != null) {
                RecipeStage recipeStage = new RecipeStage();
                recipeStage.display(activeCollection, activeRecipeHistory, false);
                addItemsListView();
                recipeListView.refresh();
            }
        });
    }

    private void newBranchButton() {
        flowBottomRow.getChildren().get(2).setOnMouseClicked(e -> {
            if (activeRecipeHistory != null) {
                Stage stage = new Stage();
                stage.setTitle("New branch name");
                TextField branchName = new TextField();
                stage.initModality(Modality.APPLICATION_MODAL);
                branchName.setPromptText("branch name");
                VBox vbox = new VBox();
                vbox.setPadding(new Insets(10));
                vbox.setAlignment(Pos.CENTER_RIGHT);
                vbox.getChildren().add(branchName);
                Scene layout = new Scene(vbox);
                stage.setScene(layout);
                branchName.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        activeRecipeHistory.newBranch(branchName.getText());
                        RecipeStage recipeStage = new RecipeStage();
                        recipeStage.display(activeCollection, activeRecipeHistory, false);
                        addItemsListView();
                        recipeListView.refresh();
                        stage.close();
                    } else if (event.getCode() == KeyCode.ESCAPE) {
                        stage.close();
                    }
                });
                stage.showAndWait();
            }
        });
    }

    private void scaleRecipeButton() {
        flowBottomRow.getChildren().get(1).setOnMouseClicked(e -> {
            if (activeRecipeHistory != null) {
                ScaleByStage scale = new ScaleByStage();
                scale.display(activeRecipeHistory);
                addItemsListView();
                recipeListView.refresh();
                instructionsTextArea.setText(activeRecipeHistory.getActiveCommit().getRecipeVersion().toString());
            }

        });
    }

    private void logAttemptButton() {
        flowBottomRow.getChildren().get(0).setOnMouseClicked(e -> {
            if (activeRecipeHistory != null) {
                activeRecipeHistory.attempt(clock);
                infoLabel.setText(String.format("Attempted count: %1$d :: Modified count %2$d",
                        activeRecipeHistory.totalAttempts(), activeRecipeHistory.getCommits().size() - 1));
            }
        });
    }

    private void saveAsImageButton(Stage primaryStage) {
        flowTopRow.getChildren().get(2).setOnMouseClicked(e -> {
            String message = activeCollection.toJson();
            File fileIn = new File("data/icons/sharing/collectionsharingbynikitagolubev.png");
            Steganos encoder = new Steganos();
            encoder.encode(message, fileIn);
            File fileOut = fileChooserHelper("./data/icons/sharing/exported",
                    "png",
                    "save", primaryStage);
            if (fileOut != null) {
                try {
                    encoder.save(fileOut);
                } catch (IOException ex) {
                    AlertMessage.display("Error while saving.", "IOException");
                }
            }
        });
    }

    private void newRecipeButton() {
        flowTopRow.getChildren().get(1).setOnMouseClicked(e -> {
            RecipeStage stage = new RecipeStage();
            stage.display(activeCollection, activeRecipeHistory, true);
            addItemsListView();
            recipeListView.refresh();
        });
    }

    private void loadJsonButton(Stage primaryStage) {
        flowTopRow.getChildren().get(0).setOnMouseClicked(e -> {
            File fileIn = fileChooserHelper("./data/recipecollections",
                    "json",
                    "load", primaryStage);
            if (fileIn != null) {
                openFile(fileIn);
            }
        });
    }

    private void recipeListViewRightClickMenu() {
        recipeListView.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>();
            ContextMenu contextMenu = new ContextMenu();
            cell.textProperty().bindBidirectional(cell.itemProperty());
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    buildContextMenu(cell, contextMenu);
                }
            });
            return cell;
        });
    }

    private void buildContextMenu(ListCell<String> cell, ContextMenu contextMenu) {
        List<String> branches = activeCollection.get(cell.getItem()).getBranches();
        Menu switchBranch = new Menu();
        MenuItem removeRecipe = new MenuItem();
        removeRecipe.textProperty().bind(Bindings.format("remove", cell.itemProperty()));
        removeRecipe.setOnAction(e -> {
            activeCollection.remove(cell.getItem());
            addItemsListView();
            recipeListView.refresh();
        });
        buildBranchList(cell, branches, switchBranch);
        
        switchBranch.textProperty().bind(Bindings.format("branches", cell.itemProperty()));
        contextMenu.getItems().clear();
        contextMenu.getItems().add(switchBranch);
        contextMenu.getItems().add(removeRecipe);

        cell.textProperty().bindBidirectional(cell.itemProperty());
        cell.setContextMenu(contextMenu);
    }

    private void buildBranchList(ListCell<String> cell, List<String> branches, Menu switchBranch) {
        for (String s : branches) {
            MenuItem child = new MenuItem(s);
            switchBranch.getItems().add(child);
            child.textProperty().bind(Bindings.format(s, cell.itemProperty()));
            child.setOnAction(e -> {
                activeCollection.get(cell.getItem()).checkout(s);
                activeRecipeHistory = activeCollection.get(cell.getItem());
                updateTextArea();
            });
        }
    }

    private void recipeListViewOnDragDrop() {
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
                AlertMessage.display("Problem loading the collection.", "IOException");
            }
        });
    }

    private void recipeListViewSetup() {
        recipeListViewDefaultIcon();

        // Accept and load PNG files of recipe collections.
        recipeListViewOnDragDrop();

        // Right-click context menu to switch branches or export recipes
        // https://stackoverflow.com/questions/28264907/javafx-listview-contextmenu
        //TODO: Complete this implementation
        recipeListViewRightClickMenu();
    }

    private void recipeListViewDefaultIcon() {
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
    }

    private void gridPaneLayoutSetup(Stage primaryStage) {
//        gridPane.setGridLinesVisible(true);
        gridPane.setPadding(new Insets(10, 20, 20, 20));
        gridPane.add(flowTopRow, 0, 0, 5, 2);
        gridPane.add(flowBottomRow, 0, 12, 5, 2);
        gridPane.add(darkModeToggle, 14, 12, 1, 1);
        gridPane.add(recipeListView, 0, 2, 4, 10);
        gridPane.add(instructionsTextArea, 4, 2, 10, 10);
        gridPane.add(infoLabel, 5, 12, 10, 1);
        gridPane.setHgap(20);
        gridPane.setVgap(10);
        scene = new Scene(gridPane, WIDTH, HEIGHT);
        scene.getStylesheets().add("./ui/gitbreadlightstyle.css");
        primaryStage.setScene(scene);
        // Image courtesy of Freepik on https://www.flaticon.com/free-icon/agronomy_1188035
        primaryStage.getIcons().add(new Image("file:./data/icons/wheatcolor512.png"));
        primaryStage.show();
    }

    private void initialize(Stage primaryStage) {
        activeCollection = new RecipeDevCollection();
        activeRecipeHistory = null;
        primaryStage.setTitle("GitBread");
        gridPane = new GridPane();
        fieldAndButtons();
        flowTopRow = makeFlowPaneButtons(topRecipeBarIcons);
        flowBottomRow = makeFlowPaneButtons(bottomRecipeBarIcons);
        recipeListView = new ListView<String>();
        instructionsTextArea = new TextArea();
        instructionsTextArea.setWrapText(true);
        instructionsTextArea.setEditable(false);
        infoLabel = new Label();
        items = FXCollections.observableArrayList();
        primaryStage.sizeToScene();
        primaryStage.setMinHeight(HEIGHT);
        primaryStage.setMinWidth(WIDTH);
        primaryStage.setMaxHeight(HEIGHT);
        primaryStage.setMaxWidth(WIDTH);
    }


    private void updateTextArea() {
        instructionsTextArea.setText(activeRecipeHistory
                .getActiveCommit()
                .getRecipeVersion()
                .toString());
    }

    private FlowPane makeFlowPaneButtons(String[] urls) {
        int numImages = urls.length;
        FlowPane flow = new FlowPane();
        flow.setPadding(new Insets(5, 0, 5, 0));
        flow.setVgap(2);
        flow.setHgap(4);
        Button[] pages = new Button[numImages];
        for (int i = 0; i < numImages; i++) {
            pages[i] = new Button();
            pages[i].setGraphic(new ImageView(new Image(urls[i])));
        }
        for (int i = 0; i < numImages; i++) {
            flow.getChildren().add(pages[i]);
        }
        return flow;

    }

    private void setTooltips() {
        darkModeToggle.setTooltip(new Tooltip("Toggle DarkMode"));
        Tooltip.install(flowTopRow.getChildren().get(0), new Tooltip("Load recipe book"));
        Tooltip.install(flowTopRow.getChildren().get(1), new Tooltip("Add recipe"));
        Tooltip.install(flowTopRow.getChildren().get(2), new Tooltip("Export recipe book as image"));
        Tooltip.install(flowTopRow.getChildren().get(3), new Tooltip("Export recipe"));
        Tooltip.install(flowBottomRow.getChildren().get(0), new Tooltip("Log attempt"));
        Tooltip.install(flowBottomRow.getChildren().get(1), new Tooltip("Scale"));
        Tooltip.install(flowBottomRow.getChildren().get(2), new Tooltip("New branch"));
        Tooltip.install(flowBottomRow.getChildren().get(3), new Tooltip("Update recipe"));
    }

    private void openFile(File file) {
        try {
            activeCollection = loadRecipeCollectionFile(file);
            addItemsListView();
        } catch (IOException e) {
            AlertMessage.display("Error loading the file.", "IOException");
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
    }

    //EFFECTS: Opens a file chooser window for loading or saving. Returns null if
    private File fileChooserHelper(String initialDirectory, String fileFilter, String dialogOption, Stage stage) {
        File file = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(initialDirectory));
        if (fileFilter.toLowerCase().equals("json")) {
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("JSON files (*.json)",
                    "*.json");
            fileChooser.getExtensionFilters().add(extensionFilter);
        } else if (fileFilter.toLowerCase().equals("png")) {
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("PNG files (*.png)",
                    "*.png");
            fileChooser.getExtensionFilters().add(extensionFilter);
        }
        if (dialogOption.toLowerCase().equals("save")) {
            file = fileChooser.showSaveDialog(stage);
        } else if (dialogOption.toLowerCase().equals("load")) {
            file = fileChooser.showOpenDialog(stage);
        }
        return file;
    }
}
