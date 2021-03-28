package model;

/*
A single ingredient for any type of recipe. It has a type which is generally the name of the ingredient, and a weight
which is in grams.
 */

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class IngredientRefactor {
    // https://www.bbga.org/files/2009FormulaFormattingSINGLES.pdf
    public enum Type { FLOUR, WATER, SALT, YEAST, MILK, SUGAR, FAT, EGG, MIX_IN, SPICE }

    private Type type;
    private String name;
    private int weight; // in grams

    //REQUIRES: Ingredient must be from a known list and weight
    //          in grams must be positive.
    //EFFECTS: constructs an ingredient with a description and weight (in grams)
    @JsonCreator // needed because there is no parameterless constructor. For deserializing ingredients with Jackson.
    public IngredientRefactor(@JsonProperty("type") Type type,
                              @JsonProperty("name") String name,
                              @JsonProperty("grams") int grams) {
        this.type = type;
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

    //EFFECTS: returns the type of the ingredient
    public Type getType() {
        return this.type;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        IngredientRefactor that = (IngredientRefactor) object;
        return type == that.type &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name);
    }
}
