package persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestReader {
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

            frenchLoaf = new BreadRecipe(1000);
            frenchLoafTesting = new BreadRecipe(650, 0.72);
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
    void TestLoadTwoRecipes() {
        try {
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
    void TestLoadRecipeCollectionJson() {
        try {
            RecipeDevCollection collection = Reader.loadRecipeCollectionJson(this.collection.toJson());
            assertEquals(this.collection.toString(true), collection.toString(true));
        } catch (IOException | BranchDoesNotExistException e) {
            fail();
        }
    }

    @Test
    void TestLoadRecipeHistoryJson() {
        try {
            NodeGraph history = Reader.loadRecipeDevHistoryJson(frenchLoafHist.toJson());
            assertEquals(frenchLoafHist.totalAttempts(), history.totalAttempts());
            assertEquals(frenchLoafHist.getCurrentBranch(), history.getCurrentBranch());
            assertEquals(frenchLoafHist.toJson(), history.toJson());
        } catch (JsonProcessingException | BranchDoesNotExistException e) {
            fail();
        }
    }

    @Test
    void TestDummyConstructor() {
        Reader reader = new Reader();
    }

    @Test
    void WriteComplexRead() {
        Writer writer;
        Reader reader;
        try {
            writer = new Writer(new File("./data/recipecollections/recipeNodeGraphTest.json"));
            RecipeDevCollection collection = new RecipeDevCollection();
            collection.add("Graph Recipe", HelperComplexGraph());
            writer.write(collection);
            writer.close();
            RecipeDevCollection readIn = Reader.loadRecipeCollectionFile(new File("./data/recipecollections/recipeNodeGraphTest.json"));
        } catch (IOException e) {
            fail("IOException");
        } catch (NoSuchAlgorithmException e) {
            fail("NoSuchAlgorithmException");
        } catch (BranchAlreadyExistsException e) {
            fail("BranchAlreadyExistsException");
        } catch (BranchDoesNotExistException e) {
            fail("BranchDoesNotExistException");
        }
    }

    private NodeGraph HelperComplexGraph() throws NoSuchAlgorithmException, BranchAlreadyExistsException, BranchDoesNotExistException {
        NodeGraph branchingGraph;
        branchingGraph = new NodeGraph(new BreadRecipe(1000, 1)); // to master
        branchingGraph.commit(new BreadRecipe(999, 0.99));        // to master
        branchingGraph.attempt(clock);
        branchingGraph.commit(new BreadRecipe(998, 0.98));        // to master
        branchingGraph.attempt(clock);
        branchingGraph.newBranch("test-bread");
        branchingGraph.commit(new BreadRecipe(899, 0.89));        // to test-bread
        branchingGraph.commit(new BreadRecipe(888, 0.88));        // to test-bread
        branchingGraph.attempt(clock);
        branchingGraph.newBranch("all-wheat");
        branchingGraph.commit(new BreadRecipe(799, 0.77));         // to all-wheat
        branchingGraph.commit(new BreadRecipe(788, 0.76));        // to all-wheat
        branchingGraph.attempt(clock);
        branchingGraph.attempt(clock);
        branchingGraph.checkout("test-bread");
        branchingGraph.commit(new BreadRecipe(877, 0.88));        // to test-bread
        branchingGraph.checkout("master");
        branchingGraph.commit(new BreadRecipe(699, 0.97));        // to master
        branchingGraph.commit(new BreadRecipe(688, 0.96));        // to master
        branchingGraph.attempt(clock);
        branchingGraph.commit(new BreadRecipe(677, 0.95));        // to master
        branchingGraph.merge("all-wheat");                                         // to master
        branchingGraph.commit(new BreadRecipe(599, 0.94));        // to master
        return branchingGraph;
    }
}




