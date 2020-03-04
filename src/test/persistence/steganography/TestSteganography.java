package persistence.steganography;

import model.BreadRecipe;
import model.Recipe;
import model.RecipeCollection;
import model.RecipeHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class TestSteganography {
    RecipeCollection recipeCollection;
    RecipeHistory recipeHistoryFrenchLoaf;
    RecipeHistory recipeHistoryPizza;
    RecipeHistory recipeHistoryCinnamonRaisin;
    Recipe frenchLoaf;
    Recipe pizza;
    Recipe cinnamonRaisin;

    private String message;
    private File fileIn;
    private File fileOut;
    private Steganos encoder;

    @BeforeEach
    void setUp() {
        frenchLoaf = new BreadRecipe(1000);
        pizza = new BreadRecipe(350, 0.68);
        cinnamonRaisin = new BreadRecipe(800);
        cinnamonRaisin.setInstructions("1. Mix it up 2. Bake it down 3. funky town");
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
        recipeCollection.add("French loaf", recipeHistoryFrenchLoaf);
        recipeCollection.add("Pizza", recipeHistoryPizza);
        recipeCollection.add("Cinnamon Raisin", recipeHistoryCinnamonRaisin);

        message = recipeCollection.toJson();
        fileIn = new File("./data/icons/sharing/collectionsharingbynikitagolubev.png");
        fileOut = new File("./data/icons/sharing/exported/testCollection.png");
        encoder = new Steganos();
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
