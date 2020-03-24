package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

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
        List<Node> history = graph.getHistory();
        System.out.println("what");
    }
}
