package model;

public class Ingredient {
    String type;
    int weight;

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
