package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
Bread recipe is a subclass of Recipe and represents recipes recipes made of primarily flour, with little to no fat
(i.e. this should not be used for pastry). It uses baker's formulas to allow for easy scaling of recipes and the
main constructor will back-calculate all the necessary ingredient weights.
 */
public class BreadRecipeRefactor extends RecipeRefactor {
    // if a user wants Pate fermentee/Biga/Poolish, etc. they can name them. PREFERMENT type keeps the system simple.
    public enum Type {MAIN, PREFERMENT, SOAKER}

    private static final double MILK_WATER_FRACTION = 0.88;

    private Type type;
    private double yield;
    private int doughWeight;
    private double hydration;
    private String cookingVessel;

    //EFFECTS: construct an empty recipe with an ID and flourFraction, but all other fields to be set later.
    public BreadRecipeRefactor() {
        super();
        this.hydration = 0;
        // leave all others undefined for now
    }

    //EFFECTS: construct an empty recipe with an ID and flourFraction, but all other fields to be set later.
    public BreadRecipeRefactor(String name, Type type) {
        super(name);
        this.hydration = 0;
        this.type = type;
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
        int flourWeight = (int) (doughWeight / this.getYield());
        scaleByFlourWeight(flourWeight);
    }

    public void scaleByFlourWeight(int flourWeight) {
        this.setDoughWeight(0);
        int doughResult = 0;
        for (IngredientEntry ie : this) {
            IngredientRefactor i = ie.getIngredient();
            i.setWeight((int) (ie.getBakerPercentage() * flourWeight)); // consider changing all
            doughResult += i.getWeight();
        }
        this.setDoughWeight(doughResult);
    }

    //EFFECTS: calculates the new yield after bakers percentages are updated
    public void updateYield() {
        this.setYield(0);
        double yieldResult = 0.;
        for (IngredientEntry ie : this) {
            if (ie.getBakerPercentage() != -1) {
                yieldResult += ie.getBakerPercentage();
            }
        }
        this.setYield(yieldResult);
    }

    //MODIFIES:
    //EFFECTS: calculates the bakers percentages for all the ingredients.
    public void bakerPercentage() {
        int flourMass = 0;
        int waterMass = 0;
        for (IngredientEntry ie : this) {
            if (ie.getIngredient().getType() == IngredientRefactor.Type.FLOUR) {
                flourMass += ie.getIngredient().getWeight();
            } else if (ie.getIngredient().getType() == IngredientRefactor.Type.WATER) {
                waterMass += ie.getIngredient().getWeight();
            } else if (ie.getIngredient().getType() == IngredientRefactor.Type.MILK) {
                waterMass += ie.getIngredient().getWeight() * MILK_WATER_FRACTION;
            }
        }

        this.setHydration((double) waterMass / flourMass);
        this.setDoughWeight(0);
        int doughResult = 0;
        for (IngredientEntry ie : this) {
            int weight = ie.getIngredient().getWeight();
            ie.setBakerPercentage((double) weight / flourMass);
            doughResult += weight;
        }
        this.setDoughWeight(doughResult);

        updateYield();
    }

    //EFFECTS: builds a string representation of the recipe information
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Ingredients: \n");
        for (IngredientEntry ie : super.getIngredientEntries()) {
            String name = ie.getIngredient().getName();
            result.append(String.format("   - %1$s, %2$dg\n", name, ie.getIngredient().getWeight()));
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
                (int) (this.hydration * 100), this.prepTime / 60, this.prepTime % 60,
                this.cookTime / 60, this.cookTime % 60, (this.prepTime + this.cookTime) / 60,
                (this.prepTime + this.cookTime) % 60, this.cookTemp, this.cookingVessel, this.doughWeight);
    }

    //EFFECTS: format the instructions for toString
    private String toStringHelperInstructions() {
        StringBuilder result = new StringBuilder();
        String[] splitList = this.getInstructions().split("\\d\\d?\\.");
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
        for (IngredientEntry ie : super.getIngredientEntries()) {
            IngredientRefactor i = ie.getIngredient();
            String name = i.getName().substring(0, 1).toUpperCase() + i.getName().substring(1).toLowerCase();
            stringBuilder.append(String.format("\u2022 %1$s: %2$dg\n", name, i.getWeight()));
        }
        stringList.add(stringBuilder.toString());
        String notes = this.toStringHelperBakeNotes();//.split("\\n");
        // problem here is that String.split() splits around the RegEx match so the first element is an empty string
        // because the string starts with a RegEx match (i.e. "1.")
        String temp = this.getInstructions().substring(2).trim(); // drop the "1." at the beginning of the instructions
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

    public double getHydration() {
        return hydration;
    }

    public Type getType() {
        return type;
    }


    // setters
    private void setDoughWeight(int doughWeight) {
        this.doughWeight = doughWeight;
    }

    public void setCookingVessel(String cookingVessel) {
        this.cookingVessel = cookingVessel;
    }

    public void setType(Type type) {
        this.type = type;
    }

    private void setHydration(double hydration) {
        this.hydration = hydration;
    }

    private void setYield(double yield) {
        this.yield = yield;
    }
}

