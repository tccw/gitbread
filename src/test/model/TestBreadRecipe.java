package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.time.DateTimeException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public void TestBreadRecipeConstructorDoughWeight() {
        HelperCheckBreadRecipeInitialized(frenchLoaf, 1000);
    }

    //EFFECTS: tests that the fields in the recipe are as we expect
    public void HelperCheckBreadRecipeInitialized(BreadRecipe r, int doughWeight) {
        double expectedYield = r.getFlourFraction() + r.getWaterFraction() + r.getSaltFraction()
                             + r.getSugarFraction() + r. getFatFraction() + r.getYeastFraction();
        ArrayList<String> ingredients = new ArrayList<String>();
        ingredients.addAll(Arrays.asList("flour", "water", "salt", "sugar", "fat", "yeast"));
        int flourWeight = (int)(doughWeight / r.getYield());
        ArrayList<Integer> weights = new ArrayList<Integer>(Arrays.asList(flourWeight, (int)(flourWeight * 0.66),
                (int)(flourWeight * 0.02), 0, 0, (int)(flourWeight * 0.006)));
        assertEquals(1, r.getFlourFraction());
        assertEquals(0.66, r.getWaterFraction());
        assertEquals(0.02, r.getSaltFraction());
        assertEquals(0, r.getSugarFraction());
        assertEquals(0, r.getFatFraction());
        assertEquals(0.006, r.getYeastFraction());
        assertEquals(doughWeight, r.getDoughWeight());
        assertEquals(expectedYield, r.getYield());
        assertEquals("pan", r.getCookingVessel());
        assertEquals(30, r.getCookTime());
        assertEquals(135, r.getPrepTime());
        assertEquals(425, r.getCookTemp());
        assertEquals(0, r.getAttemptHistory().size());
        assertEquals(6, r.getIngredientList().size());

        for (int i = 0; i < ingredients.size(); i++) {
            assertEquals(weights.get(i), r.getIngredientWeight(ingredients.get(i)));
        }


    }

}
