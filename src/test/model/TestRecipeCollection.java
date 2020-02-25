package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.Writer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestRecipeCollection {

    RecipeCollection recipeCollection;
    RecipeHistory recipeHistoryFrenchLoaf;
    RecipeHistory recipeHistoryPizza;
    RecipeHistory recipeHistoryCinnamonRaisin;
    Recipe frenchLoaf = new BreadRecipe(1000);
    Recipe pizza = new BreadRecipe(350, 0.68);
    Recipe cinnamonRaisin = new BreadRecipe(800);

    @BeforeEach
    void setUp() {
        recipeCollection = new RecipeCollection();
        recipeHistoryFrenchLoaf = new RecipeHistory();
        recipeHistoryPizza = new RecipeHistory();
        recipeHistoryCinnamonRaisin = new RecipeHistory();

        recipeHistoryFrenchLoaf.setMasterRecipe(frenchLoaf);
        recipeHistoryFrenchLoaf.addToHistory(frenchLoaf);

        recipeHistoryCinnamonRaisin.setMasterRecipe(cinnamonRaisin);
        recipeHistoryCinnamonRaisin.addToHistory(cinnamonRaisin);

        recipeHistoryPizza.setMasterRecipe(pizza);
        recipeHistoryPizza.addToHistory(pizza);
        recipeHistoryPizza.addToHistory(new BreadRecipe(350, 0.58));
        recipeHistoryPizza.addToHistory(new BreadRecipe(350, 0.64));

    }

    @Test
    void TestConstructor() {
        assertEquals(0, recipeCollection.size());
    }

    @Test
    void TestAddSingle() {
        assertTrue(recipeCollection.isEmpty());
        recipeCollection.add("French Loaf", recipeHistoryFrenchLoaf);
        assertEquals(1, recipeCollection.size());
    }

    @Test
    void TestAddMultiple() {
        assertTrue(recipeCollection.isEmpty());
        recipeCollection.add("French loaf", recipeHistoryFrenchLoaf);
        recipeCollection.add("Pizza", recipeHistoryPizza);
        recipeCollection.add("Cinnamon Raisin", recipeHistoryCinnamonRaisin);
        assertEquals(3, recipeCollection.size());
    }

    @Test
    void TestAddSingleRemoveSingle() {
        assertTrue(recipeCollection.isEmpty());
        recipeCollection.add("French loaf", recipeHistoryFrenchLoaf);
        recipeCollection.remove("French loaf");
        assertEquals(0, recipeCollection.size());
    }

    @Test
    void TestAddRemoveAddMultiple() {
        assertTrue(recipeCollection.isEmpty());
        recipeCollection.add("French loaf", recipeHistoryFrenchLoaf);
        recipeCollection.add("Pizza", recipeHistoryPizza);
        recipeCollection.add("Cinnamon Raisin", recipeHistoryCinnamonRaisin);
        assertEquals(3, recipeCollection.size());
        recipeCollection.remove("Cinnamon Raisin");
        recipeCollection.remove("French loaf");
        assertEquals(1, recipeCollection.size());
    }

    @Test
    void TestGet() {
        assertTrue(recipeCollection.isEmpty());
        recipeCollection.add("French loaf", recipeHistoryFrenchLoaf);
        recipeCollection.add("Pizza", recipeHistoryPizza);
        recipeCollection.add("Cinnamon Raisin", recipeHistoryCinnamonRaisin);
        assertEquals(recipeHistoryFrenchLoaf, recipeCollection.get("French loaf"));
    }

    @Test
    void TestToStringVerbose() {
        assertTrue(recipeCollection.isEmpty());
        recipeCollection.add("French loaf", recipeHistoryFrenchLoaf);
        recipeCollection.add("Pizza dough", recipeHistoryPizza);
        String expected = "Pizza dough : 0 attempts, 2 changes\n"
                + "French loaf : 0 attempts, 0 changes\n";
        assertEquals(expected, recipeCollection.toString(true));
    }

    @Test
    void TestToString() {
        assertTrue(recipeCollection.isEmpty());
        recipeCollection.add("French loaf", recipeHistoryFrenchLoaf);
        recipeCollection.add("Pizza dough", recipeHistoryPizza);
        String expected = "Pizza dough\n"
                + "French loaf\n";
        assertEquals(expected, recipeCollection.toString(false));
    }

    @Test
    void TestRecipeHistory() {
        assertEquals(1, recipeHistoryCinnamonRaisin.size());
        List<Recipe> expected = new LinkedList<>();
        expected.add(cinnamonRaisin);
        assertEquals(expected, recipeHistoryCinnamonRaisin.getHistory());
    }

    @Test
    void TestGetCollection() {
        assertTrue(recipeCollection.isEmpty());
        recipeCollection.add("French loaf", recipeHistoryFrenchLoaf);
        recipeCollection.add("Pizza dough", recipeHistoryPizza);
        Map<String, RecipeHistory> testCollection = recipeCollection.getCollection();
        assertEquals(testCollection.get("French loaf"), recipeHistoryFrenchLoaf);
        assertEquals(testCollection.get("Pizza dough"), recipeHistoryPizza);
    }

    @Test
    void TestSave() {
        assertTrue(recipeCollection.isEmpty());
        recipeCollection.add("French loaf", recipeHistoryFrenchLoaf);
        recipeCollection.add("Pizza dough", recipeHistoryPizza);

        try {
            Writer writer = new Writer(new File("data/recipecollections/recipeCollectionTest.json"));
            writer.write(recipeCollection);
        } catch (IOException e) {
            fail("Unexpected IOException.");
        }
    }

    @Test
    void setRecipeCollection() {
        assertTrue(recipeCollection.isEmpty());
        Map<String, RecipeHistory> testCollection = new HashMap<>();
        testCollection.put("French loaf", recipeHistoryFrenchLoaf);
        testCollection.put("Pizza dough", recipeHistoryPizza);
        recipeCollection.setCollection(testCollection);
        assertEquals(testCollection, recipeCollection.getCollection());
    }

}
