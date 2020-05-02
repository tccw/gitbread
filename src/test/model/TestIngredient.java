package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestIngredient {
    private Ingredient flour;

    @BeforeEach
    void setUp() {
        flour = new Ingredient("flour", 100);
    }

    @Test
    void TestSetWeight() {
        assertEquals(100, flour.getWeight());
        assertEquals("flour", flour.getName());
        flour.setWeight(150);
        flour.setWeight(403);
        assertEquals(403, flour.getWeight());
    }

    @Test
    void TestSetType() {
        assertEquals("flour", flour.getName());
        flour.setName("semolina");
        assertEquals("semolina", flour.getName());
    }
}
