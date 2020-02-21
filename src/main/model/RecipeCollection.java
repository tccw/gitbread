package model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import persistence.Saveable;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Represents a collection/log of recipes histories, which is a LinkedList of past recipe versions.
 */

public class RecipeCollection implements Saveable {

    Map<String, RecipeHistory> collection;

    //EFFECTS: instantiates a new empty collection
    public RecipeCollection() {
        collection = new HashMap<String, RecipeHistory>();
    }

    //EFFECTS: creates and adds a new recipe to the recipe collection
    public void add(String title, RecipeHistory recipe) {
        this.collection.put(title, recipe);
        // create a new RecipeHistory "repository"
    }

    //EFFECTS: return the size of the recipe collection
    public int size() {
        return this.collection.size();
    }

    //EFFECTS: removes a specific recipe from the collection
    public void remove(String title) {
        this.collection.remove(title);
    }

    //EFFECTS: returns the recipe history associated with the given key
    public RecipeHistory get(String key) {
        return this.collection.get(key);
    }

    //REQUIRES: non-empty RecipeCollection
    //EFFECTS: return a formatted string with all recipe names, cook time, and prep time
    // foreach with a HashMap https://stackoverflow.com/questions/4234985/how-to-for-each-the-hashmap
    public String toString(boolean verbose) {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, RecipeHistory> entry : this.collection.entrySet()) {
            String title = entry.getKey();
            int numAttempts = entry.getValue().countAttempts();
            int numChanges = entry.getValue().countTimesModified();

            if (verbose) {
                result.append(String.format("%1$s : %2$d attempts, %3$d changes\n", title, numAttempts, numChanges));
            } else {
                result.append(String.format("%1$s\n", title));
            }
        }
        return result.toString();
    }

    public Map<String, RecipeHistory> getCollection() {
        return collection;
    }

    //MODIFIES: fileWriter
    //EFFECTS: writes the file to disk as a serialized JSON file
    @Override
    public void save(FileWriter fileWriter) throws IOException {
        ObjectMapper mapper = JsonMapper.builder().build();
        mapper.registerSubtypes(
                RecipeCollection.class,
                RecipeHistory.class,
                Recipe.class,
                BreadRecipe.class,
                Attempt.class);
        String json = mapper.writeValueAsString(this);
        fileWriter.write(json);
    }

//    private Map<String, List<Attempt>> makeHistoryFile() {
//        Map<String, List<Attempt>> attemptHistoryMap = new HashMap<>();
//        List<Attempt> attemptHistory = new ArrayList<Attempt>();
//        for (Map.Entry<String, RecipeHistory> entry : this.collection.entrySet()) {
//            String key = entry.getKey();
//            attemptHistoryMap.put(key, entry)
//
//
//        }
//    }

    public void setCollection(Map<String, RecipeHistory> collection) {
        this.collection = collection;
    }
}

