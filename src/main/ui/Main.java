package ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;

import model.*;

import java.io.*;
import java.time.Clock;

public class Main {

    public static void main(String[] args) throws IOException {
//        new GitBreadApp();
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        RecipeCollection object = gson.
//                fromJson(new FileReader("data/recipecollections/coloutput.json"), RecipeCollection.class);
        final String TEST_COLLECTION = "data/recipecollections/testWriter.json";
        FileWriter fileWriter = new FileWriter(TEST_COLLECTION);
        final Clock clock = Clock.systemDefaultZone();

        BreadRecipe frenchLoaf = new BreadRecipe(950);
        frenchLoaf.setSugarFraction(0.03);
        BreadRecipe hearthLoaf = new BreadRecipe(375, 0.58);
        RecipeHistory frenchLoafHist = new RecipeHistory(frenchLoaf);
        RecipeHistory hearthLoafHist = new RecipeHistory(hearthLoaf);
        frenchLoafHist.setMasterRecipe(frenchLoaf);
        frenchLoafHist.setTestingRecipe(new BreadRecipe(650, 0.72));
        hearthLoafHist.setMasterRecipe(hearthLoaf);
        hearthLoafHist.setTestingRecipe(new BreadRecipe(650, 0.72));
        RecipeCollection collection = new RecipeCollection();
        collection.add("French loaf", frenchLoafHist);
        collection.add("Hearth loaf", hearthLoafHist);
        collection.get("French loaf").attempt(collection.get("French loaf").getMasterRecipe(), clock);
        System.out.println(collection.get("French loaf").getMasterRecipe().toString());
        System.out.println("-------------------------------------------------");

        final PolymorphicTypeValidator validator = BasicPolymorphicTypeValidator.builder()
                .allowIfBaseType(Recipe.class)
                .build();
        ObjectMapper safeMapper = JsonMapper.builder()
//                .activateDefaultTyping(validator)
                .build();
        safeMapper.registerSubtypes(RecipeCollection.class, RecipeHistory.class, Recipe.class, BreadRecipe.class, Attempt.class, Ingredient.class);
//        safeMapper.registerModule(JavaTimeModule());
        String json = safeMapper.writerWithDefaultPrettyPrinter().writeValueAsString(collection);

        fileWriter.write(json);
        fileWriter.close();
        FileReader fileReader = new FileReader(TEST_COLLECTION);
// TODO: determine how to get Jackson Modules working without Maven. No .jar appears to be available.
        RecipeCollection testCollection = safeMapper.readValue(fileReader, RecipeCollection.class);
        System.out.println(testCollection.get("French loaf").getMasterRecipe().toString());
        System.out.println(testCollection.toString(true));
        System.out.println(testCollection.get("French loaf"));



    }
}
