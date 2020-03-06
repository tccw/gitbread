package model;
/*
- Use a singly-linked list. In order to make references point backwards use the .add(index, value) method and always add
to the 0th index.
- Make a list of references to serve as the branches (can add as many new branches as you want)
- Make a psuedoHEAD pointer called activeVersion that can be changed to point at whatever the user wants in order to
view that version of the recipe
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// git is a directed acyclic graph
public class Repository {
    private List<Recipe> branches; //should this be a hashmap so that I can also store the string name of the branch?
    private Recipe activeBranch;
    private List<Recipe> history;

    public Repository() {
        branches = new ArrayList<>();
        activeBranch = null;
        history = new LinkedList<>();
    }

    public void add(Recipe recipe) {
        // add the new version to the front of the linkedlist.
        // This allows for effectively backwards references to previous versions
        // Still not sure how to handle branch history here as linked lists always point to the next item in the list
        // I don't yet understand how I could make the references skip. Maybe add a flag to tell me which branch a
        // recipe is in?
        history.add(0, recipe);
    }
}
