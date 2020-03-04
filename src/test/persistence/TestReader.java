package persistence;

import model.BreadRecipe;

import model.RecipeCollection;
import model.RecipeHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestReader {
    private static final String TEST_DIRECTORY = "./data/recipecollections/testWriter.json";
    private Clock clock = Clock.fixed(LocalDateTime.of(1989, 8,5,12,0)
            .toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
    private Writer testWriter;
    private RecipeCollection collection;
    BreadRecipe frenchLoaf;
    BreadRecipe frenchLoafTesting;
    BreadRecipe hearthLoaf;
    BreadRecipe hearthLoafTesting;
    RecipeHistory frenchLoafHist;
    RecipeHistory hearthLoafHist;

    @BeforeEach
    void setUp() {
        try {
            testWriter = new Writer(new File(TEST_DIRECTORY));

            frenchLoaf = new BreadRecipe(1000);
            frenchLoafTesting = new BreadRecipe(650, 0.72);
            hearthLoaf = new BreadRecipe(375, 0.58);
            hearthLoafTesting = new BreadRecipe(450, 0.72);

            frenchLoafHist = new RecipeHistory(frenchLoaf);
            hearthLoafHist = new RecipeHistory(hearthLoaf);

            frenchLoafHist.setMasterRecipe(frenchLoaf);
            frenchLoafHist.addToHistory(frenchLoafTesting);
            frenchLoafHist.setTestingRecipe(frenchLoafHist.get(1));
            hearthLoafHist.setMasterRecipe(hearthLoaf);
            hearthLoafHist.addToHistory(hearthLoafTesting);
            hearthLoafHist.setTestingRecipe(hearthLoafHist.get(1));

            collection = new RecipeCollection();
            collection.add("French loaf", frenchLoafHist);
            collection.add("Hearth loaf", hearthLoafHist);
            collection.get("French loaf").attempt(collection.get("French loaf").getMasterRecipe(), clock);
            collection.get("French loaf").attempt(collection.get("French loaf").getTestingRecipe(), clock);
        } catch (FileNotFoundException e) {
            System.out.println("Unexpected FileNotFoundException");
        } catch (IOException e) {
            System.out.println("Unexpected IOException in setUp()");
        }
    }

    @Test
    void TestLoadTwoRecipes() {
            try{
                testWriter.write(collection);
                testWriter.close();
                //Try reading them back
                RecipeCollection loadedCollection = Reader.loadRecipeCollectionFile(new File(TEST_DIRECTORY));
                assertEquals(frenchLoaf.toString(), loadedCollection.get("French loaf").getMasterRecipe().toString());
                assertEquals(frenchLoafTesting.toString(), collection.get("French loaf").getTestingRecipe().toString());
                assertEquals(hearthLoaf.toString(), loadedCollection.get("Hearth loaf").getMasterRecipe().toString());
                assertEquals(hearthLoafTesting.toString(), collection.get("Hearth loaf").getTestingRecipe().toString());
                assertEquals(2, loadedCollection.get("French loaf").countAttempts());
                assertEquals(1, loadedCollection.get("French loaf").countTimesModified());
                assertEquals(0, loadedCollection.get("Hearth loaf").countAttempts());
                assertEquals(1, loadedCollection.get("Hearth loaf").countTimesModified());
            } catch (IOException e) {
                System.out.println("Unexpected IOException");
            }
        }

        @Test
    void TestDummyConstructor() {
            Reader reader = new Reader();
        }
}




