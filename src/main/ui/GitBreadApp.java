package ui;

import com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector;
import commandline.CommandParser;
import commandline.Option;
import commandline.Options;
import model.BreadRecipe;
import model.Recipe;
import model.RecipeCollection;
import model.RecipeHistory;

import java.time.Clock;
import java.util.*;

public class GitBreadApp {

    RecipeCollection collection = new RecipeCollection();
    Clock clock = Clock.systemDefaultZone();
    Options options;

    public GitBreadApp() {
        runGitBread();
        makeOptions();
        options = makeOptions();
    }

    private void runGitBread() {
        boolean keepGoing = true;
        String command;
        Scanner input = new Scanner(System.in);

        System.out.println("GitBread v0.0 (type 'bread help' for available commands.)");
        while (keepGoing) {
            System.out.print("> ");
            command = input.nextLine();
//            command = command.toLowerCase();

            if (command.equals("bread quit")) {
                keepGoing = false;
            } else if (command.equals("bread help")) {
                displayHelp();
            } else {
                processCommand(command);
            }
        }
    }

    private Options makeOptions() {
        options = new Options();
        options.addOption(new Option("-n", "--name", false, "The name of the recipe"));
        options.addOption(new Option("-dw", "--doughweight", false, "Mixed dough weight in grams"));
        options.addOption(new Option("-fw", "--flourweight", false, "Flour weight in grams"));
        options.addOption(new Option("-h", "--hydration", false, "Hydration fraction for fluid calculation"));
        options.addOption(new Option("-si", "--setinstructions", false, "Recipe instructions"));
        options.addOption(new Option("-v", "--verbose", true, "Print out what is happening"));
        options.addOption(new Option("-m", "--master", true, "master recipe selector"));
        options.addOption(new Option("-t", "--testing", true, "testing recipe selector"));
        return options;
    }


    //EFFECTS: display help information about the available commands
    private void displayHelp() {
        System.out.println("\nAvailable commands:\n");
        System.out.println("Command style is 'bread <command> <flags> <args>'");
        System.out.println("<command> list\n");
        breadNewHelp();
        breadListHelp();
        breadViewHelp();
        breadAttemptHelp();
        breadScaleHelp();
    }

    private void breadNewHelp() {
        System.out.println("    bread new : [-n <name>] [-dw dough weight] || [-fw flour weight] [-h hydration]"
                + " [-si set instructions]");
        System.out.println("        usage example: bread new -n French loaf -dw 1000");
        System.out.println("        usage example: bread new -n Pizza -fw 350 -h 0.7");
        System.out.println("        usage example: bread new -n Pizza dough -fw 350 -h 0.7 -si 1. Mix ingredients "
                + "2. Rise for 2-24 hours 3. Stretch dough into circle 4. Top and bake for 6 minutes");
    }

    private void breadListHelp() {
        System.out.println("    bread list : [-v verbose]");
        System.out.println("        usage example: bread list");
        System.out.println("        usage example: bread list -v");
    }

    private void breadSelectHelp() {
        System.out.println("    bread select : [-n name]");
        System.out.println("        usage example: bread select -n Pizza");
    }

    private void breadViewHelp() {
        System.out.println("    bread view master/testing : [-n name]");
        System.out.println("        usage example: bread view master -n Pizza dough");
        System.out.println("        usage example: bread view testing -n Pizza dough");
    }

    private void breadAttemptHelp() {
        System.out.println("    bread attempt : [-n name] [-m master] [-t testing]");
        System.out.println("        usage example: bread attempt -n French loaf -m");
    }

    private void breadScaleHelp() {
        System.out.println("    bread scale : [-n name] [-m master] [-t testing] [-v verbose] [-dw dough weight]"
                + " || [-fw flour weight]");
        System.out.println("        usage example: bread scale -n French loaf -m");
        System.out.println("        usage example: bread scale -n Pizza dough -v");
    }


    //EFFECTS: processes the commands the user inputs.
    //notes: a scaled recipe is not considered a new recipe.
    private void processCommand(String command) {
        CommandParser parser = new CommandParser();
        parser.parse(makeOptions(), command);
        String phrase = parser.getCommand();
        if (phrase.equals("bread new")) {
            doBreadNew(parser);
        } else if (phrase.equals("bread list")) {
            doBreadList(parser);
        } else if (phrase.equals("bread attempt")) {
            doBreadAttempt(parser);
        } else if (phrase.equals("bread view")) {
            doBreadView(parser);
        } else if (phrase.equals("bread scale")) {
            doBreadScale(parser);
        } else {
            System.out.println(String.format("'%s' is not a valid command.", command));
        }
    }

    //EFFECTS: helper for processCommand(), scales the recipe and modifies only the ingredient weights in the master
    private void doBreadScale(CommandParser p) {
        BreadRecipe recipe;
        if (p.containsFlag("-t")) {
            recipe = (BreadRecipe) collection.get(p.get("-t")).getMasterRecipe();
        } else {
            recipe = (BreadRecipe) collection.get(p.get("-n")).getMasterRecipe();
        }
        if (p.containsFlag("-dw")) {
            recipe.scaleByDoughWeight(Integer.parseInt(p.get("-dw")));
        } else if (p.containsFlag("-fw")) {
            recipe.scaleByFlourWeight(Integer.parseInt(p.get("-fw")));
        }
        if (p.containsFlag("-v")) {
            System.out.println(recipe.toString());
        }

    }

    //EFFECTS: helper for processCommand(), attempts the requested recipe using the master or testing version
    private void doBreadAttempt(CommandParser p) {
        String title = p.get("-n");
        if (p.containsFlag("-m") && !p.containsFlag("-t")) {
            Recipe master = collection.get(title).getMasterRecipe();
            collection.get(title).attempt(master, clock);
        } else if (p.containsFlag("-t")) {
            Recipe testing = collection.get(title).getTestingRecipe();
            collection.get(title).attempt(testing, clock);
        } else {
            System.out.println("Please specify whether you are using the master or testing recipe.");
        }
    }

    //EFFECTS: helper for processCommand(), prints the full recipe and instructions to the console
    private void doBreadView(CommandParser p) {
        String key = p.get("-n");
        if (p.containsFlag("-m") && p.containsFlag("-t")) {
            System.out.println(String.format("----------%s (master branch)----------\n", key));
            System.out.println(collection.get(key).getMasterRecipe().toString());
            System.out.println(String.format("----------%s (testing branch)----------\n", key));
            System.out.println(collection.get(key).getTestingRecipe().toString());
        } else if (p.containsFlag("-m")) {
            System.out.println(String.format("----------%s (master branch)----------\n", key));
            System.out.println(collection.get(key).getMasterRecipe().toString());
        } else if (p.containsFlag("-t")) {
            System.out.println(String.format("----------%s (testing branch)----------\n", key));
            System.out.println(collection.get(key).getTestingRecipe().toString());
        } else {
            System.out.println("Use '-m' to view the master, '-t' to view the testing, or -m -t to view both");
        }
    }

    //EFFECTS: helper for processCommand(), prints the recipes in the collection
    private void doBreadList(CommandParser p) {
        if (p.containsFlag("-v")) {
            System.out.println("------- Recipe Collection -------");
            System.out.println(collection.toString(true));
        } else {
            System.out.println("------- Recipe Collection -------");
            System.out.println(collection.toString(false));
        }
    }

    //MODIFIES: collection
    //EFFECTS: helper for processCommand(), adds a new recipe history to the collection
    private void doBreadNew(CommandParser p) {
        String title = p.get("-n");
        if (p.containsFlag("-dw")) {
            collection.add(title, new RecipeHistory(new BreadRecipe(Integer.parseInt(p.get("-dw")))));
        } else if (p.containsFlag("-fw")) {
            collection.add(title, new RecipeHistory(new BreadRecipe(Integer.parseInt(p.get("-fw")),
                    Double.parseDouble(p.get("-h")))));
        }
        if (p.containsFlag("-si")) {
            collection.get(title).get(0).setInstructions(p.get("-si"));
        }
    }
}
