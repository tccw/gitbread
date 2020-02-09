package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestBreadRecipe {

    private BreadRecipe frenchLoaf;
    private BreadRecipe pizza;

    @BeforeEach
    public void setUp() {
        frenchLoaf = new BreadRecipe(1000);
        pizza = new BreadRecipe(500,0.7);
        pizza.setCookingVessel("pizza stone");
        pizza.setCookTemp(500);
        pizza.setPrepTime(60);
        pizza.setSaltFraction(0.032);
    }

    @Test
    public void TestBreadRecipeConstructor


}
