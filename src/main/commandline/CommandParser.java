package commandline;

import java.util.HashMap;
import java.util.Map;

public class CommandParser {
    Options options;
    String command;
    Map<String, String> parsedCommand;

    //EFFECTS: creates a new parser
    public CommandParser() {
        this.parsedCommand = new HashMap<String, String>();
    }

    //MODIFIES: this
    //EFFECTS: returns a hash map of the commands
    public void parse(Options options, String command) {
        this.parsedCommand.clear();
        for (Option opt : options.getOptions()) {
            if (opt.isBoolean() && (command.contains(opt.argShort) || command.contains(opt.argLong))) {
                parsedCommand.put(opt.getArgShort(), null);
            } else if (command.contains(opt.getArgLong())) {
                extractArgParams(command, opt, false);
            } else if (command.contains(opt.getArgShort())) {
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
            parsedCommand.put(opt.getArgShort(), command.substring(start, end).trim());
        } else {
            parsedCommand.put(opt.getArgShort(), arg.trim());
        }
    }

    //EFFECTS: returns true of a
    public boolean containsFlag(String k) {
        return parsedCommand.containsKey(k);
    }

    //EFFECTS:
    public String get(String k) {
        return parsedCommand.get(k);
    }

    public int size() {
        return parsedCommand.size();
    }

    public Map<String, String> getParsedCommand() {
        return this.parsedCommand;
    }

}
