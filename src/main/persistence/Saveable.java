package persistence;

import java.io.FileWriter;
import java.io.IOException;

public interface Saveable {
    //MODIFIES: fileWriter
    //EFFECTS: writes the file to disk
    void save(FileWriter fileWriter) throws IOException;
}
