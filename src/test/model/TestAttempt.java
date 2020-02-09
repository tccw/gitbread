package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TestAttempt {
    private ArrayList<Ingredient> ingredientList = new ArrayList<>();
    private BreadRecipe hearthLoaf;
    private Attempt attempt;
    private LocalDateTime timeNow = LocalDateTime.now();

    @BeforeEach
    public void setUp() {
        ingredientList.add(new Ingredient("flour", 663));
        ingredientList.add(new Ingredient("water", 454));
        hearthLoaf = new BreadRecipe();
        attempt = new Attempt(hearthLoaf);
        attempt.setDateTime(timeNow);
    }

    @Test
    public void TestAttemptConstructor() {
        // stub
    }

    @Test
    public void TestAddResultNotesToAttempt() {
        //stub
    }

    @Test
    public void TestAddResultNotes() {
        //stub
    }

    //EFFECTS: checks that an attempt has the
    private void HelperCheckAttemptFields(Recipe recipe, LocalDateTime time, String resultNotes, String weather) {
        // stub
    }
}