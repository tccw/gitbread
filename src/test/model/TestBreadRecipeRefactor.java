package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestBreadRecipeRefactor {
    private BreadRecipeRefactor enriched;
    private BreadRecipeRefactor regular;
    private BreadRecipeRefactor savory;
    private BreadRecipeRefactor starter;

    @BeforeEach
    public void setUp() {
        // initialize the recipes
        enriched = new BreadRecipeRefactor();
        regular = new BreadRecipeRefactor();
        savory = new BreadRecipeRefactor();
        starter = new BreadRecipeRefactor();
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
