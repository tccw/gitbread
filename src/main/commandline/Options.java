package commandline;

import java.util.ArrayList;
import java.util.List;

public class Options {

    private List<Option> options;

    //EFFECTS: creates a new options object with an empty ArrayList
    public Options() {
        options = new ArrayList<>();
    }

    //MODIFIES: this
    //EFFECTS: adds an option to the Options list
    public void addOption(Option option) {
        options.add(option);
    }

    //EFFECTS: returns the size of options
    public int size() {
        return this.options.size();
    }

    public Option get(int i) {
        return this.options.get(i);
    }

    public List<Option> getOptions() {
        return this.options;
    }
}
