package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestNodeGraph {
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
            lastRecipe = new BreadRecipe(1000,0.5);
        } catch (NoSuchAlgorithmException e) {
            fail();
        }
    }

    @Test
    void TestGetHistory() {
        try {
            graph.newBranch("new-branch");
            graph.commit(new BreadRecipe(1200,.3));
            graph.commit(new BreadRecipe(1200, 0.31));
            graph.commit(new BreadRecipe(1200, 0.32));
            graph.checkout("master");
            graph.commit(new BreadRecipe(1000, 0.8));
            List<Node> historyMaster = graph.getBranchHistory("master");
            List<Node> historyNewBranch = graph.getBranchHistory("new-branch");
            assertEquals(2, graph.getBranches().size());
            assertEquals(7, historyMaster.size());
            System.out.println("what");
        } catch (NoSuchAlgorithmException e) {
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
    void TestBranching() {
        NodeGraph branchingGraph;
        try {
            branchingGraph = new NodeGraph(new BreadRecipe(1000, 1)); // to master
            branchingGraph.commit(new BreadRecipe(999, 0.99));        // to master
            branchingGraph.commit(new BreadRecipe(998, 0.98));        // to master
            branchingGraph.newBranch("test-bread");
            branchingGraph.commit(new BreadRecipe(899, 0.89));        // to test-bread
            branchingGraph.commit(new BreadRecipe(888, 0.88));        // to test-bread
            branchingGraph.newBranch("all-wheat");
            branchingGraph.commit(new BreadRecipe(799,0.77));         // to all-wheat
            branchingGraph.commit(new BreadRecipe(788, 0.76));        // to all-wheat
            branchingGraph.checkout("test-bread");
            branchingGraph.commit(new BreadRecipe(877, 0.88));        // to test-bread
            branchingGraph.checkout("master");
            branchingGraph.commit(new BreadRecipe(699, 0.97));        // to master
            branchingGraph.commit(new BreadRecipe(688, 0.96));        // to master
            branchingGraph.commit(new BreadRecipe(677, 0.95));        // to master
            branchingGraph.merge("all-wheat");                                         // to master
            branchingGraph.commit(new BreadRecipe(599, 0.94));        // to master
            List<Node> testBreadPath = branchingGraph.getBranchHistory("test-bread");
            List<Node> allWheatPath = branchingGraph.getBranchHistory("all-wheat");
            List<Node> masterPath = branchingGraph.getBranchHistory("master");
            assertEquals(6, testBreadPath.size());
            assertEquals(7, allWheatPath.size());
            assertEquals(8, masterPath.size());
        } catch (NoSuchAlgorithmException e) {
            fail();
        }

    }

}
