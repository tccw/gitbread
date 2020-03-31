package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tk.plogitech.darksky.forecast.APIKey;
import tk.plogitech.darksky.forecast.ForecastRequest;
import tk.plogitech.darksky.forecast.ForecastRequestBuilder;
import tk.plogitech.darksky.forecast.GeoCoordinates;
import tk.plogitech.darksky.forecast.model.Latitude;
import tk.plogitech.darksky.forecast.model.Longitude;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TestAttempt {
    private BreadRecipe frenchLoaf;
    private Attempt attempt;
    private Clock clock = Clock.fixed(LocalDateTime.of(2020, 2,14,12,10)
            .toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
    private LocalDateTime timeNow = LocalDateTime.now(clock);
    private String path = "res\\value\\api_keys.txt";

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
        attempt.setResultNotes("open",
                "dark golden, crunchy",
                "tangy",
                "remove 5 mins earlier");
        String expected = "Crumb: open\n" +
                          "Crust: dark golden, crunchy\n" +
                          "Flavor: tangy\n" +
                          "Other notes: remove 5 mins earlier\n";
        HelperCheckAttemptFields(attempt, frenchLoaf, timeNow, expected);
    }

    @Test
    public void TestNoPhoto() {
        assertFalse(attempt.hasPhoto());
    }

    @Test
    void TestHasPhoto() {
        attempt.setPhotoPath(new File("file:./data/somepaththatdoesntexist.png"));
        assertTrue(attempt.hasPhoto());
    }

    @Test
    void TestPrint() {
        String expected = "\n" +
                "----------- Friday, February 14, 2020 - 12:10 PM -----------\n" +
                "ATTEMPT NOTES :\n" +
                "\n" +
                "Ingredients: \n" +
                "   - Flour, 326g\n" +
                "   - Water, 215g\n" +
                "   - Salt, 6g\n" +
                "   - Yeast, 1g\n" +
                "\n" +
                "Hydration: 66%\n" +
                "Prep: 2 hr 15 min\n" +
                "Bake: 0 hr 30 min\n" +
                "Total: 2 hr 45 min\n" +
                "Bake temp: 425 F\n" +
                "Baking vessel: pan\n" +
                "Yield: 550 g\n" +
                "\n" +
                "Instructions:\n" +
                "    1. Mix all ingredients \n" +
                "    2. Knead dough until smooth \n" +
                "    3. Let rise in oiled bowl for 1 hour \n" +
                "    4. Knock back, shape, and let rise for 45 minutes on baking pan lightly covered \n" +
                "    5. Bake 30 minutes at 425F\n";
        String actual = attempt.print();
        assertEquals(expected, actual);
    }


    //EFFECTS: checks that an attempt has the
    private void HelperCheckAttemptFields(Attempt attempt, Recipe recipe, LocalDateTime timeNow, String resultNotes) {
        assertSame(recipe, attempt.getRecipeVersion());
        assertTrue(timeNow.isEqual(attempt.getDateTime()));
        assertEquals(resultNotes, attempt.getResultNotes());
    }

    private void HelperGetWeather() throws IOException {
        ArrayList<String> apiKey = new ArrayList<>(Files.readAllLines(Paths.get(path)));

        ForecastRequest request = new ForecastRequestBuilder()
                .key(new APIKey(apiKey.get(0)))
                .language(ForecastRequestBuilder.Language.en)
                .location(new GeoCoordinates(new Longitude(-123.190460), new Latitude(49.264585))).build();
    }

}