package ui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Stack;

// following thenewboston tutorial as reference
// https://www.youtube.com/watch?v=FLkOX4Eez6o&list=PL6gx4Cwl9DGBzfXLWLSYVy8EbTdpGbUIG
public class GitBreadGUI extends Application {
    Button button1;
    Button button2;
    private static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public static void main(String[] args) {
        launch(args);
    }

    // start contains the main JavaFX code for running and handling the application
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("GitBread v0.0");
        Label label = new Label("Holla at ya boy!");
        button1 = new Button();
        button2 = new Button();
        button1.setText("Click me");
        button2.setText("No! Click me!");
        button1.setTranslateX(50);
        button2.setTranslateX(-50);

        // lambda implementation of the event handler. This works because there is only a single method in the
        // EventHandler interface.
        button1.setOnAction(e -> System.out.println("lambda event handler."));
        button2.setOnAction(e -> System.out.println("Different button dawg."));

        StackPane layout = new StackPane();
        layout.getChildren().add(button1);
        layout.getChildren().add(button2);
        Scene scene = new Scene(layout, WIDTH, HEIGHT);
//        scene.getStylesheets().add("./ui/gitbreadstyle.css");
        primaryStage.setScene(scene);
        primaryStage.show();


    }

//    @Override
//    public void handle(ActionEvent event) {
//        if(event.getSource()==button) { // the source can be different buttons
//            System.out.println("Thanks for clicking!");
//        }
//    }
}
