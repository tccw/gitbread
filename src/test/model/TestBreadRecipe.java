package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBreadRecipe {

    private BreadRecipe frenchLoaf;
    private BreadRecipe pizza;

    @BeforeEach
    public void setUp() {
        frenchLoaf = new BreadRecipe(1000);
        pizza = new BreadRecipe(500, 0.7);
    }

    @Test
    public void TestBreadRecipeConstructorDoughWeight() {
        HelperCheckBreadRecipeInitializedDoughWeight(frenchLoaf, 1000);
    }

    @Test
    public void TestBreadRecipeConstructorFlourHydration() {
        HelperCheckBreadRecipeInitializedFlourHydration(pizza, 500, 0.7);
    }

    @Test
    public void TestScaleByDoughWeight() {
        HelperCheckBreadRecipeInitializedDoughWeight(frenchLoaf, 1000);
        frenchLoaf.scaleByDoughWeight(25000);
        HelperCheckBreadRecipeInitializedDoughWeight(frenchLoaf, 25000);
    }

    @Test
    public void TestScaleByFlourWeight() {
        HelperCheckBreadRecipeInitializedFlourHydration(pizza, 500, 0.7);
        pizza.scaleByFlourWeight(530);
        HelperCheckBreadRecipeInitializedFlourHydration(pizza, 530, 0.7);
    }

    @Test
    public void TestMultiScaleByFlourWeight() {
        HelperCheckBreadRecipeInitializedFlourHydration(pizza, 500, 0.7);
        pizza.scaleByFlourWeight(300);
        pizza.scaleByDoughWeight(123456);
        pizza.scaleByFlourWeight(1350);
        HelperCheckBreadRecipeInitializedFlourHydration(pizza, 1350, 0.7);
    }

    @Test
    public void TestMultiScaleByDoughWeight() {
        HelperCheckBreadRecipeInitializedDoughWeight(frenchLoaf, 1000);
        frenchLoaf.scaleByDoughWeight(10000);
        frenchLoaf.scaleByDoughWeight(1245);
        frenchLoaf.scaleByDoughWeight(780);
        HelperCheckBreadRecipeInitializedDoughWeight(frenchLoaf, 780);
    }

    @Test
    public void TestSetInstructions() {
        HelperCheckBreadRecipeInitializedDoughWeight(frenchLoaf, 1000);
        String expected = "1. Mix the bread 2. Bake the bread 3. Eat the bread";
        frenchLoaf.setInstructions(expected);
        assertEquals(expected, frenchLoaf.getInstructions());
    }

    @Test
    public void TestSetInstructionsMulti() {
        HelperCheckBreadRecipeInitializedDoughWeight(frenchLoaf, 1000);
        String expectedOne = "1. Mix the bread 2. Bake the bread 3. Eat the bread";
        String expectedTwo = "1. Mix the bread 2. Burn the bread 3. Try to eat the bread";
        String expectedThree = "1. Consider quitting baking";
        frenchLoaf.setInstructions(expectedOne);
        frenchLoaf.setInstructions(expectedTwo);
        frenchLoaf.setInstructions(expectedThree);
        assertEquals(expectedThree, frenchLoaf.getInstructions());
    }

    @Test
    public void TestBreadRecipeSetFields() {
        HelperCheckBreadRecipeInitializedFlourHydration(pizza, 500, 0.7);
        pizza.setCookingVessel("pizza stone");
        pizza.setCookTemp(500);
        pizza.setPrepTime(60);
        pizza.setCookTime(6);
        pizza.setWaterFraction(0.64);
        pizza.setSaltFraction(0.032);
        pizza.setSugarFraction(0.01);
        pizza.setFatFraction(0.02);
        pizza.setYeastFraction(0.05);
        assertEquals(500, pizza.getCookTemp());
        assertEquals(60, pizza.getPrepTime());
        assertEquals(6, pizza.getCookTime());
        assertEquals("pizza stone", pizza.getCookingVessel());

    }

    //EFFECTS: tests that the fields in the recipe are as we expect
    public void HelperCheckBreadRecipeInitializedDoughWeight(BreadRecipe r, int doughWeight) {
        double expectedYield = r.getFlourFraction() + r.getWaterFraction() + r.getSaltFraction()
                + r.getSugarFraction() + r.getFatFraction() + r.getYeastFraction();
        ArrayList<String> ingredients = new ArrayList<String>();
        ingredients.addAll(Arrays.asList("flour", "water", "salt", "sugar", "fat", "yeast"));
        int flourWeight = (int) (doughWeight / r.getYield());
        ArrayList<Integer> weights = new ArrayList<Integer>(Arrays.asList(flourWeight, (int) (flourWeight * 0.66),
                (int) (flourWeight * 0.02), 0, 0, (int) (flourWeight * 0.006)));
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

    private void HelperCheckBreadRecipeInitializedFlourHydration(BreadRecipe r, int flourWeight, double hydration) {
        double expectedYield = r.getFlourFraction() + r.getWaterFraction() + r.getSaltFraction()
                + r.getSugarFraction() + r.getFatFraction() + r.getYeastFraction();
        int expectedDoughWeight = 0;
        ArrayList<String> ingredients = new ArrayList<String>();
        ingredients.addAll(Arrays.asList("flour", "water", "salt", "sugar", "fat", "yeast"));
        ArrayList<Integer> weights = new ArrayList<Integer>(Arrays.asList(flourWeight, (int) (flourWeight * hydration),
                (int) (flourWeight * 0.02), 0, 0, (int) (flourWeight * 0.006)));
        assertEquals(1, r.getFlourFraction());
        assertEquals(hydration, r.getWaterFraction());
        assertEquals(0.02, r.getSaltFraction());
        assertEquals(0, r.getSugarFraction());
        assertEquals(0, r.getFatFraction());
        assertEquals(0.006, r.getYeastFraction());
        assertEquals(expectedYield, r.getYield());
        assertEquals("none set", r.getCookingVessel());
        assertEquals(-1, r.getCookTime());
        assertEquals(-1, r.getPrepTime());
        assertEquals(-1, r.getCookTemp());
        assertEquals(0, r.getAttemptHistory().size());
        assertEquals(6, r.getIngredientList().size());
        for (int i = 0; i < ingredients.size(); i++) {
            assertEquals(weights.get(i), r.getIngredientWeight(ingredients.get(i)));
            expectedDoughWeight += weights.get(i);
        }
        assertEquals(expectedDoughWeight, r.getDoughWeight());

    }

}
