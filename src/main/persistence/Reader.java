package persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

public class Reader {


    //TODO: use the singleton pattern to make sure there is only ever a single instance of ObjectMapper running.
    //REQUIRES: a properly formatted JSON file
    //EFFECTS: Load the provided collection file as a a RecipeDevCollection object.
    public static RecipeDevCollection loadRecipeCollectionFile(File file) throws IOException {
        ObjectMapper mapper = JsonMapper.builder().build();
        FileReader reader = new FileReader(file);
        registerObjectMapper(mapper);

        return checkoutRecipeDevCollection(null, file, reader, mapper);
    }

    public static RecipeDevCollection loadRecipeCollectionJson(String json) throws IOException {
        ObjectMapper mapper = JsonMapper.builder().build();
        registerObjectMapper(mapper);
        return checkoutRecipeDevCollection(json, null, null, mapper);
    }

    private static RecipeDevCollection checkoutRecipeDevCollection(String json,
                                                                   File file,
                                                                   FileReader reader,
                                                                   ObjectMapper mapper) throws IOException {
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

    public static RecipeDevHistory loadRecipeDevHistoryJson(String json) throws JsonProcessingException {
        ObjectMapper mapper = JsonMapper.builder().build();
        registerObjectMapper(mapper);
        /*
            This is here as a patchwork quick-fix to true ID based serialization with Jackson. This ensures that
            the loaded file has a reference to the proper commit. Currently the active commit is being deserialized
            as a separate object with the same properties which breaks committing and attempts.
         */

        RecipeDevHistory result = mapper.readValue(json, RecipeDevHistory.class);
        result.checkout("master");
        return result;
    }

    //https://codeboje.de/jackson-java-8-datetime-handling/
    private static void registerObjectMapper(ObjectMapper mapper) {
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        mapper.registerSubtypes(
                RecipeDevCollection.class,
                RecipeDevHistory.class,
                Commit.class,
                Recipe.class,
                BreadRecipe.class,
                Attempt.class,
                Ingredient.class);
    }
}
