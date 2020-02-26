package persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {
    private FileWriter fileWriter;

    //EFFECTS: constructs a a FileWriter to write the data to a JSON text file
    public Writer(File file) throws IOException {
        this.fileWriter = new FileWriter(file);
    }

    //EFFECTS: writes the object to disk as a text file
    public void write(Saveable saveable) throws IOException {
        saveable.save(fileWriter);
    }

    //MODIFIES: this
    //EFFECTS: closes the writer
    //NOTE: This must be called in order to flush the system buffers and force the changes to disk
    // per this stackoverflow post: https://stackoverflow.com/questions/14060250/java-txt-file-from-filewriter-is-empty
    public void close() throws IOException {
        fileWriter.close();
    }

    public FileWriter getFileWriter() {
        return fileWriter;
    }

    public void setFileWriter(FileWriter fileWriter) {
        this.fileWriter = fileWriter;
    }
}
