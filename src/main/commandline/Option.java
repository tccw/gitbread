package commandline;

public class Option {
    String argShort;
    String argLong;
    String description;
    boolean isBoolean;

    public Option(String argShort, String argLong, boolean isBoolean, String description) {
        this.argShort = argShort;
        this.argLong = argLong;
        this.isBoolean = isBoolean;
        this.description = description;
    }

    public Option(String argShort, boolean isBoolean, String description) {
        this.argShort = argShort;
        this.argLong = null;
        this.isBoolean = isBoolean;
        this.description = description;
    }

    // getters
    public String getArgShort() {
        return argShort;
    }

    public String getArgLong() {
        return argLong;
    }

    public String getDescription() {
        return description;
    }

    public boolean isBoolean() {
        return isBoolean;
    }
}