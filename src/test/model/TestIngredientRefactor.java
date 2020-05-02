package model;

import org.junit.jupiter.api.BeforeEach;

public class TestIngredientRefactor {
    IngredientRefactor wholeWheatFlour;
    IngredientRefactor milk;
    IngredientRefactor raisins;

    @BeforeEach
    public void setUp() {
        wholeWheatFlour = new IngredientRefactor(IngredientRefactor.Type.FLOUR, "Whole Wheat Flour", 150);
        milk = new IngredientRefactor(IngredientRefactor.Type.MILK, "Whole Milk", 100);
        raisins = new IngredientRefactor(IngredientRefactor.Type.MIX_IN, "Raisins", 50);
    }


}
