package ui;

import java.io.*;

public class Main {
    // TODO: determine how to get Jackson Modules working without Maven. No .jar appears to be available. Necessary for
    //       deserialization of LocalDateTime fields.
    public static void main(String[] args) throws IOException {
        new GitBreadApp();
    }
}
