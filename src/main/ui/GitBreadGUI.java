package ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.javafx.css.StyleManager;
import exceptions.BranchAlreadyExistsException;
import exceptions.BranchDoesNotExistException;
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
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;
import persistence.Writer;
import persistence.steganography.Steganos;

import javax.naming.SizeLimitExceededException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Clock;
import java.util.*;

import static persistence.Reader.*;


// following thenewboston tutorial as reference
// https://www.youtube.com/watch?v=FLkOX4Eez6o&list=PL6gx4Cwl9DGBzfXLWLSYVy8EbTdpGbUIG
public class GitBreadGUI extends Application {
    private Clock clock = Clock.systemDefaultZone();
    private static final String[] topRecipeBarIcons = new String[]{"file:./data/icons/sharing/recipecollection32.png",
            "file:./data/icons/sharing/addrecipe32.png",
            "file:./data/icons/sharing/exportrecipecollectionshare32.png",
            "file:./data/icons/sharing/exportrecipe32.png",
            "file:./data/icons/buttons/savebysmashicons.png"};
    private static final String[] bottomRecipeBarIcons = new String[]{
            "file:./data/icons/buttons/mixingbyfreepik.png",
            "file:./data/icons/buttons/scalebysmashicons.png",
            "file:./data/icons/buttons/branchbysmashicons.png",
            "file:./data/icons/buttons/mergebysmashicons.png",
            "file:./data/icons/buttons/agronomy.png"};
    private static final Image TICK_MARK = new Image("file:./data/icons/buttons/tickbythoseicons.png");
    private static final String DARK_CSS = "./ui/gitbreaddarkstyle.css";
    private static final String LIGHT_CSS = "./ui/gitbreadlightstyle.css";
    ToggleButton darkModeToggle;

    RecipeDevCollection activeCollection;
    NodeGraph activeRecipeHistory;
    ListView<String> recipeListView;
    ObservableList<String> items;
    TabPane infoDisplay;
    TilePane tilePane;
    ScrollPane scrollPane;
    Tab instructions;
    Tab attempts;
    Tab images;
    GridPane instructionsGridPane;
    TextArea attemptsTextArea;
    Label infoLabel;

    FlowPane flowTopRow;
    FlowPane flowBottomRow;
    GridPane gridPane;
    Scene scene;

    private static final int WIDTH = 800;
    public static final int HEIGHT = 750;
    boolean darkMode = false;

    public static void main(String[] args) {
        launch(args);
    }

    // start contains the main JavaFX code for running and handling the application
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setOnCloseRequest(e -> {
            e.consume(); // tells Java that I will take care of this request
            confirmExit(primaryStage);
        });
        initialize(primaryStage);
        setTooltips();
        gridPaneLayoutSetup(primaryStage);
        recipeListViewSetup();
        setUpButtonActions(primaryStage);
    }

    private void confirmExit(Stage primaryStage) {
        boolean confirmExit = ConfirmStage.display("Are you sure you want to exit GitBread?",
                "Confirm exit");
        if (confirmExit && activeCollection.isEmpty()) {
            primaryStage.close();
        } else if (!activeCollection.isEmpty() && confirmExit) {
            if (ConfirmStage.display("Would you like to save before exiting?", "Confirm")) {
                saveAsJson(primaryStage);
            }
            primaryStage.close();
        }
    }

    // THE LOOK
    //MODIFIES: this
    //EFFECTS: initialize the main stages and other UI elements
    private void initialize(Stage primaryStage) {
        activeCollection = new RecipeDevCollection();
        activeRecipeHistory = null;
        primaryStage.setTitle("GitBread");
        gridPane = new GridPane();
        fieldAndButtons();
        flowTopRow = makeFlowPaneButtons(topRecipeBarIcons);
        flowBottomRow = makeFlowPaneButtons(bottomRecipeBarIcons);
        recipeListView = new ListView<>();
        tabSetup();
        infoLabel = new Label();
        items = FXCollections.observableArrayList();
        primaryStage.sizeToScene();
        primaryStage.setMinHeight(HEIGHT);
        primaryStage.setMinWidth(WIDTH);
        primaryStage.setMaxHeight(HEIGHT);
        primaryStage.setMaxWidth(WIDTH);
        Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
        StyleManager.getInstance().addUserAgentStylesheet(LIGHT_CSS);
    }

    //MODIFIES: this
    //EFFECTS: set tooltips for all the UI elements
    private void setTooltips() {
        darkModeToggle.setTooltip(new Tooltip("Toggle DarkMode"));
        Tooltip.install(flowTopRow.getChildren().get(0), new Tooltip("Load recipe book"));
        Tooltip.install(flowTopRow.getChildren().get(1), new Tooltip("Add recipe"));
        Tooltip.install(flowTopRow.getChildren().get(2), new Tooltip("Export recipe book as image"));
        Tooltip.install(flowTopRow.getChildren().get(3), new Tooltip("Export recipe as image"));
        Tooltip.install(flowTopRow.getChildren().get(4), new Tooltip("Save"));
        Tooltip.install(flowBottomRow.getChildren().get(0), new Tooltip("Log attempt"));
        Tooltip.install(flowBottomRow.getChildren().get(1), new Tooltip("Scale"));
        Tooltip.install(flowBottomRow.getChildren().get(2), new Tooltip("New branch"));
        Tooltip.install(flowBottomRow.getChildren().get(3), new Tooltip("Merge"));
        Tooltip.install(flowBottomRow.getChildren().get(4), new Tooltip("Update recipe"));
    }

    //MODIFIES: this
    //EFFECTS: place the gridPane elements
    private void gridPaneLayoutSetup(Stage primaryStage) {
//        gridPane.setGridLinesVisible(true);
        gridPane.setPadding(new Insets(10, 20, 20, 20));
        gridPane.add(flowTopRow, 0, 0, 5, 2);
        gridPane.add(flowBottomRow, 0, 57, 5, 2);
        gridPane.add(darkModeToggle, 14, 0, 1, 1);
        gridPane.add(recipeListView, 0, 2, 4, 55);
        gridPane.add(infoDisplay, 4, 2, 10, 55);
        gridPane.add(infoLabel, 5, 57, 10, 1);
        gridPane.setHgap(20);
        gridPane.setVgap(10);
        scene = new Scene(gridPane, WIDTH, HEIGHT);
//        scene.getStylesheets().add("./ui/gitbreadlightstyle.css");
        primaryStage.setScene(scene);
        // Image courtesy of Freepik on https://www.flaticon.com/free-icon/agronomy_1188035
        primaryStage.getIcons().add(new Image("file:./data/icons/wheatcolor512.png"));
        primaryStage.show();
    }

    //THE LOGIC
    private void recipeListViewSetup() {
        recipeListViewDefaultIcon();

        // Accept and load PNG files of recipe collections.
        recipeListViewOnDragDrop();

        // Right-click context menu to switch branches or export recipes
        // https://stackoverflow.com/questions/28264907/javafx-listview-contextmenu
        recipeListViewRightClickMenu();
    }

    //MODIFIES: this
    //EFFECTS: set the background image of the default recipe collection / recipe list area
    private void recipeListViewDefaultIcon() {
        ImageView recipeListPlaceHolder = new ImageView(
                new Image("file:./data/icons/sharing/recipecollectionsplitload.png"));
        recipeListPlaceHolder.setOpacity(0.5);
        recipeListView.setPlaceholder(recipeListPlaceHolder);
        recipeListView.setMinWidth(204);
        recipeListView.setMaxWidth(204);
        recipeListView.setOnDragOver(event -> {
                    if (event.getDragboard().hasFiles() || event.getDragboard().hasImage()) {
                        event.acceptTransferModes(TransferMode.ANY);
                    }
                    event.consume();
                }
        );
    }

    //EFFECTS: Loads
    //https://stackoverflow.com/questions/61068970/why-does-the-same-image-edit-png-produce-two-slightly-different-byte-arrays-i/61149562#61149562
    private void recipeListViewOnDragDrop() {
        recipeListView.setOnDragDropped(event -> {
            Steganos encoder = new Steganos();
            if (event.getDragboard().hasUrl()) {
                try {
                    URL path = new URL(event.getDragboard().getUrl());
                    String decoded = encoder.decode(path);
                    loadToStage(encoder, decoded);
                } catch (IOException e) {
                    AlertStage.display("Problem loading the collection.", "IOException");
                } catch (BranchDoesNotExistException e) {
                    AlertStage.display("Branch does not exist.", "BranchDoesNotExistException");
                } finally {
                    addItemsListView();
                }
            }
        });
    }

    //MODIFIES: this
    //EFFECTS: logic for loading a recipe collection vs. a recipe
    private void loadToStage(Steganos encoder, String json) throws IOException, BranchDoesNotExistException {
        if (encoder.isDecodedCollection()) {
            activeCollection = loadRecipeCollectionJson(json);
        } else {
            String title = TextInputStage.display("Load Recipe", "Enter the recipe name", "Recipe title");
            activeCollection.add(title, loadRecipeDevHistoryJson(json));
        }
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
        Set<String> branches = activeCollection.get(cell.getItem()).getBranches();
        Menu switchBranch = new Menu();
        MenuItem removeRecipe = new MenuItem();
        removeRecipe.textProperty().bind(Bindings.format("remove", cell.itemProperty()));
        removeRecipe.setOnAction(e -> {
            boolean toRemove = ConfirmStage.display("Remove this recipe history?", "Remove Recipe Confirmation");
            if (toRemove) {
                activeCollection.remove(cell.getItem());
                addItemsListView();
                recipeListView.refresh();
            }
        });
        buildBranchList(cell, branches, switchBranch);

        switchBranch.textProperty().bind(Bindings.format("branches", cell.itemProperty()));
        contextMenu.getItems().clear();
        contextMenu.getItems().add(switchBranch);
        contextMenu.getItems().add(removeRecipe);

        cell.textProperty().bindBidirectional(cell.itemProperty());
        cell.setContextMenu(contextMenu);
    }

    private void buildBranchList(ListCell<String> cell, Set<String> branches, Menu switchBranch) {
        ToggleGroup branchToggle = new ToggleGroup();
        for (String s : branches) {
            RadioMenuItem child = new RadioMenuItem(s);
            if (activeRecipeHistory != null && s.equals(activeRecipeHistory.getCurrentBranch())) {
                child.setSelected(true);
            }
//            } else if (s.equals("master")) {
//                child.setSelected(true);
//            }
            child.setToggleGroup(branchToggle);
            switchBranch.getItems().add(child);
            child.textProperty().bind(Bindings.format(s, cell.itemProperty()));
            child.setOnAction(e -> {
                try {
                    activeCollection.get(cell.getItem()).checkout(s);
                } catch (BranchDoesNotExistException ex) {
                    AlertStage.display("Branch " + s + " does not exist.", "BranchDoesNotExistException");
                }
                activeRecipeHistory = activeCollection.get(cell.getItem());
                updateTextArea();
            });
        }
    }

    private void setUpButtonActions(Stage primaryStage) {
        // Open a file browser to load a JSON text file of a recipe collection
        flowTopRow.getChildren().get(0).setOnMouseClicked(e -> loadJson(primaryStage));

        // Open the add recipe stage
        flowTopRow.getChildren().get(1).setOnMouseClicked(e -> addNewRecipe());

        // save the recipe collection as a sharable PNG.
        flowTopRow.getChildren().get(2).setOnMouseClicked(e -> saveCollectionAsImage(primaryStage));

        // save a single recipe as a sharable PNG
        flowTopRow.getChildren().get(3).setOnMouseClicked(e -> saveRecipeAsImage(primaryStage));

        // save the recipe collection as a JSON file
        flowTopRow.getChildren().get(4).setOnMouseClicked(e -> saveAsJson(primaryStage));

        // log an attempt with the active recipe version
        flowBottomRow.getChildren().get(0).setOnMouseClicked(e -> logAttempt(primaryStage));

        // scale and re-display the active recipe in the instructions TextArea
        flowBottomRow.getChildren().get(1).setOnMouseClicked(e -> scaleRecipe());

        // make a new branch
        flowBottomRow.getChildren().get(2).setOnMouseClicked(e -> doNewBranch());

        // merge a branch with current branch
        flowBottomRow.getChildren().get(3).setOnMouseClicked(e -> doMergeBranch());

        //make a new commit to the current recipe and branch
        flowBottomRow.getChildren().get(4).setOnMouseClicked(e -> newCommitToCurrentBranch());

        darkModeToggle.setOnMouseClicked(e -> doDarkModeToggle());

        recipeListViewListener();
    }

    private void loadJson(Stage primaryStage) {
        File fileIn = fileChooserHelper("./data/recipecollections",
                "json",
                "load", primaryStage);
        if (fileIn != null) {
            openFile(fileIn);
        }
    }

    private void addNewRecipe() {
        RecipeStage stage = new RecipeStage();
        stage.display(activeCollection, activeRecipeHistory, true);
        addItemsListView();
        recipeListView.refresh();
        //TODO: fix this to select from the listview the item just modified or added
    }

    private void saveCollectionAsImage(Stage primaryStage) {
        try {
            String message = activeCollection.toJson();
            File fileIn = new File("data/icons/sharing/collectionsharingbynikitagolubev.png");
            Steganos encoder = new Steganos();
            encoder.encode(message, fileIn, true);
            File fileOut = fileChooserHelper("./data/icons/sharing/exported",
                    "png",
                    "save", primaryStage);
            if (fileOut != null) {
                encoder.save(fileOut);
            }

        } catch (IOException | SizeLimitExceededException ex) {
            AlertStage.display("Error while saving.", "IOException");
        }
    }

    private void saveRecipeAsImage(Stage primaryStage) {
        if (activeRecipeHistory != null) {
            try {
                String message = activeRecipeHistory.toJson();
                File fileIn = fileChooserHelper("./data/images/attemptphotos", "png", "load", primaryStage);
                if (fileIn == null) {
                    return;
                }
                File fileOut = fileChooserHelper("./data/icons/sharing/exported", "png", "save", primaryStage);
                Steganos encoder = new Steganos();
                encoder.encode(message, fileIn, false);
                if (fileOut != null) {
                    encoder.save(fileOut);
                }
            } catch (JsonProcessingException ex) {
                AlertStage.display("Error converting to JSON.", "JsonProcessingException");
            } catch (IOException ex) {
                AlertStage.display("Error saving image.", "IOException");
            } catch (SizeLimitExceededException e) {
                AlertStage.display("Size of message is too large", "SizeLimitExceededException");
            }
        }
    }

    private void saveAsJson(Stage primaryStage) {
        if (!activeCollection.isEmpty()) {
            try {
                File file = fileChooserHelper(
                        "./data/recipecollections",
                        "json",
                        "save",
                        primaryStage);
                Writer writer = new Writer(file);
                writer.write(activeCollection);
                writer.close();
            } catch (IOException ex) {
                // TODO: add custom exception to differentiate between an error saving and a null input file
                AlertStage.display("Error saving file.", "IOException");
            }
        } else {
            AlertStage.display("No recipes to save!", "Empty Collection");
        }
    }

    private void logAttempt(Stage primaryStage) {
        if (activeRecipeHistory != null) {
            activeRecipeHistory.attempt(clock);
            int size = activeRecipeHistory.getActiveNode().getRecipeVersion().getAttemptHistory().size();
            Attempt attempt = activeRecipeHistory.getActiveNode().getRecipeVersion()
                    .getAttemptHistory().get(size - 1);
            logAttemptNotes(primaryStage, attempt);
            updateAttemptModifiedLabel();
            updateTextArea();
        }
    }

    private void logAttemptNotes(Stage primaryStage, Attempt attempt) {
        AttemptNotesStage notes = new AttemptNotesStage();
        notes.display(primaryStage, attempt);
    }

    private void scaleRecipe() {
        if (activeRecipeHistory != null) {
            ScaleByStage scale = new ScaleByStage();
            scale.display(activeRecipeHistory);
            addItemsListView();
            recipeListView.refresh();
            updateTextArea();
        }

    }

    private void doNewBranch() {
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
            branchNameOnKeyPressed(stage, branchName);
            stage.showAndWait();
        }
    }

    private void branchNameOnKeyPressed(Stage stage, TextField branchName) {
        branchName.setOnKeyPressed(event -> {
            try {
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
            } catch (BranchAlreadyExistsException e) {
                AlertStage.display("A branch with that name already exists.", "BranchAlreadyExistsException");
            }
        });
    }

    private void doMergeBranch() {
        if (activeRecipeHistory != null) {
            MergeStage mergeStage = new MergeStage();
            mergeStage.display(activeRecipeHistory);
            updateTextArea();
        }
    }

    private void newCommitToCurrentBranch() {
        if (activeRecipeHistory != null) {
            RecipeStage recipeStage = new RecipeStage();
            recipeStage.display(activeCollection, activeRecipeHistory, false);
            addItemsListView();
            recipeListView.refresh();
        }
    }

    //https://stackoverflow.com/questions/46559981/javafx-set-default-css-stylesheet-for-the-whole-application
    //EFFECTS: modifies the global style sheet for the javaFX instance.
    private void doDarkModeToggle() {
        if (!darkMode) {
            StyleManager.getInstance().addUserAgentStylesheet(DARK_CSS);
            darkMode = true;
        } else {
            StyleManager.getInstance().removeUserAgentStylesheet(DARK_CSS);
            StyleManager.getInstance().addUserAgentStylesheet(LIGHT_CSS);
            darkMode = false;
        }
    }

    private void recipeListViewListener() {
        recipeListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                activeRecipeHistory = activeCollection.get(newValue);
                updateAttemptModifiedLabel();
                updateTextArea();
            }
        });
    }

    private void updateAttemptModifiedLabel() {
        infoLabel.setText(String.format("Attempts: %1$d :: Modifications: %2$d",
                activeRecipeHistory.totalAttempts(), activeRecipeHistory.size() - 1));
    }

    //https://stackoverflow.com/questions/4917326/how-to-iterate-over-the-files-of-a-certain-directory-in-java/4917347
    private void tabSetup() {
        attemptsTextArea = new TextArea();
        attemptsTextArea.setWrapText(true);
        attemptsTextArea.setEditable(false);
        infoDisplay = new TabPane();
        infoDisplay.setMaxHeight(HEIGHT * 0.7);
        instructions = new Tab("Instructions");
        instructions.setClosable(false);
        instructionsGridPane = new GridPane();
        instructionsGridPane.setPadding(new Insets(10));
        instructionsGridPane.setVgap(20);
        instructionsGridPane.setHgap(10);
        ScrollPane scrollPaneInstructions = new ScrollPane();
        scrollPaneInstructions.setFitToWidth(true);
        scrollPaneInstructions.setContent(instructionsGridPane);
        instructions.setContent(scrollPaneInstructions);
        attempts = new Tab("Attempt Record");
        attempts.setClosable(false);
        attempts.setContent(attemptsTextArea);
        tileScrollPaneSetUp();
        images = new Tab("Attempt Lookbook");
        images.setClosable(false);
        images.setContent(scrollPane);
        infoDisplay.getTabs().addAll(instructions, attempts, images);

    }

    private void tileScrollPaneSetUp() {
        tilePane = new TilePane();
        tilePane.setHgap(10);
        tilePane.setVgap(10);
        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tilePane);
    }

    //MODIFIES: this
    //EFFECTS: update the attempts look book tab with images
    private void updateAttemptLookBook(NodeGraph activeRecipeHistory) {
        tilePane.getChildren().clear();
        final int IMAGE_VIEW_SIZE = 150;
        List<Attempt> attempts = activeRecipeHistory.getAttempts();
        for (Attempt attempt : attempts) {
            if (attempt.hasPhoto()) {
                ImageView imageView = new ImageView(new Image("file:" + attempt.getPhotoPath()));
                imageView.setFitWidth(IMAGE_VIEW_SIZE);
                imageView.setFitHeight(IMAGE_VIEW_SIZE);
                tilePane.getChildren().add(imageView);
                Node nodeOfAttempt = activeRecipeHistory.getNodeOfAttempt(attempt);
                imageView.setId(nodeOfAttempt.getSha1());
                Tooltip.install(imageView,
                        new Tooltip(nodeOfAttempt.getBranchLabel() + " " + imageView.getId().substring(0, 7)));
                imageView.setOnMouseEntered(e -> imageView.setOpacity(0.7));
                imageView.setOnMouseExited(e -> imageView.setOpacity(1.0));
                imageView.setOnMouseClicked(e -> {
                    //stub
                    // will search for the node based on the imageView ID and pull up that recipe version
                });
            }
        }
    }

    //EFFECTS: prints a history of the attempts of the CURRENT ACTIVE COMMIT along with any notes.
    private void updateTextArea() {
        updateInstructions();
        updateAttemptsTextArea();
        updateAttemptLookBook(activeRecipeHistory);
    }


    //MODIFIES: this
    //EFFECTS: creates
    private void updateInstructions() {
        instructionsGridPane.getChildren().clear();
        boolean hasInstructions = !this.activeRecipeHistory
                .getActiveNode().getRecipeVersion().getInstructions().isEmpty();
        List<String> displayString = this.activeRecipeHistory.getActiveNode().getRecipeVersion().splitInstructions();
        if (this.activeRecipeHistory != null && hasInstructions) {

            int sepIndex = displayString.indexOf("INSTRUCTIONS_SEPARATOR");
            for (int i = 0; i < sepIndex; i++) {
                instructionsGridPane.addRow(i);
                Label label = new Label();
                label.setText(displayString.get(i));
                instructionsGridPane.add(label, 1, i);
            }
            for (int i = sepIndex + 1; i < displayString.size(); i++) {
                instructionsGridPane.addRow(i);
                TextFlow textFlow = new TextFlow();
                ImageView toggleCheck = new ImageView(new Image("file:./data/icons/buttons/numbertoggles/"
                        + (i - sepIndex) + ".png"));
                setToggleCheckListeners(toggleCheck, textFlow, i);
                instructionsGridPane.add(toggleCheck, 0, i);
                textFlow.getChildren().add(new Text(displayString.get(i)));
                instructionsGridPane.add(textFlow, 1, i);
            }
        }
    }

    private void updateAttemptsTextArea() {
        StringBuilder attemptsString = new StringBuilder();
        List<Attempt> attempts = activeRecipeHistory.getAttempts();
        for (Attempt attempt : attempts) {
            attemptsString.append(attempt.print());
        }
        attemptsTextArea.clear();
        attemptsTextArea.setText(attemptsString.toString());
    }

    private void setToggleCheckListeners(ImageView toggleCheck, TextFlow step, int i) {
        toggleCheck.setPickOnBounds(true);
        int loc = i - 2;
        toggleCheck.setOnMouseClicked(e -> {
            if (!toggleCheck.getImage().equals(TICK_MARK)) {
                toggleCheck.setImage(TICK_MARK);
                step.setOpacity(0.4);
            } else {
                toggleCheck.setImage(new Image("file:./data/icons/buttons/numbertoggles/" + loc + ".png"));
                step.setOpacity(1.0);
            }
        });
        toggleCheck.setOnMouseEntered(e -> toggleCheck.setOpacity(0.5));
        toggleCheck.setOnMouseExited(e -> toggleCheck.setOpacity(1.0));
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

    private void openFile(File file) {
        try {
            activeCollection = loadRecipeCollectionFile(file);
            addItemsListView();
        } catch (IOException e) {
            AlertStage.display("Error loading the file.", "IOException");
        } catch (BranchDoesNotExistException e) {
            e.printStackTrace();
        }
    }

    private void addItemsListView() {
        recipeListView.getItems().clear();
        for (Map.Entry<String, NodeGraph> entry : activeCollection.getCollection().entrySet()) {
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
