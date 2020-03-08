package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.Writer;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestRecipeDevCollection {

    RecipeDevCollection recipeCollection;
    RecipeDevHistory recipeHistoryFrenchLoaf;
    RecipeDevHistory recipeHistoryPizza;
    RecipeDevHistory recipeHistoryCinnamonRaisin;
    Recipe frenchLoaf = new BreadRecipe(1000);
    Recipe pizza = new BreadRecipe(350, 0.68);
    Recipe cinnamonRaisin = new BreadRecipe(800);

    @BeforeEach
    void setUp() {
        try {
            recipeCollection = new RecipeDevCollection();
            recipeHistoryFrenchLoaf = new RecipeDevHistory(frenchLoaf);
            recipeHistoryPizza = new RecipeDevHistory(pizza);
            recipeHistoryCinnamonRaisin = new RecipeDevHistory(cinnamonRaisin);
            recipeHistoryFrenchLoaf.commit(new BreadRecipe(500,0.76));
            recipeHistoryPizza.commit(new BreadRecipe(340, 0.65));
            //should have 3 historys, with 2,2, & 1 commits in their history all on the master branches
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Unexpected NoSuchAlgorithmException");
        }
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
        String expected = "Pizza dough : 0 attempts, 1 changes\n"
                + "French loaf : 0 attempts, 1 changes\n";
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
        try {
            assertEquals(1, recipeHistoryCinnamonRaisin.size());
            List<Commit> expected = new LinkedList<>();
            expected.add(new Commit(cinnamonRaisin, "master"));
            List<Commit> actual = recipeHistoryCinnamonRaisin.getCommits();
            assertEquals(expected.get(0).getBranchLabel(), actual.get(0).getBranchLabel());
            assertEquals(expected.get(0).getRecipeVersion().toString(), actual.get(0).getRecipeVersion().toString());
        } catch (NoSuchAlgorithmException e) {
            fail();
        }

    }

    @Test
    void TestGetCollection() {
        assertTrue(recipeCollection.isEmpty());
        recipeCollection.add("French loaf", recipeHistoryFrenchLoaf);
        recipeCollection.add("Pizza dough", recipeHistoryPizza);
        Map<String, RecipeDevHistory> testCollection = recipeCollection.getCollection();
        assertEquals(testCollection.get("French loaf"), recipeHistoryFrenchLoaf);
        assertEquals(testCollection.get("Pizza dough"), recipeHistoryPizza);
    }

    @Test
    void TestSave() {
        assertTrue(recipeCollection.isEmpty());
        recipeCollection.add("French loaf", recipeHistoryFrenchLoaf);
        recipeCollection.add("Pizza dough", recipeHistoryPizza);

        try {
            Writer writer = new Writer(new File("./data/recipecollections/recipeCollectionTest.json"));
            writer.write(recipeCollection);
            writer.close();
        } catch (IOException e) {
            fail("Unexpected IOException.");
        }
    }

    @Test
    void setRecipeCollection() {
        assertTrue(recipeCollection.isEmpty());
        Map<String, RecipeDevHistory> testCollection = new HashMap<>();
        testCollection.put("French loaf", recipeHistoryFrenchLoaf);
        testCollection.put("Pizza dough", recipeHistoryPizza);
        recipeCollection.setCollection(testCollection);
        assertEquals(testCollection, recipeCollection.getCollection());
    }

}
