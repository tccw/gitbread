package model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.util.ArrayList;

/*
This is the abstract representation of a Recipe with common fields and methods.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
public abstract class Recipe {

    protected ArrayList<Ingredient> ingredientList = new ArrayList<Ingredient>();
    protected String instructions;
    protected String sha1;
    /*
     @JsonManagedReference tells Jackson to serialize this part of the circular reference and use it during
     deserialization to reconstruct the other side of the circular reference/bidirectional relationship. In this case,
     the other side is recipeHistory which is of type Recipe within the Attempt class.
     More here: https://www.baeldung.com/jackson-bidirectional-relationships-and-infinite-recursion
     */
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
        generateSHA1();
    }

    protected void generateSHA1() {
        try {
            this.sha1 = HashCodeMaker.sha1(this);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    protected void setCookTime(int cookTime) {
        this.cookTime = cookTime;
        generateSHA1();
    }

    protected void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
        generateSHA1();
    }

    protected void setCookTemp(int cookTemp) {
        this.cookTemp = cookTemp;
        generateSHA1();
    }

    public void setIngredientList(ArrayList<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
        generateSHA1();
    }

    public void setAttemptHistory(ArrayList<Attempt> attemptHistory) {
        this.attemptHistory = attemptHistory;
        generateSHA1();
    }

    // getters
    @JsonSerialize
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

    @JsonSerialize
    public String getSha1() {
        return this.sha1;
    }
}
