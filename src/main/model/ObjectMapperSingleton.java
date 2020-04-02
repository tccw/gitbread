package model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// TODO: could I just extend ObjectMapper here? Would that be bad design?
public class ObjectMapperSingleton {
    private static ObjectMapper INSTANCE;

    private ObjectMapperSingleton() {
        // set the constructor to private so that no other classes can call it
    }

    public static ObjectMapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = JsonMapper.builder().build();
            registerObjectMapper(INSTANCE);
        }
        return INSTANCE;
    }

    //https://codeboje.de/jackson-java-8-datetime-handling/
    private static void registerObjectMapper(ObjectMapper mapper) {
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        mapper.registerSubtypes(
                RecipeDevCollection.class,
                Node.class,
                NodeGraph.class,
                Recipe.class,
                BreadRecipe.class,
                Attempt.class,
                Ingredient.class);
    }
}
