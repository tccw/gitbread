package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
    public void TestAddToHistoryModifiedMulti() {
        HelperVerifyConstructor(recipeHistory);
        recipeHistory.setMasterRecipe(frenchLoaf);
        recipeHistory.addToHistory(new BreadRecipe(800, 0.55));
        recipeHistory.addToHistory(new BreadRecipe(1000));
        recipeHistory.addToHistory(new BreadRecipe(3031));
        recipeHistory.addToHistory(pizza);
        assertEquals(4, recipeHistory.size());
        assertEquals(3, recipeHistory.countTimesModified());
        assertSame(pizza, recipeHistory.get(3));
    }

    @Test
    public void TestAddToHistoryMultiVerifyHistoryArray() {
        List<Recipe> expectedHistory = new LinkedList<Recipe>(Arrays.asList(pizza,
                frenchLoaf, cinnamonRaisin));
        List<Recipe> history = recipeHistory.getHistory();
        recipeHistory.setMasterRecipe(pizza);
        recipeHistory.addToHistory(pizza);
        recipeHistory.addToHistory(frenchLoaf);
        recipeHistory.addToHistory(cinnamonRaisin);
        for (int i = 0; i < expectedHistory.size(); i++) {
            assertSame(expectedHistory.get(i), history.get(i));
        }
    }
    //TODO add recipes and change testing and master recipe references
    //TODO test viewAttempts()
    //TODO test viewRecipeVersionList()

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

    private void HelperVerifyConstructor(RecipeHistory h) {
        assertSame(null, h.getMasterRecipe());
        assertSame(null, h.getTestingRecipe());
        assertEquals(0, h.size());
    }


}
