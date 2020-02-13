package model;

/*
A single ingredient for any type of recipe. It has a type which is generally the name of the ingredient, and a weight
which is in grams.
 */

public class Ingredient {
    String type;
    int weight; // in grams

    //REQUIRES: Ingredient must be from a known list (flour, water, salt, sugar, eggs, yeast, preferment)
    //EFFECTS: constructs an ingredient with a description and weight (in grams)
    public Ingredient(String type, int grams) {
        this.type = type;
        this.weight = grams;
    }

    //EFFECTS:
    public void setWeight(int weight) {
        this.weight = weight;
    }

    //EFFECTS: returns the weight of an ingredient in grams
    public int getWeight() {
        return this.weight; //stub
    }

    //EFFECTS: returns the type of ingredient
    public String getType() {
        return this.type;
    }
}
