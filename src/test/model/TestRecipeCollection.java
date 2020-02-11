package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestRecipeCollection {

    RecipeCollection recipeCollection;
    RecipeHistory recipeHistoryFrenchLoaf;
    RecipeHistory recipeHistoryPizza;
    RecipeHistory recipeHistoryCinnamonRaisin;
    Recipe frenchLoaf = new BreadRecipe(1000);
    Recipe pizza = pizza = new BreadRecipe(350, 0.68);
    Recipe cinnamonRaisin = new BreadRecipe(800);

    @BeforeEach
    public void setUp() {
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
    public void TestConstructor() {
        assertEquals(0, recipeCollection.size());
    }

    @Test
    public void TestAddSingle() {
        assertEquals(0, recipeCollection.size());
        recipeCollection.add("French Loaf", recipeHistoryFrenchLoaf);
        assertEquals(1, recipeCollection.size());
    }

    @Test
    public void TestAddMultiple() {
        assertEquals(0, recipeCollection.size());
        recipeCollection.add("French loaf", recipeHistoryFrenchLoaf);
        recipeCollection.add("Pizza", recipeHistoryPizza);
        recipeCollection.add("Cinnamon Raisin", recipeHistoryCinnamonRaisin);
        assertEquals(3, recipeCollection.size());
    }

    @Test
    public void TestAddSingleRemoveSingle() {
        assertEquals(0, recipeCollection.size());
        recipeCollection.add("French loaf", recipeHistoryFrenchLoaf);
        recipeCollection.remove("French loaf");
        assertEquals(0, recipeCollection.size());
    }

    @Test
    public void TestAddRemoveAddMultiple() {
        assertEquals(0, recipeCollection.size());
        recipeCollection.add("French loaf", recipeHistoryFrenchLoaf);
        recipeCollection.add("Pizza", recipeHistoryPizza);
        recipeCollection.add("Cinnamon Raisin", recipeHistoryCinnamonRaisin);
        assertEquals(3, recipeCollection.size());
        recipeCollection.remove("Cinnamon Raisin");
        recipeCollection.remove("French loaf");
        assertEquals(1, recipeCollection.size());
    }

    @Test
    public void TestToString() {
        assertEquals(0, recipeCollection.size());
        recipeCollection.add("French loaf", recipeHistoryFrenchLoaf);
        recipeCollection.add("Pizza dough", recipeHistoryPizza);
        String expected = "Pizza dough - 0 attempts, 2 changes\n"
                + "French loaf - 0 attempts, 0 changes\n";
        assertEquals(expected, recipeCollection.toString());
    }

}
