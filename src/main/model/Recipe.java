package model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.Clock;
import java.util.ArrayList;
import java.util.UUID;

/*
This is the abstract representation of a Recipe with common fields and methods.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public abstract class Recipe {

    protected ArrayList<Ingredient> ingredientList = new ArrayList<>();
    protected String instructions;
    @JsonSerialize
    @JsonDeserialize
    protected UUID id; // ID for proper object deserialization
    /*
     @JsonManagedReference tells Jackson to serialize this part of the circular reference and use it during
     deserialization to reconstruct the other side of the circular reference/bidirectional relationship. In this case,
     the other side is recipeHistory which is of type Recipe within the Attempt class.
     More here: https://www.baeldung.com/jackson-bidirectional-relationships-and-infinite-recursion
     */
    @JsonManagedReference
    protected ArrayList<Attempt> attemptHistory = new ArrayList<>();
    protected int cookTime; // in minutes
    protected int prepTime; // in minutes
    protected int cookTemp; // in F

    //EFFECTS: Generates an unique ID for the recipe so that Jackson can deserialize the different references properly
    public Recipe() {
        this.id = UUID.randomUUID();
    }

    //EFFECTS: Counts the number of elements in attemptHistory
    protected int countAttempts() {
        return this.attemptHistory.size();
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
    protected void addAttempt(Clock clock) {
        this.attemptHistory.add(new Attempt(this, clock));
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

    public String[] splitInstructions() {
        // problem here is that String.split() splits around the RegEx match so the first element is an empty string
        // because the string starts with a RegEx match (i.e. "1.")
        String temp = this.instructions.substring(2).trim(); // drop the "1." at the beginning of the instructions
        return temp.split("\\d\\d?\\.");
    }

    // setters
    public void setInstructions(String instructions) {
        this.instructions = instructions.trim();
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    public void setCookTemp(int cookTemp) {
        this.cookTemp = cookTemp;
    }

    public abstract Recipe copy();

//    public void setIngredientList(ArrayList<Ingredient> ingredientList) {
//        this.ingredientList = ingredientList;
//    }
//
//    public void setAttemptHistory(ArrayList<Attempt> attemptHistory) {
//        this.attemptHistory = attemptHistory;;
//    }

    // getters
    @JsonSerialize
    public ArrayList<Ingredient> getIngredientList() {
        return this.ingredientList;
    }

    @JsonSerialize
    public String getInstructions() {
        return this.instructions;
    }

    @JsonSerialize
    public ArrayList<Attempt> getAttemptHistory() {
        return this.attemptHistory;
    }

    @JsonSerialize
    public int getCookTime() {
        return this.cookTime;
    }

    @JsonSerialize
    public int getPrepTime() {
        return this.prepTime;
    }

    @JsonSerialize
    public int getCookTemp() {
        return this.cookTemp;
    }
}
