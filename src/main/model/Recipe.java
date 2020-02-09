package model;

import java.util.ArrayList;

public abstract class Recipe {

    protected ArrayList<Ingredient> ingredientList;
    protected String instructions;
    protected ArrayList<Attempt> attemptHistory;
    protected int cookTime; // in minutes
    protected int prepTime; // in minutes
    protected int cookTemp; // in F

    // setters
    protected void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    protected void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    protected void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    protected void setCookTemp(int cookTemp) {
        this.cookTemp = cookTemp;
    }

    // getters
    protected ArrayList<Ingredient> getIngredientList() {
        return this.ingredientList;
    }

    protected String getInstructions() {
        return this.instructions;
    }

    protected ArrayList<Attempt> getAttemptHistory() {
        return this.attemptHistory;
    }

    protected int getCookTime() {
        return this.cookTime;
    }

    protected int getPrepTime() {
        return this.prepTime;
    }

    protected int getCookTemp() {
        return this.cookTemp;
    }

    //setters


    //EFFECTS: Counts the number of elements in attemptHistory
    protected int countAttempts() {
        return 0; //stub
    }

    //MODIFIES: this
    //EFFECTS: adds an ingredient to the ingredient list
    protected void addIngredient() {
        //stub
    }

    //EFFECTS: counts the number of elements in the ingredients ArrayList
    protected void countIngredients() {
        //stub
    }

    //MODIFIES: this
    //EFFECTS: adds an attempt to the attempt history
    protected void addAttempt(Attempt attempt) {
        this.attemptHistory.add(attempt);
    }

    // Get ingredient weights
    //EFFECTS: produce the weight of the selected ingredient in grams
    public int getIngredientWeight(String ingredientName) {
        for (Ingredient i : this.ingredientList) {
            if (i.getType().equals(ingredientName.toLowerCase())) {
                return i.getWeight();
            }
        }
        return -1; //System.out.printf("The ingredient \"%s\" could not be found", ingredientName);
    }

    //EFFECTS: this
    public String toString() {
        return ""; // stub
    }


}
