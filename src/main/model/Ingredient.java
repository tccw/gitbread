package model;

/*
A single ingredient for any type of recipe. It has a type which is generally the name of the ingredient, and a weight
which is in grams.
 */

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Ingredient {
    String name;
    int weight; // in grams

    //REQUIRES: Ingredient must be from a known list (flour, water, salt, sugar, eggs, yeast, preferment) and weight
    //          in grams must be positive.
    //EFFECTS: constructs an ingredient with a description and weight (in grams)
    @JsonCreator // needed because there is not parameterless constructor. For deserializing ingredients with Jackson.
    public Ingredient(@JsonProperty("name") String name,
                      @JsonProperty("grams") int grams) {
        this.name = name;
        this.weight = grams;
    }

    //EFFECTS:
    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setName(String name) {
        this.name = name;
    }

    //EFFECTS: returns the weight of an ingredient in grams
    public int getWeight() {
        return this.weight;
    }

    //EFFECTS: returns the name of ingredient
    public String getName() {
        return this.name;
    }

}
