package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestRecipeHistory {

    private RecipeHistory recipeHistoryNull;
    private RecipeHistory recipeHistoryNotNull;
    private Recipe frenchLoaf = new BreadRecipe(1000);
    private Recipe pizza = new BreadRecipe(350, 0.68);
    private Recipe cinnamonRaisin = new BreadRecipe(800);
    private Clock clock = Clock.fixed(LocalDateTime.of(2020, 2, 14, 12, 10)
            .toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));


    @BeforeEach
    public void setUp() {
        recipeHistoryNull = new RecipeHistory();
        recipeHistoryNotNull = new RecipeHistory(cinnamonRaisin);

    }

    @Test
    public void TestConstructor() {
        HelperVerifyConstructor(recipeHistoryNull);
        assertSame(cinnamonRaisin, recipeHistoryNotNull.getActiveVersion());
        assertSame(cinnamonRaisin, recipeHistoryNotNull.get(0));
    }

    @Test
    public void TestAddToHistorySingle() {
        HelperVerifyConstructor(recipeHistoryNull);
        recipeHistoryNull.setActiveVersion(pizza);
        recipeHistoryNull.addToHistory(pizza);
        assertEquals(1, recipeHistoryNull.size());
        assertEquals(pizza, recipeHistoryNull.getActiveVersion());
    }

    @Test
    public void TestAddToHistoryModifiedMulti() {
        HelperVerifyConstructor(recipeHistoryNull);
        recipeHistoryNull.setActiveVersion(frenchLoaf);
        recipeHistoryNull.addToHistory(new BreadRecipe(800, 0.55));
        recipeHistoryNull.addToHistory(new BreadRecipe(1000));
        recipeHistoryNull.addToHistory(new BreadRecipe(3031));
        recipeHistoryNull.addToHistory(pizza);
        assertEquals(4, recipeHistoryNull.size());
        assertEquals(3, recipeHistoryNull.countTimesModified());
        assertSame(pizza, recipeHistoryNull.get(3));
    }

    @Test
    public void TestAddToHistoryMultiVerifyHistoryArray() {
        List<Recipe> expectedHistory = new LinkedList<Recipe>(Arrays.asList(pizza,
                frenchLoaf, cinnamonRaisin));
        List<Recipe> history = recipeHistoryNull.getHistory();
        recipeHistoryNull.setActiveVersion(pizza);
        recipeHistoryNull.addToHistory(pizza);
        recipeHistoryNull.addToHistory(frenchLoaf);
        recipeHistoryNull.addToHistory(cinnamonRaisin);
        for (int i = 0; i < expectedHistory.size(); i++) {
            assertSame(expectedHistory.get(i), history.get(i));
        }
    }
    //TODO add recipes and change testing and master recipe references
    //TODO test viewAttempts()
    //TODO test viewRecipeVersionList()

    @Test
    public void TestMultiAddHistoryMasterTesting() {
        HelperVerifyConstructor(recipeHistoryNull);

        recipeHistoryNull.setActiveVersion(pizza);
        recipeHistoryNull.addToHistory(pizza);
        recipeHistoryNull.addToHistory(new BreadRecipe(350, 0.58));
        recipeHistoryNull.addToHistory(new BreadRecipe(350, 0.64));
        recipeHistoryNull.setActiveVersion(recipeHistoryNull.get(1));
        recipeHistoryNull.attempt(recipeHistoryNull.getActiveVersion(), clock);
        recipeHistoryNull.setTestingRecipe(recipeHistoryNull.get(2));

        assertEquals(3, recipeHistoryNull.size());
        assertSame(recipeHistoryNull.getActiveVersion(), recipeHistoryNull.get(1));
        assertSame(recipeHistoryNull.getTestingRecipe(), recipeHistoryNull.get(2));
        assertEquals(1, recipeHistoryNull.countAttempts());
    }

    private void HelperVerifyConstructor(RecipeHistory h) {
        assertSame(null, h.getActiveVersion());
        assertSame(null, h.getTestingRecipe());
        assertEquals(0, h.size());
    }


}
