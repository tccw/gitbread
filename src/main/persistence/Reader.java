package persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.BranchDoesNotExistException;
import model.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

public class Reader {

    private static ObjectMapper mapper = ObjectMapperSingleton.getInstance();

    //TODO: use the singleton pattern to make sure there is only ever a single instance of ObjectMapper running.
    //REQUIRES: a properly formatted JSON file
    //EFFECTS: Load the provided collection file as a a RecipeDevCollection object.
    public static RecipeDevCollection loadRecipeCollectionFile(File file) throws IOException,
                                                                                 BranchDoesNotExistException {
        FileReader reader = new FileReader(file);
        return checkoutRecipeDevCollection(null, file, reader);
    }

    public static RecipeDevCollection loadRecipeCollectionJson(String json) throws IOException,
                                                                                   BranchDoesNotExistException {
        return checkoutRecipeDevCollection(json, null, null);
    }

    private static RecipeDevCollection checkoutRecipeDevCollection(String json,
                                                                   File file,
                                                                   FileReader reader) throws IOException,
                                                                                BranchDoesNotExistException {
        RecipeDevCollection result;
        if (file == null) {
            result = mapper.readValue(json, RecipeDevCollection.class);
        } else {
            result = mapper.readValue(reader, RecipeDevCollection.class);
        }
        Set<String> keys = result.getCollection().keySet();
        for (String key : keys) {
            result.get(key).checkout("master");
        }
        return result;
    }

    public static NodeGraph loadRecipeDevHistoryJson(String json) throws JsonProcessingException,
                                                                         BranchDoesNotExistException {
        /*
           ** This is here as a patchwork quick-fix to true ID based serialization with Jackson. This ensures that
            the loaded file has a reference to the proper commit. Currently the active commit is being deserialized
            as a separate object with the same properties which breaks committing and attempts.
         */
        NodeGraph result = mapper.readValue(json, NodeGraph.class);
        result.checkout("master"); // **
        return result;
    }

}
