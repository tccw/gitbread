package model;

import java.util.ArrayList;

public abstract class Recipe {

    protected ArrayList<Ingredient> ingredientList;
    protected String instructions;
    protected ArrayList<Attempt> attemptHistory;
    protected int cookTime; // in minutes
    protected int prepTime; // in minutes

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

    //EFFECTS: Counts the number of elements in attemptHistory
    protected int countAttempts() {
        return 0; //stub
    }

    //MODIFIES: this
    //EFFECTS: adds an ingredient to the ingredient list
    protected void addIngredient() {
        //stub
    }

    //MODIFIES: this
    //EFFECTS: adds an attempt to the attempt history
    protected void addAttempt(Attempt attempt) {
        this.attemptHistory.add(attempt);
    }


}
