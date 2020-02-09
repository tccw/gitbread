package model;

import java.util.ArrayList;
import java.util.Arrays;

public class BreadRecipe extends Recipe {
    private static final String defaultInstructions =
            "1. Mix all ingredients"
                    + " 2. Knead dough until smooth"
                    + " 3. Let rise in oiled bowl for 1 hour"
                    + " 4. Knock back, shape, and let rise for 45 minutes on baking pan lightly covered"
                    + " 5. Bake 30 minutes at 425F.";
    private static final double flourConst = 1;

    private double flourFraction;
    private double waterFraction;
    private double saltFraction;
    private double sugarFraction;
    private double fatFraction;
    private double yeastFraction;
    private double yield;
    private int doughWeight;
    private String cookingVessel;

    //REQUIRES: flourWeight and hydrationFraction are > 0
    //EFFECTS: constructs bread recipe for a given flour weight and desired hydration. All other parameters are empty
    //         or zeroed out.
    public BreadRecipe(int flourWeight, double hydrationPercentage) {
        this.flourFraction = flourConst; // flour baker's percentage is always 1
        this.waterFraction = hydrationPercentage;
        this.saltFraction = 0.02;
        this.sugarFraction = 0;
        this.fatFraction = 0;
        this.yeastFraction = 0.006;
        this.yield = flourFraction + waterFraction + saltFraction + sugarFraction + fatFraction + yeastFraction;
        this.calcIngredientsFromFlourWeight(flourWeight);
        for (Ingredient i : ingredientList) {
            this.doughWeight += i.getWeight();
        }
        super.instructions = defaultInstructions;
        super.attemptHistory = new ArrayList<Attempt>();
        super.cookTime = -1;  // -1 indicates no cookTime has been set
        super.prepTime = -1; // -1 indicates no prepTime has been set
        super.cookTemp = -1; // -1 indicates no cookTemp has been set
        this.cookingVessel = "none set";
    }

    //EFFECTS: constructs a default bread recipe with a desired wet dough weight
    public BreadRecipe(int doughWeight) {
        this.flourFraction = flourConst; // flour baker's percentage is always 1
        this.waterFraction = .66;
        this.saltFraction = 0.02;
        this.sugarFraction = 0;
        this.fatFraction = 0;
        this.yeastFraction = 0.006;
        this.yield = flourFraction + waterFraction + saltFraction + sugarFraction + fatFraction + yeastFraction;
        this.doughWeight = doughWeight; // grams
        this.calcIngredientsFromDoughWeight(this.doughWeight);
        super.instructions = defaultInstructions;
        super.attemptHistory = new ArrayList<Attempt>();
        super.cookTime = 30;  // minutes
        super.prepTime = 135; //minutes
        super.cookTemp = 425; // in degrees F
        this.cookingVessel = "pan";
    }

    // getters

    public double getFlourFraction() {
        return flourFraction;
    }

    public double getWaterFraction() {
        return waterFraction;
    }

    public double getSaltFraction() {
        return saltFraction;
    }

    public double getSugarFraction() {
        return sugarFraction;
    }

    public double getFatFraction() {
        return fatFraction;
    }

    public double getYeastFraction() {
        return yeastFraction;
    }

    public double getYield() {
        return yield;
    }

    public int getDoughWeight() {
        return doughWeight;
    }

    public String getCookingVessel() {
        return cookingVessel;
    }

    // setters
    public void setWaterFraction(double waterFraction) {
        this.waterFraction = waterFraction;
    }

    public void setSaltFraction(double saltFraction) {
        this.saltFraction = saltFraction;
    }

    public void setSugarFraction(double sugarFraction) {
        this.sugarFraction = sugarFraction;
    }

    public void setFatFraction(double fatFraction) {
        this.fatFraction = fatFraction;
    }

    public void setYeastFraction(double yeastFraction) {
        this.yeastFraction = yeastFraction;
    }

    public void setDoughWeight(int doughWeight) {
        this.doughWeight = doughWeight;
    }

    public void setCookingVessel(String cookingVessel) {
        this.cookingVessel = cookingVessel;
    }

    //REQUIRES: doughWeight > 0
    //MODIFIES: this
    //EFFECTS: calculate the ingredients list from the desired final wet dough weight
    private void calcIngredientsFromDoughWeight(int doughWeight) {
        int flourWeight = (int) (doughWeight / this.yield);
        this.ingredientList.addAll(Arrays.asList(new Ingredient("flour", flourWeight),
                new Ingredient("water", (int) (flourWeight * this.waterFraction)),
                new Ingredient("salt",  (int) (flourWeight * this.saltFraction)),
                new Ingredient("fat",   (int) (flourWeight * this.fatFraction)),
                new Ingredient("sugar", (int) (flourWeight * this.sugarFraction)),
                new Ingredient("yeast", (int) (flourWeight * this.yeastFraction))));
    }

    //REQUIRES: flourWeight > 0
    //MODIFIES: this
    //EFFECTS: calculate the ingredients list from given flour weight
    private void calcIngredientsFromFlourWeight(int flourWeight) {
        this.ingredientList.addAll(Arrays.asList(new Ingredient("flour", flourWeight),
                new Ingredient("water", (int) (flourWeight * this.waterFraction)),
                new Ingredient("salt",  (int) (flourWeight * this.saltFraction)),
                new Ingredient("fat",   (int) (flourWeight * this.fatFraction)),
                new Ingredient("sugar", (int) (flourWeight * this.sugarFraction)),
                new Ingredient("yeast", (int) (flourWeight * this.yeastFraction))));
    }

}
