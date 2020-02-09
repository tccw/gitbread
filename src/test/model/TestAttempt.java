package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class TestAttempt {
    private ArrayList<Ingredient> ingredientList = new ArrayList<>();
    private BreadRecipe hearthLoaf;
    private String instructions;
    private Attempt attempt;
    private LocalDateTime timeNow = LocalDateTime.now();

    @BeforeEach
    public void setUp() {
        ingredientList.add(new Ingredient("flour", 663));
        ingredientList.add(new Ingredient("water", 454));
        ingredientList.add(new Ingredient("sugar", 14));
        ingredientList.add(new Ingredient("salt", 14));
        ingredientList.add(new Ingredient("active-dry-yeast", 7));
        instructions = "1. Mix all ingredients 2. Knead dough until smooth 3. Let rise in oiled bowl for 1 hour " +
                        " 4. Knock back, shape, and let rise for 45 minutes on baking pan lightly covered " +
                        "5. Bake 30 minutes at 425F.";
        hearthLoaf = new BreadRecipe(ingredientList, instructions, 135, 30, 425);
        attempt = new Attempt(hearthLoaf);
        attempt.setDateTime(timeNow);
    }

    @Test
    public void TestAttemptConstructorWithoutParams() {
        BreadRecipe frenchLoaf = new BreadRecipe();
//        HelperCheckAttemptFields();
    }

    @Test
    public void TestAttemptConstructorWithParams() {
        HelperCheckAttemptFields(attempt, hearthLoaf, timeNow, "");
    }

    @Test
    public void TestAddSingleResultNotesToAttempt() {
        HelperCheckAttemptFields(attempt, hearthLoaf, timeNow, "");
        attempt.setResultNotes("open", "dark golden, crunchy", "tangy", "remove 5 mins earlier");
//        HelperCheckAttemptFields(attempt, hearthLoaf, timeNow, );
    }

    @Test
    public void TestAddMultipleResultNotesToAttempt() {
        //stub
    }

    @Test
    public void TestAddResultNotes() {
        //stub
    }

    //EFFECTS: checks that an attempt has the
    private void HelperCheckAttemptFields(Attempt attempt, Recipe recipe, LocalDateTime time, String resultNotes) {
        assertSame(recipe, attempt.getRecipeVersion());
        assertSame(time, attempt.getDateTime());
        assertEquals(resultNotes, attempt.getResultNotes());
    }
}