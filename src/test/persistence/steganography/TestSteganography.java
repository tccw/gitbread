package persistence.steganography;

import com.fasterxml.jackson.core.JsonProcessingException;
import model.BreadRecipe;
import model.Recipe;
import model.RecipeDevCollection;
import model.RecipeDevHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.AlertMessage;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

public class TestSteganography {
    RecipeDevCollection recipeCollection;
    RecipeDevHistory recipeHistoryFrenchLoaf;
    RecipeDevHistory recipeHistoryPizza;
    RecipeDevHistory recipeHistoryCinnamonRaisin;
    Recipe frenchLoaf;
    Recipe pizza;
    Recipe cinnamonRaisin;

    private String message;
    private File fileIn;
    private File fileOut;
    private Steganos encoder;

    @BeforeEach
    void setUp() {

        try {
            frenchLoaf = new BreadRecipe(1000);
            pizza = new BreadRecipe(350, 0.68);
            cinnamonRaisin = new BreadRecipe(800);
            cinnamonRaisin.setInstructions("1. Mix it up 2. Bake it down 3. funky town");
            recipeCollection = new RecipeDevCollection();
            recipeHistoryFrenchLoaf = new RecipeDevHistory(frenchLoaf);
            recipeHistoryPizza = new RecipeDevHistory(pizza);
            recipeHistoryCinnamonRaisin = new RecipeDevHistory(cinnamonRaisin);
            recipeHistoryPizza.commit(new BreadRecipe(350, 0.58));
            recipeHistoryPizza.commit(new BreadRecipe(350, 0.64));
            recipeCollection.add("French loaf", recipeHistoryFrenchLoaf);
            recipeCollection.add("Pizza", recipeHistoryPizza);
            recipeCollection.add("Cinnamon Raisin", recipeHistoryCinnamonRaisin);
            message = recipeCollection.toJson();
            fileIn = new File("./data/icons/sharing/collectionsharingbynikitagolubev.png");
            fileOut = new File("./data/icons/sharing/exported/testCollection.png");
            encoder = new Steganos();
        } catch (NoSuchAlgorithmException | JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    @Test
    void TestConstructor() {
        assertNull(encoder.getImage());
        assertNull(encoder.getEncodedPixels());
        assertNull(encoder.getOriginalPixels());
        assertNull(encoder.getByteMessage());
        assertNull(encoder.getInputStream());
    }

    @Test
    void TestEncode() {

        try {
            encoder.encode(message, fileIn);
            encoder.save(fileOut);
        } catch (IOException e) {
            fail("Unexpected IOException.");
        }
        assertNotNull(encoder.getEncodedPixels());
    }

    //TODO: determine why this test changes every time I run it.
    @Test
    void TestDecode() {
        try {
            encoder.encode(message, fileIn);
            String out = encoder.decode(fileOut);
            assertEquals(recipeCollection.toJson(), out);
        } catch (IOException e) {
            fail();
        }
    }
}
