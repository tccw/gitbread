package ui;

import com.sun.javafx.image.IntPixelGetter;
import model.BreadRecipe;
import model.Recipe;
import model.RecipeCollection;
import model.RecipeHistory;

import java.time.Clock;
import java.util.*;

public class GitBreadApp {

    private Scanner input;
    RecipeCollection collection = new RecipeCollection();
    Clock clock = Clock.systemDefaultZone();

    public GitBreadApp() {
        runGitBread();
    }

    private void runGitBread() {
        boolean keepGoing = true;
        String command;
        input = new Scanner(System.in);

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
        String phrase = parseCommandPhrase(command);
        if (phrase.equals("bread new")) {
            doBreadNew(command);
        } else if (phrase.equals("bread list")) {
            doBreadList(command);
        } else if (phrase.equals("bread attempt")) {
            doBreadAttempt(command);
        } else if (phrase.equals("bread view master")) {
            doBreadView(command, true);
        } else if (phrase.equals("bread view testing")) {
            doBreadView(command, false);
        } else if (phrase.equals("bread scale")) {
            doBreadScale(command);
        } else {
            System.out.println(String.format("'%s' is not a valid command.", command));
        }
    }

    //EFFECTS: helper for processCommand(), scales the recipe and modifies only the ingredient weights in the master
    private void doBreadScale(String c) {
        Map<String, String> args = breadScaleArgsHelper(c);
        scaleBreadRecipe(c, args);
    }

    //EFFECTS: helper for doBreadScale which uses the parsed args from breadScaleArgsHelper to actually call
    //        the scaling methods from BreadRecipe.
    private void scaleBreadRecipe(String c, Map<String, String> args) {
        BreadRecipe recipe = (BreadRecipe) collection.get(args.get("-n")).getMasterRecipe();
        if (c.contains("-dw")) {
            recipe.scaleByDoughWeight(Integer.parseInt(args.get("-dw")));
        } else if (c.contains("-fw")) {
            recipe.scaleByFlourWeight(Integer.parseInt(args.get("-fw")));
        }

        if (c.contains("-v")) {
            System.out.println(recipe.toString());
        }
    }

    //EFFECTS: helper for doBreadScale which parses the arguments from the command
    private Map<String, String> breadScaleArgsHelper(String c) {
        String[] flags = {"-n", "-m", "-t", "-v", "-dw", "-fw"};
        Map<String, String> args = new HashMap<>();
        for (String s : flags) {
            if (c.contains(s)) {
                int start = c.indexOf(s) + s.length();
                String arg = c.substring(start);
                int end = arg.indexOf("-") + start;
                if (arg.contains("-")) {
                    args.put(s, c.substring(start, end).trim());
                } else {
                    args.put(s, arg.trim());
                }
            }
        }
        return args;
    }

    //EFFECTS: helper for processCommand(), attempts the requested recipe using the master or testing version
    private void doBreadAttempt(String c) {
        String[] flags = {"-n", "-m", "-t"};
        String title = "";
        List<Integer> indexes = new ArrayList<>();
        for (String s : flags) {
            if (c.contains(s)) {
                indexes.add(c.indexOf(s));
            }
        }
        title = breadAttemptTitleParse(c, title, indexes);

        if (c.contains("-m")) {
            Recipe master = collection.get(title).getMasterRecipe();
            collection.get(title).attempt(master, clock);
        } else {
            Recipe testing = collection.get(title).getTestingRecipe();
            collection.get(title).attempt(testing, clock);
        }
    }

    //EFFECTS: helper for doBreadAttempt to parse the name/title from the command string.
    private String breadAttemptTitleParse(String c, String title, List<Integer> indexes) {
        if (c.contains("-m") && (c.contains("-t"))) {
            System.out.println("Cannot add an attempt with two recipe version simultaneously.");
        } else if (indexes.get(0) > indexes.get(1)) {
            title = c.substring(indexes.get(0) + 2).trim();
        } else {
            title = c.substring(indexes.get(0) + 2, indexes.get(1)).trim();
        }
        return title;
    }

    //EFFECTS: pulls the command phrase 'bread <command>' from the input string
    private String parseCommandPhrase(String c) {
        String phrase;
        if (c.contains("-")) {
            phrase = c.substring(0, c.indexOf("-")).trim();
        } else {
            phrase = c.trim();
        }
        return phrase;
    }

    //EFFECTS: helper for processCommand(), prints the full recipe and instructions to the console
    private void doBreadView(String c, boolean master) {
        String key = c.substring(c.indexOf("-n") + 2).trim();
        if (master) {
            System.out.println(String.format("----------%s (master branch)----------\n", key));
            System.out.println(collection.get(key).getMasterRecipe().toString());
        } else {
            System.out.println(String.format("----------%s (testing branch)----------\n", key));
            System.out.println(collection.get(key).getTestingRecipe().toString());
        }
    }

    //TODO: Easier parsing is to use the index of the comand and the index of the nex hypen if it exists
    //EFFECTS: helper for processCommand(), prints the recipes in the collection
    private void doBreadList(String c) {
        if (c.contains("-v")) {
            System.out.println("------- Recipe Collection -------");
            System.out.println(collection.toString(true));
        } else {
            System.out.println("------- Recipe Collection -------");
            System.out.println(collection.toString(false));
        }
    }

    //MODIFIES: collection
    //EFFECTS: helper for processCommand(), adds a new recipe history to the collection
    private void doBreadNew(String c) {
        String title = parseTitle(c);
        if (c.contains("-dw")) {
            int doughWeight = Integer
                    .parseInt(c.substring(c.indexOf("-dw"))
                            .replaceAll("\\D+", ""));
            collection.add(title, new RecipeHistory(new BreadRecipe(doughWeight)));
        } else if (c.contains("-fw")) {
            parseFlourAndHydration(c, title);
        }

        if (c.contains("-si")) {
            collection.get(title).get(0).setInstructions(c.substring(c.indexOf("-si") + 3).trim());
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

    //EFFECTS: helper for breadArcCommand(), pulls the title/name from a 'bread new' command.
    private String parseTitle(String c) {
        String result = "";
        if (c.contains("-dw") && (c.contains("-fw") || (c.contains("-h")))) {
            System.out.println("Cannot set a recipe by both dough weight and flour weight.");
        }

        result = parseTitleHelper(c, result);
        return result;

    }

    private String parseTitleHelper(String c, String result) {
        int nameIndex = c.indexOf("-n");
        if (c.contains("-dw")) {
            int dwIndex = c.indexOf("-dw");
            if (dwIndex > nameIndex) {
                result = c.substring(nameIndex + 2, dwIndex).trim();
            } else {
                result = c.substring(nameIndex + 2).trim();
            }
        } else if (c.contains("-fw")) {
            int fwIndex = c.indexOf("-fw");
            int hydrationIndex = c.indexOf("-h");

            if (nameIndex > Math.max(hydrationIndex, fwIndex)) {
                result = c.substring(nameIndex + 2).trim();
            } else {
                result = c.substring(nameIndex + 2, Math.min(hydrationIndex, fwIndex)).trim();
            }
        }
        return result;
    }
}
