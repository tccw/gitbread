package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
Bread recipe is a subclass of Recipe and represents recipes recipes made of primarily flour, with little to no fat
(i.e. this should not be used for pastry). It uses baker's formulas to allow for easy scaling of recipes and the
main constructor will back-calculate all the necessary ingredient weights.
 */
public class BreadRecipeRefactor extends RecipeRefactor {
    private double yield;
    private int doughWeight;
    private String cookingVessel;

    //EFFECTS: construct an empty recipe with an ID and flourFraction, but all other fields to be set later.
    public BreadRecipeRefactor() {
        super();
        // leave all others undefined for now
    }

    @Override
    //EFFECTS: Create a deep copy of a recipe ignoring the attempt history. Used for merging branches.
    public RecipeRefactor copy() {
        BreadRecipeRefactor copy = new BreadRecipeRefactor();
        copy.setSubRecipes(this.getSubRecipes());
        copy.setIngredientEntries(this.getIngredientEntries());
        copy.setInstructions(this.getInstructions());
        copy.setCookTime(this.getCookTime());
        copy.setPrepTime(this.getPrepTime());
        copy.setCookTemp(this.getCookTemp());
        copy.setCookingVessel(this.getCookingVessel());
        return copy;
    }

    //EFFECTS:
    public void scaleByDoughWeight(int doughWeight) {
        this.setDoughWeight(doughWeight);
        calcIngredientsFromDoughWeight(doughWeight);
    }

    public void scaleByFlourWeight(int flourWeight) {
        calcIngredientsFromFlourWeight(flourWeight);
        this.doughWeight = 0;
        for (IngredientRefactor i : ingredientEntries) {
            this.doughWeight += i.getWeight();
        }
    }

    //EFFECTS: calculates the new yield after bakers percentages are updated
    public void updateYield() {
        this.yield = 0;
        for (IngredientEntry ie : this) {
            if (ie.getBakerPercentage() != -1) {
                this.yield += ie.getBakerPercentage();
            }
        }
    }

    //REQUIRES: doughWeight > 0
    //MODIFIES: this
    //EFFECTS: calculate the ingredients list from the desired final wet dough weight
    private void calcIngredientsFromDoughWeight(int doughWeight) {
        updateYield();
        int flourWeight = (int) (doughWeight / this.yield);
        ArrayList<Double> bakersFractions = new ArrayList<>(Arrays.asList(
                this.flourFraction, this.waterFraction,
                this.saltFraction, this.fatFraction,
                this.sugarFraction, this.yeastFraction));
        for (int i = 0; i < super.ingredientEntries.size(); i++) {
            super.ingredientEntries.get(i).setWeight((int) (flourWeight * bakersFractions.get(i)));
        }
    }

    //MODIFIES:
    //EFFECTS: calculates the bakers percentages for all the ingredients.
    public void bakerPercentage() {
        int flourMass = 0;
        for (IngredientRefactor i : this) {
            if (i.getType() == IngredientRefactor.Type.FLOUR) {
                flourMass += i.getWeight();
            }
        }

        for (IngredientRefactor i : this) {

        }

    }

    //REQUIRES: flourWeight > 0
    //MODIFIES: this
    //EFFECTS: calculate the ingredients list from given flour weight
    private void calcIngredientsFromFlourWeight(int flourWeight) {
        ArrayList<Double> bakersFractions = new ArrayList<>(Arrays.asList(
                this.flourFraction, this.waterFraction,
                this.saltFraction, this.fatFraction,
                this.sugarFraction, this.yeastFraction));
        for (int i = 0; i < super.ingredientEntries.size(); i++) {
            super.ingredientEntries.get(i).setWeight((int) (flourWeight * bakersFractions.get(i)));
        }
    }

    //EFFECTS: builds a string representation of the recipe information
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Ingredients: \n");
        for (IngredientRefactor i : ingredientEntries) {
            if (i.getWeight() > 0) {
                String name = i.getName().substring(0, 1).toUpperCase() + i.getName().substring(1).toLowerCase();
                result.append(String.format("   - %1$s, %2$dg\n", name, i.getWeight()));
            }
        }
        result.append("\n");
        result.append(toStringHelperBakeNotes());
        result.append(toStringHelperInstructions());

        return result.toString();
    }

    //EFFECTS: format the bake notes for toString()
    private String toStringHelperBakeNotes() {
        return String.format("Hydration: %1$d%%\n"
                        + "Prep: %2$d hr %3$d min\n"
                        + "Bake: %4$d hr %5$d min\n"
                        + "Total: %6$d hr %7$d min\n"
                        + "Bake temp: %8$d F\n"
                        + "Baking vessel: %9$s\n"
                        + "Yield: %10$d g\n",
                (int) (this.waterFraction * 100), this.prepTime / 60, this.prepTime % 60,
                this.cookTime / 60, this.cookTime % 60, (this.prepTime + this.cookTime) / 60,
                (this.prepTime + this.cookTime) % 60, this.cookTemp, this.cookingVessel, this.doughWeight);
    }

    //EFFECTS: format the instructions for toString
    private String toStringHelperInstructions() {
        StringBuilder result = new StringBuilder();
        String[] splitList = this.instructions.split("\\d\\d?\\.");
        result.append("\nInstructions:\n");
        for (int i = 1; i < splitList.length; i++) {
            result.append(String.format("    %d." + splitList[i] + "\n", i));
        }
        return result.toString();
    }

    @Override
    public List<String> splitInstructions() {
        List<String> stringList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        for (IngredientRefactor i : ingredientEntries) {
            if (i.getWeight() > 0) {
                String name = i.getName().substring(0, 1).toUpperCase() + i.getName().substring(1).toLowerCase();
                stringBuilder.append(String.format("\u2022 %1$s: %2$dg\n", name, i.getWeight()));
            }
        }
        stringList.add(stringBuilder.toString());
        String notes = this.toStringHelperBakeNotes();//.split("\\n");
        // problem here is that String.split() splits around the RegEx match so the first element is an empty string
        // because the string starts with a RegEx match (i.e. "1.")
        String temp = this.instructions.substring(2).trim(); // drop the "1." at the beginning of the instructions
        String[] instructions = temp.split("\\d\\d?\\.");
        stringList.add(notes);
        stringList.add("INSTRUCTIONS_SEPARATOR");
        stringList.addAll(Arrays.asList(instructions));

        return stringList;
    }

    // getters

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

    public void setDoughWeight(int doughWeight) {
        this.doughWeight = doughWeight;
    }

    public void setCookingVessel(String cookingVessel) {
        this.cookingVessel = cookingVessel;
    }

}

