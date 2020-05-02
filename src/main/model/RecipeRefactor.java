package model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.Clock;
import java.util.*;

/*
This is the abstract representation of a Recipe with common fields and methods.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public abstract class RecipeRefactor implements Iterable<IngredientEntry> {

    protected List<IngredientEntry> ingredientEntries;
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
    protected List<AttemptRefactor> attemptHistory;
    protected List<RecipeRefactor> subRecipes;
    protected int cookTime; // in minutes
    protected int prepTime; // in minutes
    protected int cookTemp; // in F?

    //EFFECTS: Generates an unique ID for the recipe so that Jackson can deserialize the different references properly
    public RecipeRefactor() {
        this.id = UUID.randomUUID();
        this.instructions = "";
        this.ingredientEntries = new ArrayList<>();
        this.attemptHistory = new ArrayList<>();
        this.subRecipes = new ArrayList<>();
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
        return this.ingredientEntries.size();
    }

    //MODIFIES: this
    //EFFECTS: adds an attempt to the attempt history
    protected void addAttempt(Clock clock) {
        this.attemptHistory.add(new AttemptRefactor(this, clock));
    }

    //EFFECTS: produce the weight of the selected ingredient in grams
    public int getIngredientWeight(String ingredientName) {
        for (IngredientEntry i : this.ingredientEntries) {
            if (i.getIngredient().getName().equals(ingredientName.toLowerCase())) {
                return i.getIngredient().getWeight();
            }
        }
        return -1; //System.out.printf("The ingredient \"%s\" could not be found", ingredientName);
    }

    //MODIFIES: this
    //EFFECTS: adds an ingredient to the list of ingredients
    public void addIngredient(IngredientRefactor ingredient) {
        if (!this.ingredientEntries.contains(new IngredientEntry(ingredient))) {
            this.ingredientEntries.add(new IngredientEntry(ingredient));
        }
    }

    public void addSubRecipe(RecipeRefactor recipe) {
        if (!this.subRecipes.contains(recipe)) {
            this.subRecipes.add(recipe);
        }
    }

    //EFFECTS: this
    public abstract String toString();

    public abstract List<String> splitInstructions();

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

    public void setIngredientEntries(List<IngredientEntry> ingredientEntries) {
        this.ingredientEntries = ingredientEntries;
    }

    public void setSubRecipes(List<RecipeRefactor> subRecipes) {
        this.subRecipes = subRecipes;
    }

    public abstract RecipeRefactor copy();

//    public void setIngredientList(ArrayList<Ingredient> ingredientList) {
//        this.ingredientList = ingredientList;
//    }
//
//    public void setAttemptHistory(ArrayList<Attempt> attemptHistory) {
//        this.attemptHistory = attemptHistory;;
//    }

    // getters
    @JsonSerialize
    public List<IngredientEntry> getIngredientEntries() {
        return this.ingredientEntries;
    }

    @JsonSerialize
    public String getInstructions() {
        return this.instructions;
    }

    @JsonSerialize
    public List<AttemptRefactor> getAttemptHistory() {
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

    public List<RecipeRefactor> getSubRecipes() {
        return subRecipes;
    }

    //EFFECTS: returns the iterator for
    @Override
    public Iterator<IngredientEntry> iterator() {
        return new IngredientsIterator();
    }

    public class IngredientsIterator implements Iterator<IngredientEntry> {
        private Iterator<IngredientEntry> ingredientsEntryIterator = ingredientEntries.iterator();
        private List<Iterator<IngredientEntry>> subRecipesIterators = new ArrayList<>();
        private IngredientEntry cursor;

        for (RecipeRefactor r : subRecipes) { //not sure why this doesn't see subRecipes field from above
           subRecipesIterators.add(r.getIngredientEntries().iterator());
        }

        @Override
        public boolean hasNext() {
            for (Iterator<IngredientEntry> it : subRecipesIterators) {
                if (it.hasNext()) {
                    cursor = it.next();
                    return true;
                }
            }
            return false;
        }

        @Override
        public IngredientEntry next() {
            return cursor;
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeRefactor that = (RecipeRefactor) o;
        return cookTime == that.cookTime &&
                prepTime == that.prepTime &&
                cookTemp == that.cookTemp &&
                Objects.equals(ingredientEntries, that.ingredientEntries) &&
                Objects.equals(instructions, that.instructions) &&
                id.equals(that.id) &&
                Objects.equals(attemptHistory, that.attemptHistory) &&
                Objects.equals(subRecipes, that.subRecipes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredientEntries, instructions, id, attemptHistory, subRecipes, cookTime, prepTime, cookTemp);
    }
}
