package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.text.AbstractDocument;
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
            List<Node> historyMaster = graph.getHistory("master");
            List<Node> historyNewBranch = graph.getHistory("new-branch");
            assertEquals(2, graph.getBranches().size());
            assertEquals(6, historyMaster.size());
            System.out.println("what");
        } catch (NoSuchAlgorithmException e) {
           fail();
        }
    }
}
