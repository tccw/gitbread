package model;

import java.lang.reflect.Array;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/*
A single node representing a point in the history of the development of a recipe.
 */
public class Node {
    String branch;
    ArrayList<Node> parents;
    Recipe recipeVersion;
    String recipeSHA1;

    //EFFECTS: initializes a node which is root with the default branch name "master"
    public Node() {
        branch = "master";
        parents = new ArrayList<>(); // only the root will have an empty parents list
        recipeVersion = null;
        recipeSHA1 = null;
    }

    //EFFECTS: initializes a node which is root with the default branch name "master" and assigns a starting recipe
    public Node(Recipe recipe) throws NoSuchAlgorithmException {
        branch = "master";
        parents = new ArrayList<>();
        recipeVersion = recipe;
        recipeSHA1 = HashCodeMaker.sha1(recipe);
    }

    //EFFECTS: mak

    private void commitChange(Recipe recipe) {

    }
}
