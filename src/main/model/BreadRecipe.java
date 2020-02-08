package model;

public class BreadRecipe extends Recipe {

    //EFFECTS: constructs an empty bread recipe
    public BreadRecipe() {
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
