package model;

import java.util.Objects;

public class IngredientEntry {
    private IngredientRefactor ingredient;
    private double bakerPercentage;

    public IngredientEntry(IngredientRefactor ingredient) {
        this.ingredient = ingredient;
        this.bakerPercentage = -1;
    }

    // Getters and Setters


    public IngredientRefactor getIngredient() {
        return ingredient;
    }

    public void setIngredient(IngredientRefactor ingredient) {
        this.ingredient = ingredient;
    }

    public double getBakerPercentage() {
        return bakerPercentage;
    }

    public void setBakerPercentage(double bakerPercentage) {
        this.bakerPercentage = bakerPercentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientEntry that = (IngredientEntry) o;
        return ingredient.equals(that.ingredient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredient);
    }
}
