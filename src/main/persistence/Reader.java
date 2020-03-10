package persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
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
        mapper.registerSubtypes(
                RecipeDevCollection.class,
                RecipeDevHistory.class,
                Commit.class,
                Recipe.class,
                BreadRecipe.class,
                Attempt.class,
                Ingredient.class);
        return mapper.readValue(reader, RecipeDevCollection.class);
    }

    public static RecipeDevCollection loadRecipeCollectionJson(String json) throws JsonProcessingException {
        ObjectMapper mapper = JsonMapper.builder().build();
        mapper.registerSubtypes(
                RecipeDevCollection.class,
                RecipeDevHistory.class,
                Commit.class,
                Recipe.class,
                BreadRecipe.class,
                Attempt.class,
                Ingredient.class);
        return mapper.readValue(json, RecipeDevCollection.class);
    }

}
