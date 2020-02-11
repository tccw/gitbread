package ui;

import model.Recipe;
import model.RecipeCollection;

import java.util.Scanner;

public class GitBreadApp {

    private Scanner input;
    RecipeCollection collection = new RecipeCollection();

    public GitBreadApp() {
        runGitBread();
    }

    private void runGitBread() {
        boolean keepGoing = true;
        String command = null;
        input = new Scanner(System.in);

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("quit")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
    }

    private void displayMenu() {
        System.out.println("\nSelect from: ");
        System.out.println("add                  --> add new recipe to collection");
        System.out.println("select (recipe name) --> select a recipe");
        System.out.println("attempt              --> attempt selected recipe");
        System.out.println("view                 --> list the available recipes ");
        System.out.println("read                 --> add new recipe to collection");
        System.out.println("quit                 --> exit GitBread");

    }

    private void processCommand(String command) {
        if (command.equals("add")) {
            Scanner input = new Scanner(System.in);
            System.out.println("Enter recipe name: ");
            String title = input.next();

            collection.add(title, );
        } else if (command.equals("select")) {
            //stub
        } else if (command.equals("attempt")) {
            //stub
        } else if (command.equals("view")) {
            //stub
        } else if (command.equals("read")) {
            //stub
        }
    }
}
