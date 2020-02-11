package model;
/*
Represents the version history for a single Recipe. This is represented by a RecipeMap which is a HashMap of recipes
labeled with unique identifiers. It also has two Recipe objects which point to the "master", or default view recipe,
and a "testing" recipe which points to some variation of the original recipe where a user might be testing changes to a
recipe which is not final yet.
*/

import java.util.LinkedList;

public class RecipeHistory {

    Recipe masterRecipe;
    Recipe testingRecipe;
    LinkedList<Recipe> recipeHistory;

    //REQUIRES:
    //MODIFIES:
    //EFFECTS: Creates an empty RecipeHistory by initializing an empty RecipeMap.
    public RecipeHistory() {
        masterRecipe = null;
        testingRecipe = null;
        recipeHistory = new LinkedList<Recipe>();
    }

    // getters
    public Recipe getMasterRecipe() {
        return masterRecipe;
    }

    public Recipe getTestingRecipe() {
        return testingRecipe;
    }

    public LinkedList<Recipe> getRecipeHistory() {
        return recipeHistory;
    }

    // setters
    public void setMasterRecipe(Recipe master) {
        this.masterRecipe = master;
    }

    public void setTestingRecipe(Recipe testing) {
        this.testingRecipe = testing;
    }

    //EFFECTS: counts the number of times the recipe has been attempted
    public int countAttempts() {
        // first version can simply return the length of the AttemptsList
        // future version might want to return attempts per recipe version
        int count = 0;
        for (Recipe r : this.recipeHistory) {
            count += r.countAttempts();
        }
        return count;
    }

    //REQUIRES: recipeHistory length > 0
    //EFFECTS: print out the number of times a recipe has been modified, ignoring the first entry (initial 'commit')
    public int countTimesModified() {
        return recipeHistory.size() - 1;
    }

    //EFFECTS: add a recipe version to the recipe list
    public void addToHistory(Recipe newVersion) {
        recipeHistory.add(newVersion);
    }


    //REQUIRES:
    //MODIFIES:
    //EFFECTS: Returns a formatted string of all recipe attempts in the format below
    // example recipe: https://www.kingarthurflour.com/recipes/hearth-bread-recipe
    /*

    ------------ Attempt 0 ------------

    Date attempted: Saturday, February 8, 2020
    Weather for day: Clear throughout the day, High: 10C, Low: 0C, RH: 65%, chance of rain 0%
    Weather at start: Clear, 4C, RH 87%, chance of rain: 0%

    Recipe Version: Hearth Bread v0

    Ingredients:
        - Active-dry-yeast, 7g
        - Sugar, 14g
        - Salt, 14g
        - Water (lukewarm), 454g
        - All-purpose flour, 663g

    Hydration: 68%

    Prep: 15 minutes
    Rise time: 1 hr 45 minutes
    Bake: 30 minutes
    Total: 2 hr 30 minutes
    Bake temp: 425 F
    Baking vessel: pan
    Yield: 2 loaves (a loaf is considered ~550g of ingredients)

    Instructions:
        1. Mix all ingredients together until the dough pulls away from the bowl, adding more flour if necessary.
        2. Knead the dough until smooth (~ 7 minutes in mixer, varies by hand)
        3. Place dough in a greased mixing bowl and let rise until doubled in size (1-2 hours)
        4. Deflate dough, cut in half, shape into two ovals and place on a baking sheet. Lightly cover with greased
        plastic wrap and let rise for 45 minutes.
        5. Score the tops of the loaves and brush with lukewarm water.
        6. Bake for 25-35 minutes, until the crust is golden brown and sounds hollow to the touch.
        7. Remove the loaves from the oven, take them off the pan and return them to the oven. Turn off the oven, crack
        the door open and let loaves cool.
        8. Store completely cooled bread in a paper bag at room temp for several days. For longer storage wrap well and
        freeze.

    Result notes:
        - Crumb: open, springy
        - Crust: golden, crunchy
        - Flavor: perfect

     */
    public String viewAttempts() {
        // returns a formatted string of all attempts
        return "";
    }

    //EFFECTS: returns a formatted string with all the different recipe version names and dates, most recent first.
    /*
    ------ RECIPE DEVELOPMENT HISTORY ------
    Hearth Bread v2 (testing) : Saturday, February 8, 2020
    Hearth Bread v1 (master) : Tuesday, February 4, 2020
    Hearth Bread v0 : Monday, January 27, 2020

     */
    public String viewRecipeVersionList() {
        return "";
    }


}
