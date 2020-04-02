package model;

import exceptions.BranchAlreadyExistsException;
import exceptions.BranchDoesNotExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.Reader;
import persistence.Writer;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestNodeGraph {
    private Clock clock = Clock.fixed(LocalDateTime.of(2020, 2, 14, 12, 10)
            .toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
    NodeGraph graph;
    BreadRecipe initialRecipe;
    BreadRecipe lastRecipe;

    @BeforeEach
    void setUp() {
        try {
            initialRecipe = new BreadRecipe(500, 0.99);
            graph = new NodeGraph(initialRecipe);
            for (double i = 0.2; i < 0.3; i = i + 0.02) {
                graph.commit(new BreadRecipe(1000, 1 - i));
            }
            lastRecipe = new BreadRecipe(1000, 0.5);
            graph.newBranch("new-branch");
            graph.commit(new BreadRecipe(1200, .3));
            graph.commit(new BreadRecipe(1200, 0.31));
            graph.commit(new BreadRecipe(1200, 0.32));
            graph.checkout("master");
            graph.commit(new BreadRecipe(1000, 0.8));
        } catch (NoSuchAlgorithmException | BranchDoesNotExistException | BranchAlreadyExistsException e) {
            fail();
        }
    }

    @Test
    void TestGetHistory() {
        try {
            List<Node> historyMaster = graph.getBranchHistory("master");
            List<Node> historyNewBranch = graph.getBranchHistory("new-branch");
            assertEquals(2, graph.getBranches().size());
            assertEquals(7, historyMaster.size());
        } catch (BranchDoesNotExistException e) {
            fail();
        }
    }

    /*
          m
          |
          m
          |
          m
          | \
          m  t
          |  |
          m  t
          |  | \
          m  t  a
          |     |
          |     a
          |   /
          | /
          m
          |
          m
   */
    @Test
    void TestBranchingMerging() {
        NodeGraph branchingGraph;
        try {
            branchingGraph = HelperComplexGraph();
            List<Node> testBreadPath = branchingGraph.getBranchHistory("test-bread");
            List<Node> allWheatPath = branchingGraph.getBranchHistory("all-wheat");
            List<Node> masterPath = branchingGraph.getBranchHistory("master");
            assertEquals(6, testBreadPath.size());
            assertEquals(7, allWheatPath.size());
            assertEquals(8, masterPath.size());
        } catch (NoSuchAlgorithmException | BranchDoesNotExistException | BranchAlreadyExistsException e) {
            fail();
        }

    }


    @Test
    void TestBranchDoesNotExistCheckout() {
        try {
            graph.checkout("branch-does-not-exist");
            fail();
        } catch (BranchDoesNotExistException e) {
            // expected result
        }
    }

    @Test
    void TestBranchCheckout() {
        try {
            graph.checkout("new-branch");
            assertEquals("new-branch", graph.getCurrentBranch());
        } catch (BranchDoesNotExistException e) {
            fail();
        }
    }

    @Test
    void TestBranchAlreadyExistsNewBranch() {
        try {
            graph.newBranch("master");
            fail();
        } catch (BranchAlreadyExistsException e) {
            // expected result
        }
    }

    @Test
    void TestCreateNewBranchNoCommit() {
        try {
            graph.newBranch("butter-biscuits");
            assertEquals(2, graph.getMostRecentNodesByBranch().size());
            assertEquals("butter-biscuits", graph.getCurrentBranch());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void TestBranchHistoryNonexistentBranch() {
        try {
            graph.getBranchHistory("branch-does-not-exist");
            fail();
        } catch (BranchDoesNotExistException e) {
            // expected result
        }
    }

    @Test
    void TestMergeBranchDoesNotExist() {
        try {
            graph.merge("branch-does-not-exist");
            fail();
        } catch (BranchDoesNotExistException e) {
            // expected result
        } catch (NoSuchAlgorithmException e) {
            fail();
        }
    }

    @Test
    void TestOneAttempt() {
        graph.attempt(clock);
        assertEquals(1, graph.totalAttempts());
    }

    @Test
    void TestMultipleAttempt() {
        NodeGraph complexGraph;
        try {
            complexGraph = HelperComplexGraph();
            assertEquals(6, complexGraph.totalAttempts());
            assertEquals(13, complexGraph.size());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void TestGetNodeHistory() {
        try {
            List<Node> fullBranchHistory = graph.getBranchHistory("master");
            List<Node> partialBranchHistory = graph.getNodeHistory(fullBranchHistory.get(3));
            List<Node> root = graph.getNodeHistory(fullBranchHistory.get(0));
            assertEquals(7, fullBranchHistory.size());
            assertEquals(4, partialBranchHistory.size());
            assertNull(root);
        } catch (BranchDoesNotExistException e) {
            fail();
        }
    }

    @Test
    void TestGetNodeOfAttempt() {
        try{
            NodeGraph complexGraph = HelperComplexGraph();
            List<Attempt> attempts = complexGraph.getAttempts();
            Attempt attempt = attempts.get(0);
            Node node = complexGraph.getNodeOfAttempt(attempt);
            assertEquals(688, node.getRecipeVersion().getIngredientWeight("flour"));
        } catch (NoSuchAlgorithmException e) {
            fail("NoSuchAlgorithmException");
        } catch (BranchAlreadyExistsException e) {
            fail("BranchAlreadyExistsException");
        } catch (BranchDoesNotExistException e) {
            fail("BranchDoesNotExistException");
        }
    }

    @Test
    void TestSave() {
        Writer writer;
        try {
            writer = new Writer(new File("./data/recipecollections/recipeNodeGraphTest.json"));
            writer.write(HelperComplexGraph());
            writer.close();
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


    @Test
    void TestDummyConstructor() {
        NodeGraph dummy = new NodeGraph();
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
