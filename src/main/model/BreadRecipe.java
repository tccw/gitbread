package model;

import java.util.ArrayList;

public class BreadRecipe extends Recipe {

    //EFFECTS: constructs an empty bread recipe
    public BreadRecipe() {
        //stub
    }

    //EFFECTS: constructs a bread recipe with ingredients, instructions, preparation time, and cooking time
    public BreadRecipe(ArrayList<Ingredient> ingredients, String steps, int prepTime, int cookTime, int cookTempF) {
        //stub
    }

    public void scaleRecipe(int scale) {
        //stub
    }

    //REQUIRES: hydration must be > 0
    //MODIFIES: this
    //EFFECTS: calculates the amount of water needed to reach a desired hydration
    public void calcWater(double hydration) {
        // stub
    }

    //REQUIRES: gramsWater must be > 0
    //MODIFIES: this
    //EFFECTS: calculates the hydration fraction given an amount of water and inserts it into the ingredients list.
    public void calcHydration(int gramsWater) {

    }


}
