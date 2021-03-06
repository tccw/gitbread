package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import persistence.Saveable;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
Represents a collection/log of recipes histories, which is a LinkedList of past recipe versions.
 */

public class RecipeDevCollection implements Saveable {

    private Map<String, NodeGraph> collection;

    //EFFECTS: instantiates a new empty collection
    public RecipeDevCollection() {
        collection = new HashMap<>();
    }

    //EFFECTS: creates and adds a new recipe to the recipe collection
    public void add(String title, NodeGraph recipeHistory) {
        this.collection.put(title, recipeHistory);
        // create a new NodeGraph "repository"
    }

    //EFFECTS: return the size of the recipe collection
    public int size() {
        return this.collection.size();
    }

    //EFFECTS: return true if the collection is empty (size = 0)
    @JsonIgnore
    public boolean isEmpty() {
        return this.collection.isEmpty();
    }

    //EFFECTS: removes a specific recipe from the collection
    public void remove(String title) {
        this.collection.remove(title);
    }

    //EFFECTS: returns the recipe history associated with the given key
    public NodeGraph get(String key) {
        return this.collection.get(key);
    }

    //REQUIRES: non-empty RecipeDevCollection
    //EFFECTS: return a formatted string with all recipe names, cook time, and prep time
    // foreach with a HashMap https://stackoverflow.com/questions/4234985/how-to-for-each-the-hashmap
    public String toString(boolean verbose) {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, NodeGraph> entry : this.collection.entrySet()) {
            String title = entry.getKey();
            int numAttempts = entry.getValue().totalAttempts();
            int numChanges = entry.getValue().size() - 1;
            if (verbose) {
                result.append(String.format("%1$s : %2$d attempts, %3$d changes\n", title, numAttempts, numChanges));
            } else {
                result.append(String.format("%1$s\n", title));
            }
        }
        return result.toString();
    }

    //MODIFIES: fileWriter
    //EFFECTS: writes the file to disk as a serialized JSON file
    @Override
    public void save(FileWriter fileWriter) throws IOException {
        fileWriter.write(this.toJson());
    }

    //EFFECTS: helper for writing file to Json and for the steganography package.
    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = ObjectMapperSingleton.getInstance();
        String json;
        json = mapper.writeValueAsString(this);
        return json;
    }

    public void setCollection(Map<String, NodeGraph> collection) {
        this.collection = collection;
    }

    public Map<String, NodeGraph> getCollection() {
        return collection;
    }

}

