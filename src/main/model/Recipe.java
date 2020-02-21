package model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.Clock;
import java.util.ArrayList;

/*
This is the abstract representation of a Recipe with common fields and methods.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
public abstract class Recipe {

    protected ArrayList<Ingredient> ingredientList = new ArrayList<Ingredient>();
    protected String instructions;
    @JsonManagedReference
    protected ArrayList<Attempt> attemptHistory = new ArrayList<Attempt>();
    protected int cookTime; // in minutes
    protected int prepTime; // in minutes
    protected int cookTemp; // in F

    //EFFECTS: Counts the number of elements in attemptHistory
    protected int countAttempts() {
        return attemptHistory.size();
    }

    //MODIFIES: this
    //EFFECTS: adds an ingredient to the ingredient list
//    protected void addIngredient() {
//        //stub
//    }

    //EFFECTS: counts the number of elements in the ingredients ArrayList
    protected int countIngredients() {
        return this.ingredientList.size();
    }

    //MODIFIES: this
    //EFFECTS: adds an attempt to the attempt history
    protected void addAttempt(Recipe recipe, Clock clock) {
        this.attemptHistory.add(new Attempt(recipe, clock));
    }

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
    public abstract String toString();

    // setters
    public void setInstructions(String instructions) {
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

    public void setIngredientList(ArrayList<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public void setAttemptHistory(ArrayList<Attempt> attemptHistory) {
        this.attemptHistory = attemptHistory;
    }

    // getters
    protected ArrayList<Ingredient> getIngredientList() {
        return this.ingredientList;
    }

    @JsonSerialize
    protected String getInstructions() {
        return this.instructions;
    }

    protected ArrayList<Attempt> getAttemptHistory() {
        return this.attemptHistory;
    }

    @JsonSerialize
    protected int getCookTime() {
        return this.cookTime;
    }

    @JsonSerialize
    protected int getPrepTime() {
        return this.prepTime;
    }

    @JsonSerialize
    protected int getCookTemp() {
        return this.cookTemp;
    }
}
