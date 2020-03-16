package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestGraphHistory {
    private GraphHistory graph;
    private Node root;
    private Node headMaster;

    @BeforeEach
    void setUp() {
        try {
            graph = new GraphHistory(new BreadRecipe(1000, 0.99));
            root = graph.getRoot();
            for (double i = 0; i < 0.08; i = i + 0.02) {
                graph.commit(new BreadRecipe(1000, 1 - i));
            }
        } catch (NoSuchAlgorithmException e) {
            fail();
        }
    }

    @Test
    void testConstructor() {
        assertEquals(5, graph.size());
    }

    @Test
    void testGetRoot() {
        try {
            BreadRecipe recipe = (BreadRecipe)(graph.getRoot().getRecipeVersion());
            assertEquals(0.99, recipe.getWaterFraction());
        } catch (NoSuchAlgorithmException e) {
            fail();
        }
    }

    @Test
    void testNewBranches() {
        try {
            graph.newBranch("low-hydration");
            for (double i = 0.2; i < 0.3; i = i + 0.02) {
                graph.commit(new BreadRecipe(1000, 1 - i));
            }
            assertEquals(10, graph.size());
        } catch (NoSuchAlgorithmException e) {
            fail();
        }
    }

    @Test
    void dummyConstructorTest() {
        GraphHistory dummy = new GraphHistory();
    }
}
