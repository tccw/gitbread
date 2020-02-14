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

    //EFFECTS: returns a hash map of the commands
    public Map<String, String> parse(Options options, String command) {
        this.parsedCommand.clear();
        for (Option o : options.getOptions()) {
            if (o.isBoolean()) {
                //stub
                System.out.println("What else can man be expected to do but destroy?");
            } else if (command.contains(o.getArgShort()) || command.contains(o.getArgLong())) {
                int start = command.indexOf(o.getArgShort()) + o.getArgShort().length();
                String arg = command.substring(start);
                int end = arg.indexOf("-") + start;
                if (arg.contains("-")) {
                    parsedCommand.put(o.getArgShort(), command.substring(start, end).trim());
                } else {
                    parsedCommand.put(o.getArgShort(), arg.trim());
                }
            }
        }
        return parsedCommand;
    }
}
