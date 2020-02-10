package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import static org.junit.jupiter.api.Assertions.*;

class TestAttempt {
    private BreadRecipe frenchLoaf;
    private Attempt attempt;
    Clock clock = Clock.fixed(LocalDateTime.of(2020, 2,14,12,10)
            .toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
    private LocalDateTime timeNow = LocalDateTime.now(clock);

    @BeforeEach
    public void setUp() {
        frenchLoaf = new BreadRecipe(550);
        attempt = new Attempt(frenchLoaf, clock);
    }

    @Test
    public void TestAttemptConstructorWithParams() {
        HelperCheckAttemptFields(attempt, frenchLoaf, timeNow, "");
    }

    @Test
    public void TestAddSingleResultNotesToAttempt() {
        HelperCheckAttemptFields(attempt, frenchLoaf, timeNow, "");
        attempt.setResultNotes("open", "dark golden, crunchy", "tangy", "remove 5 mins earlier");
        String expected = "Crumb: open, springy\n" +
                          "Crust: golden, crunchy\n" +
                          "Flavor: perfect\n" +
                          "Other notes: remove 5 mins earlier\n";
        HelperCheckAttemptFields(attempt, frenchLoaf, timeNow, expected);
    }

    //EFFECTS: checks that an attempt has the
    private void HelperCheckAttemptFields(Attempt attempt, Recipe recipe, LocalDateTime timeNow, String resultNotes) {
        assertSame(recipe, attempt.getRecipeVersion());
        assertTrue(timeNow.isEqual(attempt.getDateTime()));
        assertEquals(resultNotes, attempt.getResultNotes());

    }
}