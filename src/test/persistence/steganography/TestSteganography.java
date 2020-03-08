package persistence.steganography;

import model.BreadRecipe;
import model.Recipe;
import model.RecipeDevCollection;
import model.RecipeDevHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        } catch (NoSuchAlgorithmException e) {
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
        encoder.encode(message, fileIn);
        try {
            encoder.save(fileOut);
        } catch (IOException e) {
            fail("Unexpected IOException.");
        }
        assertNotNull(encoder.getEncodedPixels());
    }

    @Test
    void TestDecode() {
        encoder.encode(message, fileIn);
        String out = encoder.decode(fileOut);
        assertEquals(recipeCollection.toJson(), out);
    }
}
