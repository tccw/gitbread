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

public class Reader {


    //REQUIRES: a properly formatted JSON file
    //EFFECTS: Load the provided collection file as a a RecipeDevCollection object.
    public static RecipeDevCollection loadRecipeCollectionFile(File file) throws IOException {
        ObjectMapper mapper = JsonMapper.builder().build();
        FileReader reader = new FileReader(file);
        registerObjectMapper(mapper);
        return mapper.readValue(reader, RecipeDevCollection.class);
    }

    public static RecipeDevCollection loadRecipeCollectionJson(String json) throws JsonProcessingException {
        ObjectMapper mapper = JsonMapper.builder().build();
        registerObjectMapper(mapper);
        return mapper.readValue(json, RecipeDevCollection.class);
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
