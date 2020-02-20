package persistence;

import model.BreadRecipe;
import model.RecipeCollection;
import model.RecipeHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TestWriter {
    private static final String TEST_DIRECTORY = "data/recipecollections/testWriter.json";
    Writer testWriter;

    @BeforeEach
    void setUp() {
        try {
            testWriter = new Writer(new File(TEST_DIRECTORY));

            BreadRecipe frenchLoaf = new BreadRecipe(1000);
            BreadRecipe hearthLoaf = new BreadRecipe(375, 0.58);
            RecipeHistory frenchLoafHist = new RecipeHistory(frenchLoaf);
            RecipeHistory hearthLoafHist = new RecipeHistory(hearthLoaf);
            frenchLoafHist.setMasterRecipe(frenchLoaf);
            frenchLoafHist.setTestingRecipe(new BreadRecipe(650, 0.72));
            hearthLoafHist.setMasterRecipe(frenchLoaf);
            frenchLoafHist.setTestingRecipe(new BreadRecipe(650, 0.72));
            RecipeCollection collection = new RecipeCollection();
            collection.add("French loaf", frenchLoafHist);
            collection.add("Hearth loaf", hearthLoafHist);
        } catch (FileNotFoundException e) {
            System.out.println("Unexpected FileNotFoundException");
        } catch (IOException e) {
            System.out.println("Unexpected IOException in setUp()");
        }
    }

    @Test
    void TestWrite() {
    // need the load class working in order to run this
    }
}