package model;

/*
A single ingredient for any type of recipe. It has a type which is generally the name of the ingredient, and a weight
which is in grams.
 */

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Ingredient {
    String type;
    int weight; // in grams

    //REQUIRES: Ingredient must be from a known list (flour, water, salt, sugar, eggs, yeast, preferment) and weight
    //          in grams must be positive.
    //EFFECTS: constructs an ingredient with a description and weight (in grams)
    @JsonCreator // needed because there is not parameterless constructor. For deserializing ingredients with Jackson.
    public Ingredient(@JsonProperty("type") String type, @JsonProperty("grams") int grams) {
        this.type = type;
        this.weight = grams;
    }

    //EFFECTS:
    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setType(String type) {
        this.type = type;
    }

    //EFFECTS: returns the weight of an ingredient in grams
    public int getWeight() {
        return this.weight;
    }

    //EFFECTS: returns the type of ingredient
    public String getType() {
        return this.type;
    }
}
