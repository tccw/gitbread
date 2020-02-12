package ui;

import com.sun.javafx.image.IntPixelGetter;
import model.BreadRecipe;
import model.RecipeCollection;
import model.RecipeHistory;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

        System.out.println("GitBread v0.0 (type 'bread help' for available commands.)");
        while (keepGoing) {
            System.out.print("> ");
            command = input.nextLine();
            command = command.toLowerCase();

            if (command.equals("bread quit")) {
                keepGoing = false;
            } else if (command.equals("bread help")) {
                displayHelp();
            } else {
                processCommand(command);
            }
        }
    }

    private void displayHelp() {
        System.out.println("\nAvailable commands:\n");
        System.out.println("Command style is 'bread <command> <flags>'");
        System.out.println("<command> list");
        System.out.println("    bread arc : [-n name] [-dw dough weight] || [-fw flour weight] [-h hydration]"
                + " [-si set instructions]");
        System.out.println("        usage example: bread arc -n French loaf -dw 1000");
        System.out.println("        usage example: bread arc -n Pizza -fw 350 -h 0.7");
        System.out.println("        usage example: bread arc -n French loaf -fw 350 -h 0.7 -si 1. Mix ingredients "
                + "2. Rise for 2-24 hours 3. Stretch dough into circle 4. Top and bake for 6 minutes\n");
        System.out.println("    bread list : [-v verbose]");
        System.out.println("        usage example: bread list");
        System.out.println("        usage example: bread list -v\n");
        System.out.println("    bread select : [-n name]");
        System.out.println("        usage example: bread select -n Pizza\n");
    }

    //EFFECTS: processes the commands the user inputs.
    private void processCommand(String command) {
        String localCmd;
        if (command.contains("-")) {
            localCmd = command.substring(0, command.indexOf("-")).trim();
        } else {
            localCmd = command.trim();
        }

        if (localCmd.equals("bread arc")) {
            breadArcCommand(command);
        } else if (localCmd.equals("bread list")) {
            breadListCommand(command);
        } else if (localCmd.equals("attempt")) {
            //stub
        } else if (localCmd.equals("view")) {
            //stub
        } else if (localCmd.equals("read")) {
            //stub
        } else {
            System.err.println("Not a valid command.");
        }
    }

    private void breadListCommand(String c) {
        if (c.contains("-v")) {
            System.out.println("------- Recipe Collection -------");
            System.out.println(collection.toString());
        } else {
            System.out.println("------- Recipe Collection -------");
            System.out.println(collection.toString());
        }

    }

    //MODIFIES: collection
    //EFFECTS: helper for processCommand(), adds a new recipe history to the collection
    private void breadArcCommand(String command) {
        String title = parseTitle(command);
        if (command.contains("-dw")) {
            int doughWeight = Integer
                    .parseInt(command.substring(command.indexOf("-dw"))
                            .replaceAll("\\D+", ""));
            collection.add(title, new RecipeHistory(new BreadRecipe(doughWeight)));
        } else if (command.contains("-fw")) {
            parseFlourAndHydration(command, title);
        }
    }

    //MODIFIES: collection
    //EFFECTS: helper for breadArcCommand(), pulls the flour weight and hydration from a command
    private void parseFlourAndHydration(String c, String title) {
        int fwIndex = c.indexOf("-fw");
        int hydrationIndex = c.indexOf("-h");
        double hydration = Double.parseDouble(c.substring(hydrationIndex + 2, hydrationIndex + 6));
        int flourWeight;
        if (fwIndex > hydrationIndex) {
            flourWeight = Integer.parseInt(c.substring(fwIndex)
                    .replaceAll("\\D+", "").trim());
        } else {
            flourWeight = Integer.parseInt(c.substring(fwIndex, hydrationIndex)
                    .replaceAll("\\D+", "").trim());
            //this should account for -n being between -h and -fw since it removes all non numeric strings.
        }
        collection.add(title, new RecipeHistory(new BreadRecipe(flourWeight, hydration)));
    }

    //EFFECTS: helper for breadArcCommand(), pulls the title/name from a 'bread arc' command.
    private String parseTitle(String c) {
        String result = "";
        if (c.contains("-dw")) {
            int dwIndex = c.indexOf("-dw");
            int nameIndex = c.indexOf("-n");

            if (dwIndex > nameIndex) {
                result = c.substring(nameIndex + 2, dwIndex).trim();
            } else {
                result = c.substring(nameIndex + 2).trim();
            }
        } else if (c.contains("-fw")) {
            int fwIndex = c.indexOf("-fw");
            int hydrationIndex = c.indexOf("-h");
            int nameIndex = c.indexOf("-n");

            if (nameIndex > Math.max(hydrationIndex, fwIndex)) {
                result = c.substring(nameIndex + 2).trim();
            } else if ((nameIndex < fwIndex) && (fwIndex < hydrationIndex)) {
                result = c.substring(nameIndex + 2, hydrationIndex).trim();
            } else if ((nameIndex < fwIndex) && (fwIndex > hydrationIndex)) {
                result = c.substring(nameIndex + 2, fwIndex).trim();
            }
        }
        return result;
    }

    private List<String> parseCommand(String command) {
        String[] split = command.split("\\-"); //splitting the input string by hyphen
        //        List<String> parsed = new ArrayList<String>();
//        for (String s : split) {
//            parsed.add(s.replaceAll("\\s+", ""));
//        }
        return new ArrayList<String>(Arrays.asList(split));
    }
}
