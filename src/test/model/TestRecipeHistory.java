package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

public class TestRecipeHistory {

    private RecipeHistory recipeHistory;
    private Recipe frenchLoaf = new BreadRecipe(1000);
    private Recipe pizza = new BreadRecipe(350, 0.68);
    private Recipe cinnamonRaisin = new BreadRecipe(800);
    private Clock clock = Clock.fixed(LocalDateTime.of(2020, 2, 14, 12, 10)
            .toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));


    @BeforeEach
    public void setUp() {
        recipeHistory = new RecipeHistory();
    }

    @Test
    public void TestConstructor() {
        HelperVerifyConstructor(recipeHistory);
    }

    @Test
    public void TestAddToHistorySingle() {
        HelperVerifyConstructor(recipeHistory);
        recipeHistory.setMasterRecipe(pizza);
        recipeHistory.addToHistory(pizza);
        assertEquals(1, recipeHistory.size());
        assertEquals(pizza, recipeHistory.getMasterRecipe());
    }

    @Test
    public void TestMultiAddHistoryMasterTesting() {
        HelperVerifyConstructor(recipeHistory);
        recipeHistory.setMasterRecipe(pizza);
        recipeHistory.addToHistory(pizza);
        recipeHistory.addToHistory(new BreadRecipe(350, 0.58));
        recipeHistory.addToHistory(new BreadRecipe(350, 0.64));
        recipeHistory.setMasterRecipe(recipeHistory.get(1));
        recipeHistory.attempt(recipeHistory.getMasterRecipe(), clock);
        recipeHistory.setTestingRecipe(recipeHistory.get(2));
        assertEquals(3, recipeHistory.size());
        assertSame(recipeHistory.getMasterRecipe(), recipeHistory.get(1));
        assertSame(recipeHistory.getTestingRecipe(), recipeHistory.get(2));
        assertEquals(1, recipeHistory.countAttempts());
    }

    //TODO add multiple recipes to the history
    @Test
    public void TestAddToHistoryMulti() {
        HelperVerifyConstructor(recipeHistory);
        recipeHistory.setMasterRecipe(frenchLoaf);
        recipeHistory.addToHistory(frenchLoaf);

        recipeHistory.setMasterRecipe(cinnamonRaisin);
        recipeHistory.addToHistory(cinnamonRaisin);
        recipeHistory.addToHistory(new BreadRecipe(800, 0.55));
    }
    //TODO add recipes and change testing and master recipe references
    //TODO test viewAttempts()
    //TODO test viewRecipeVersionList()

    private void HelperVerifyConstructor(RecipeHistory h) {
        assertSame(null, h.getMasterRecipe());
        assertSame(null, h.getTestingRecipe());
        assertEquals(0, h.size());
    }


}
