package model;

import java.util.HashMap;

public class RecipeCollection {

    HashMap<String, Recipe> collection;

    //EFFECTS creates and adds a new recipe to the recipe collection
    public void addNewRecipe(Recipe recipe) {
        collection = new HashMap<String, Recipe>();
        // create a new RecipeHistory "repository"

    }

    //EFFECTS: return a formatted string with all recipe names, cook time, and prep time
    public String toString() {
        return ""; //stub
    }
}

