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
    Recipe frenchLoaf = new BreadRecipe(1000);
    Recipe pizza = new BreadRecipe(350, 0.68);
    Recipe cinnamonRaisin = new BreadRecipe(800);

    private String message;
    private File fileIn;
    private File fileOut;
    private Steganos encoder;

    @BeforeEach
    void setUp() {
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
        fileIn = new File("./data/icons/collectionsharing/collectionsharingbynikitagolubev.png");
        fileOut = new File("./data/icons/collectionsharing/sharing/testCollection.png");
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
        encoder.encode(message, fileOut);
        String out = encoder.decode(fileOut);
        assertEquals(recipeCollection.toJson(), out);
    }
}
