package persistence;

import model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestReader {
    private static final String TEST_DIRECTORY = "./data/recipecollections/testWriter.json";
    private Clock clock = Clock.fixed(LocalDateTime.of(1989, 8,5,12,0)
            .toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
    private Writer testWriter;
    private RecipeDevCollection collection;
    BreadRecipe frenchLoaf;
    BreadRecipe frenchLoafTesting;
    BreadRecipe hearthLoaf;
    BreadRecipe hearthLoafTesting;
    RecipeDevHistory frenchLoafHist;
    RecipeDevHistory hearthLoafHist;

    @BeforeEach
    void setUp() {
        try {
            testWriter = new Writer(new File(TEST_DIRECTORY));

            frenchLoaf = new BreadRecipe(1000);
            frenchLoafTesting = new BreadRecipe(650, 0.72);
            hearthLoaf = new BreadRecipe(375, 0.58);
            hearthLoafTesting = new BreadRecipe(450, 0.72);
            frenchLoafHist = new RecipeDevHistory(frenchLoaf);
            hearthLoafHist = new RecipeDevHistory(hearthLoaf);
            frenchLoafHist.commit(frenchLoafTesting);
            hearthLoafHist.commit(hearthLoafTesting);

            collection = new RecipeDevCollection();
            collection.add("French loaf", frenchLoafHist);
            collection.add("Hearth loaf", hearthLoafHist);
            collection.get("French loaf").attempt(clock);
            collection.get("French loaf").attempt(clock);
        } catch (FileNotFoundException e) {
            fail("Unexpected FileNotFoundException");
        } catch (IOException e) {
            fail("Unexpected IOException in setUp()");
        } catch (NoSuchAlgorithmException e) {
            fail("Unexpected NoSuchAlgorithmException");
        }
    }

    @Test
    void TestLoadTwoRecipes() {
            try{
                testWriter.write(collection);
                testWriter.close();
                //Try reading them back
                RecipeDevCollection loadedCollection = Reader.loadRecipeCollectionFile(new File(TEST_DIRECTORY));
                assertEquals(frenchLoaf.toString(), loadedCollection.get("French loaf").getActiveCommit()
                        .getRecipeVersion().toString());
                assertEquals(frenchLoafTesting.toString(), collection.get("French loaf").getActiveCommit()
                        .getRecipeVersion().toString());
                assertEquals(hearthLoaf.toString(), loadedCollection.get("Hearth loaf").getActiveCommit()
                        .getRecipeVersion().toString());
                assertEquals(hearthLoafTesting.toString(), collection.get("Hearth loaf").getActiveCommit()
                        .getRecipeVersion().toString());
                assertEquals(2, loadedCollection.get("French loaf").totalAttempts());
                assertEquals(1, loadedCollection.get("French loaf").size() - 1);
                assertEquals(0, loadedCollection.get("Hearth loaf").totalAttempts());
                assertEquals(1, loadedCollection.get("Hearth loaf").size() - 1);
            } catch (IOException e) {
                fail("Unexpected IOException");
            }
        }

        @Test
    void TestDummyConstructor() {
            Reader reader = new Reader();
        }
}




