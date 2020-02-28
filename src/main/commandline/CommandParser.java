package commandline;

import java.util.HashMap;
import java.util.Map;

public class CommandParser {
    String command;
    Map<String, String> parsedOptions;

    //EFFECTS: creates a new parser
    public CommandParser() {
        this.parsedOptions = new HashMap<String, String>();
        command = null;
    }

    //MODIFIES: this
    //EFFECTS: returns a hash map of the commands
    // TODO: Fix the bug allowing -verbose to be parsed the same as -v and --verbose.
    public void parse(Options options, String command) {
        this.command = commandPhrase(command);
        this.parsedOptions.clear();
        for (Option opt : options.getOptions()) {
            if (opt.isBoolean() && (command.contains(opt.getArgShort()) || command.contains(opt.getArgLong()))) {
                parsedOptions.put(opt.getArgShort(), null);
            } else if (command.contains(opt.getArgLong())) {
                extractArgParams(command, opt, false);
            } else if (command.contains(opt.getArgShort() + " ")) {
                extractArgParams(command, opt, true);
            }
        }
    }

    //EFFECTS: extracts the arguments from both short and long flags that have them
    private void extractArgParams(String command, Option opt, boolean isShort) {
        int start;
        if (isShort) {
            start = command.indexOf(opt.getArgShort()) + opt.getArgShort().length();
        } else {
            start = command.indexOf(opt.getArgLong()) + opt.getArgLong().length();
        }
        String arg = command.substring(start);
        int end = arg.indexOf("-") + start;
        if (arg.contains("-")) {
            parsedOptions.put(opt.getArgShort(), command.substring(start, end).trim());
        } else {
            parsedOptions.put(opt.getArgShort(), arg.trim());
        }
    }

    //EFFECTS: pulls the command phrase 'bread <command>' from the input string
    private String commandPhrase(String c) {
        String phrase;
        if (c.contains("-")) {
            phrase = c.substring(0, c.indexOf("-")).trim();
        } else {
            phrase = c.trim();
        }
        return phrase;
    }

    //EFFECTS: returns true of a
    public boolean containsFlag(String k) {
        return parsedOptions.containsKey(k);
    }

    //EFFECTS: returns the value associated with the key 'k'
    public String get(String k) {
        return parsedOptions.get(k);
    }

    //EFFECTS: returns the size of the map
    public int size() {
        return parsedOptions.size();
    }

    public String getCommand() {
        return command;
    }
}
