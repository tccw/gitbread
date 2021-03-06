package persistence;

import exceptions.BranchAlreadyExistsException;
import exceptions.BranchDoesNotExistException;
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

import static org.junit.jupiter.api.Assertions.*;

class TestWriter {
    private static final String TEST_DIRECTORY = "./data/recipecollections/testWriter.json";
    private Clock clock = Clock.fixed(LocalDateTime.of(1989, 8, 5, 12, 0)
            .toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
    private Writer testWriter;
    private RecipeDevCollection collection;
    BreadRecipe frenchLoaf;
    BreadRecipe frenchLoafTesting;
    BreadRecipe hearthLoaf;
    BreadRecipe hearthLoafTesting;
    NodeGraph frenchLoafHist;
    NodeGraph hearthLoafHist;

    @BeforeEach
    void setUp() {
        try {
            testWriter = new Writer(new File(TEST_DIRECTORY));

            frenchLoaf = new BreadRecipe(650, 0.72);
            frenchLoafTesting =  new BreadRecipe(1000);
            hearthLoaf = new BreadRecipe(375, 0.58);
            hearthLoafTesting = new BreadRecipe(450, 0.72);
            frenchLoafHist = new NodeGraph(frenchLoaf);
            hearthLoafHist = new NodeGraph(hearthLoaf);
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
    void TestWriteExpectedIOException() {
        try {
            Writer writer = new Writer(new File("data/persistence/folderThatDoesntExist/test.json"));
            writer.write(new RecipeDevCollection());
            fail("Should have thrown an exception.");
        } catch (IOException e){
            // do nothing as this is expected
        }
    }

    @Test
    void TestWrite() {
        try{
            testWriter.write(collection);
            testWriter.close();
            //Try reading them back
            RecipeDevCollection loadedCollection = Reader.loadRecipeCollectionFile(new File(TEST_DIRECTORY));
            assertEquals(frenchLoafTesting.toString(), collection.get("French loaf").getActiveNode()
                    .getRecipeVersion().toString());
            assertEquals(hearthLoafTesting.toString(), collection.get("Hearth loaf").getActiveNode()
                    .getRecipeVersion().toString());
            assertEquals(2, loadedCollection.get("French loaf").totalAttempts());
            assertEquals(1, loadedCollection.get("French loaf").size() - 1);
            assertEquals(0, loadedCollection.get("Hearth loaf").totalAttempts());
            assertEquals(1, loadedCollection.get("Hearth loaf").size() - 1);
        } catch (IOException e) {
            fail("Unexpected IOException");
        } catch (BranchDoesNotExistException e) {
            fail();
        }
    }

    @Test
    void TestWriteComplex() {
        try {
            RecipeDevCollection repo = new RecipeDevCollection();
            NodeGraph recipeVersionHistory;
            recipeVersionHistory = new NodeGraph(new BreadRecipe(1000));
            recipeVersionHistory.commit(new BreadRecipe(1000, 0.60));
            recipeVersionHistory.commit(new BreadRecipe(1000, 0.59));
            recipeVersionHistory.newBranch("high-hydration-test");
            recipeVersionHistory.commit(new BreadRecipe(360, 0.78));
            recipeVersionHistory.commit(new BreadRecipe(360, 0.79));
            recipeVersionHistory.checkout("master");
            recipeVersionHistory.commit(new BreadRecipe(1000, 0.58));
            recipeVersionHistory.checkout("high-hydration-test");
            recipeVersionHistory.commit(new BreadRecipe(1000, 0.81));
            recipeVersionHistory.commit(new BreadRecipe(600, 0.51));
            recipeVersionHistory.newBranch("high-temp");
            recipeVersionHistory.commit(new BreadRecipe(1000,0.76));
            recipeVersionHistory.commit(new BreadRecipe(1000,0.72));
            recipeVersionHistory.checkout("high-hydration-test");
            recipeVersionHistory.merge("master");
            recipeVersionHistory.commit(new BreadRecipe(650, 0.45));
            repo.add("Saved Test Recipe", recipeVersionHistory);
            Writer writer = new Writer(new File("./data/recipecollections/complexHistory.json"));
            writer.write(repo);
            writer.close();
        } catch (Exception e) {
            fail();
        }
    }

}