package model;

import exceptions.NoSuchIngredientException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestBreadRecipeRefactor {
    private BreadRecipeRefactor enriched;
    private BreadRecipeRefactor regular;
    private BreadRecipeRefactor savory;
    private BreadRecipeRefactor starter;

    @BeforeEach
    public void setUp() {
        // initialize the recipes
        enriched = new BreadRecipeRefactor("Light Rye Loaf", BreadRecipeRefactor.Type.MAIN);
        regular = new BreadRecipeRefactor("French Loaf", BreadRecipeRefactor.Type.MAIN);
        savory = new BreadRecipeRefactor("Rye Sandwich Bread", BreadRecipeRefactor.Type.MAIN);
        starter = new BreadRecipeRefactor("Levain", BreadRecipeRefactor.Type.PREFERMENT);
        // add all the ingredients
        enrichedHelper();
        regularHelper();
        savoryHelper();
        starterHelper();
    }

    @Test
    public void TestBakersPercentage() {
        enriched.bakerPercentage(); // this is a good candidate for the observer pattern. Recalc whenever ingredients change
        regular.bakerPercentage();
        savory.bakerPercentage();

        assertEquals((double) 397 / (482 + 213), enriched.getHydration());
        assertEquals(1.0, enriched.getIngredientEntries().get(0).getBakerPercentage()
                              + enriched.getIngredientEntries().get(1).getBakerPercentage());
        assertEquals(1.0 , regular.getIngredientEntries().get(3).getBakerPercentage()
                + starter.getIngredientEntries().get(2).getBakerPercentage()
                + starter.getIngredientEntries().get(3).getBakerPercentage());
        try {
            assertEquals(454, regular.getIngredientWeight("Lukewarm Water"));
        } catch (NoSuchIngredientException e) {
            fail();
        }

    }

    @Test
    public void TestGetIngredientWeightFail() {
        try {
            starter.getIngredientWeight("Not an ingredient");
            fail();
        } catch (NoSuchIngredientException e) {
            // expected
        }
    }

    @Test public void TestScaleByFlourWeight() {
        enriched.bakerPercentage();
        enriched.scaleByFlourWeight(350);

        int[] expected = new int[]{242, 107, 4, 14, 7, 21, 42, 14, 1, 199};
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], enriched.getIngredientEntries().get(i).getIngredient().getWeight());
        }
    }

    @Test
    public void TestScaleByDoughWeight() {
        enriched.bakerPercentage();
        enriched.scaleByDoughWeight(2500);

        int[] expected = new int[]{};
    }

    @Test
    public void TestToString() {
        regular.bakerPercentage();
        regular.setInstructions(new String("1. Something 2. Something else 3. Another thing"));
        regular.setCookingVessel("Baking Pan");
        regular.setCookTemp(400);
        regular.setPrepTime(210);
        System.out.println(regular.toString());
        List<String> output = regular.splitInstructions();
        for (String s : output) {
            System.out.println(s);
        }
    }

    //HELPERS

    private void enrichedHelper() {
        enriched.addIngredient(new IngredientRefactor(IngredientRefactor.Type.FLOUR, "AP Flour", 482));
        enriched.addIngredient(new IngredientRefactor(IngredientRefactor.Type.FLOUR, "Rye Flour", 213));
        enriched.addIngredient(new IngredientRefactor(IngredientRefactor.Type.YEAST, "Instant Yeast", 8));
        enriched.addIngredient(new IngredientRefactor(IngredientRefactor.Type.SUGAR, "Brown Sugar", 28));
        enriched.addIngredient(new IngredientRefactor(IngredientRefactor.Type.SALT, "Salt", 14));
        enriched.addIngredient(new IngredientRefactor(IngredientRefactor.Type.FAT, "Butter", 43));
        enriched.addIngredient(new IngredientRefactor(IngredientRefactor.Type.SUGAR, "Molasses", 85));
        enriched.addIngredient(new IngredientRefactor(IngredientRefactor.Type.FAT, "Baker's Special Dry Milk", 28));
        enriched.addIngredient(new IngredientRefactor(IngredientRefactor.Type.MIX_IN, "Caraway Seeds", 2));
        enriched.addIngredient(new IngredientRefactor(IngredientRefactor.Type.WATER, "Lukewarm Water", 397));
    }

    private void regularHelper() {
        regular.addIngredient(new IngredientRefactor(IngredientRefactor.Type.WATER, "Lukewarm Water", 227));
        regular.addIngredient(new IngredientRefactor(IngredientRefactor.Type.YEAST, "Instant Yeast", 2));
        regular.addIngredient(new IngredientRefactor(IngredientRefactor.Type.SUGAR, "White Sugar", 14));
        regular.addIngredient(new IngredientRefactor(IngredientRefactor.Type.FLOUR, "Bread Flour", 450));
        regular.addSubRecipe(starter);

    }

    private void savoryHelper() {
        savory.addIngredient(new IngredientRefactor(IngredientRefactor.Type.YEAST, "Instant Yeast", 8));
        savory.addIngredient(new IngredientRefactor(IngredientRefactor.Type.WATER, "Lukewarm Water", 152));
        savory.addIngredient(new IngredientRefactor(IngredientRefactor.Type.FAT, "Vegetable Oil", 50));
        savory.addIngredient(new IngredientRefactor(IngredientRefactor.Type.SALT, "Salt", 3));
        savory.addIngredient(new IngredientRefactor(IngredientRefactor.Type.SUGAR, "White Sugar", 12));
        savory.addIngredient(new IngredientRefactor(IngredientRefactor.Type.MIX_IN, "Caraway Seeds", 2));
        savory.addIngredient(new IngredientRefactor(IngredientRefactor.Type.MIX_IN, "Dill Seeds", 2));
        savory.addIngredient(new IngredientRefactor(IngredientRefactor.Type.MIX_IN, "Mustard Seeds", 2));
        savory.addIngredient(new IngredientRefactor(IngredientRefactor.Type.FLOUR, "Potato Flour", 46));
        savory.addIngredient(new IngredientRefactor(IngredientRefactor.Type.FLOUR, "Bread Flour", 298));
        savory.addIngredient(new IngredientRefactor(IngredientRefactor.Type.FLOUR, "Pumpernickel", 138));
        savory.addIngredient(new IngredientRefactor(IngredientRefactor.Type.WATER, "Dill Pickle Juice", 170));
    }

    private void starterHelper() {
        starter.addIngredient(new IngredientRefactor(IngredientRefactor.Type.WATER, "Lukewarm Water", 227));
        starter.addIngredient(new IngredientRefactor(IngredientRefactor.Type.YEAST, "Instant Yeast", 2));
        starter.addIngredient(new IngredientRefactor(IngredientRefactor.Type.FLOUR, "Bread Flour", 149));
        starter.addIngredient(new IngredientRefactor(IngredientRefactor.Type.FLOUR, "Whole Wheat Flour", 28));
    }
}
