package model;

import com.sun.corba.se.impl.legacy.connection.USLPort;

import java.util.ArrayList;
import java.util.Arrays;

public class BreadRecipe extends Recipe {
    private static final String defaultInstructions =
            "1. Mix all ingredients"
                    + " 2. Knead dough until smooth"
                    + " 3. Let rise in oiled bowl for 1 hour"
                    + " 4. Knock back, shape, and let rise for 45 minutes on baking pan lightly covered"
                    + " 5. Bake 30 minutes at 425F";
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
        this.blankIngredientsTemplate();
        this.calcIngredientsFromFlourWeight(flourWeight);
        for (Ingredient i : ingredientList) {
            this.doughWeight += i.getWeight();
        }
        super.instructions = "";
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
        this.blankIngredientsTemplate();
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
        scaleByDoughWeight(this.doughWeight);
    }

    public void setSaltFraction(double saltFraction) {
        this.saltFraction = saltFraction;
        scaleByDoughWeight(this.doughWeight);
    }

    public void setSugarFraction(double sugarFraction) {
        this.sugarFraction = sugarFraction;
        scaleByDoughWeight(this.doughWeight);
    }

    public void setFatFraction(double fatFraction) {
        this.fatFraction = fatFraction;
        scaleByDoughWeight(this.doughWeight);
    }

    public void setYeastFraction(double yeastFraction) {
        this.yeastFraction = yeastFraction;
        scaleByDoughWeight(this.doughWeight);
    }

    public void setDoughWeight(int doughWeight) {
        this.doughWeight = doughWeight;
    }

    public void setCookingVessel(String cookingVessel) {
        this.cookingVessel = cookingVessel;
    }

    //EFFECTS:
    public void scaleByDoughWeight(int doughWeight) {
        this.setDoughWeight(doughWeight);
        calcIngredientsFromDoughWeight(doughWeight);
    }

    public void scaleByFlourWeight(int flourWeight) {
        calcIngredientsFromFlourWeight(flourWeight);
        this.doughWeight = 0;
        for (Ingredient i : ingredientList) {
            this.doughWeight += i.getWeight();
        }
    }

    //MODIFIES: this
    //EFFECTS: creates an array list with unset ingredient weights
    private void blankIngredientsTemplate() {
        super.ingredientList.addAll(Arrays.asList(
                new Ingredient("flour", -1),
                new Ingredient("water", -1),
                new Ingredient("salt", -1),
                new Ingredient("fat", -1),
                new Ingredient("sugar", -1),
                new Ingredient("yeast", -1)));
    }

    //REQUIRES: doughWeight > 0
    //MODIFIES: this
    //EFFECTS: calculate the ingredients list from the desired final wet dough weight
    private void calcIngredientsFromDoughWeight(int doughWeight) {
        int flourWeight = (int) (doughWeight / this.yield);
        ArrayList<Double> bakersFractions = new ArrayList<Double>();
        bakersFractions.addAll(Arrays.asList(this.flourFraction, this.waterFraction,
                this.saltFraction, this.fatFraction,
                this.sugarFraction, this.yeastFraction));
        for (int i = 0; i < super.ingredientList.size(); i++) {
            super.ingredientList.get(i).setWeight((int) (flourWeight * bakersFractions.get(i)));
        }
    }

    //REQUIRES: flourWeight > 0
    //MODIFIES: this
    //EFFECTS: calculate the ingredients list from given flour weight
    private void calcIngredientsFromFlourWeight(int flourWeight) {
        ArrayList<Double> bakersFractions = new ArrayList<Double>();
        bakersFractions.addAll(Arrays.asList(this.flourFraction, this.waterFraction,
                this.saltFraction, this.fatFraction,
                this.sugarFraction, this.yeastFraction));
        for (int i = 0; i < super.ingredientList.size(); i++) {
            super.ingredientList.get(i).setWeight((int) (flourWeight * bakersFractions.get(i)));
        }
    }

    //EFFECTS: builds a string representation of the recipe information
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Ingredients: \n");
        for (Ingredient i : ingredientList) {
            if (i.getWeight() > 0) {
                String name = i.getType().substring(0, 1).toUpperCase() + i.getType().substring(1).toLowerCase();
                result.append(String.format("   - %1$s, %2$dg\n", name, i.getWeight()));
            }
        }
        result.append(toStringHelperBakeNotes());
        result.append(toStringHelperInstructions());

        return result.toString();
    }

    //EFFECTS: format the bake notes for toString()
    private String toStringHelperBakeNotes() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("\nHydration: %1$d%%\n"
                        + "Prep: %2$d hr %3$d min\n"
                        + "Bake: %4$d hr %5$d min\n"
                        + "Total: %6$d hr %7$d min\n"
                        + "Bake temp: %8$d F\n"
                        + "Baking vessel: %9$s\n"
                        + "Yield: %10$d g\n",
                (int) (this.waterFraction * 100), this.prepTime / 60, this.prepTime % 60,
                this.cookTime / 60, this.cookTime % 60, (this.prepTime + this.cookTime) / 60,
                (this.prepTime + this.cookTime) % 60, this.cookTemp, this.cookingVessel, this.doughWeight));
        return result.toString();
    }

    //EFFECTS: format the instructions for toString
    private String toStringHelperInstructions() {
        StringBuilder result = new StringBuilder();
        String[] splitList = this.instructions.split("\\d\\.");
        result.append("\nInstructions:\n");
        for (int i = 1; i < splitList.length; i++) {
            result.append(String.format("    %d." + splitList[i] + "\n", i));
        }
        return result.toString();
    }
}

