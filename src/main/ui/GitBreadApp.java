package ui;

import model.BreadRecipe;
import model.Recipe;
import model.RecipeCollection;
import model.RecipeHistory;

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

        System.out.println("GitBread v0.0");
        while (keepGoing) {

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
        System.out.println("    bread select : [-n name]");
        System.out.println("        usage example: bread select -n Pizza\n");
        System.out.println("    bread list : [-v verbose]");
        System.out.println("        usage example: bread list");
        System.out.println("        usage example: bread list -v\n");


    }

    private void processCommand(String command) {
        List<String> parsed = parseCommand(command);
        String localCmd = parsed.get(0).trim();
        List<String> params = parsed.subList(1, parsed.size() - 1);

        if (localCmd.equals("bread arc")) {
            String title = getTitle(command);
            double hydration;
            double flourWeight;
            int doughWeight;

            if (command.contains("-dw")) {
                doughWeight = Integer
                        .parseInt(command.substring(command.indexOf("-dw"))
                                .replaceAll("\\D+", ""));
                collection.add(title, new RecipeHistory(new BreadRecipe(doughWeight)));
            }
        } else if (localCmd.equals("select")) {
            //stub
        } else if (localCmd.equals("attempt")) {
            //stub
        } else if (localCmd.equals("view")) {
            //stub
        } else if (localCmd.equals("read")) {
            //stub
        }
    }

    private String getTitle(String c) {
        String result = "";
        if (c.contains("-dw")) {
            int dwIndex = c.indexOf("-dw");
            int nIndex = c.indexOf("-n");
            if (dwIndex > nIndex) {
                result = c.substring(nIndex + 2, dwIndex).trim();
            } else {
                result = c.substring(nIndex + 2).trim();
            }
        } else if (c.contains("-fw")) {
            int fwIndex = c.indexOf("-fw");
            int hIndex = c.indexOf("-h");
            int nIndex = c.indexOf("-n");
            if (nIndex > Math.max(hIndex, fwIndex)) {
                result = c.substring(nIndex + 2).trim();
            } else if ((nIndex < fwIndex) && (fwIndex < hIndex)) {
                result = c.substring(nIndex + 2, hIndex).trim();
            } else if ((nIndex < fwIndex) && (fwIndex > hIndex)) {
                result = c.substring(nIndex + 2, fwIndex).trim();
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
