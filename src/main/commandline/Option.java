package commandline;

public class Option {
    private String argShort;
    private String argLong;
    private String description;
    private boolean isBoolean;

    public Option(String shortFlag, String longFlag, boolean isBoolean, String description) {
        this.argShort = shortFlag;
        this.argLong = longFlag;
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