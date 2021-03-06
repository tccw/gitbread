package model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

public class Node {
    /*
    Each node points to its parent node. Nodes will normally have at most of two parents (branches that merge) but
    can technically have an arbitrary number of parent nodes.
    Only the initial node will have an empty parents list.
                                        a<--a<--a
                                       /          \
                                  t<--t<--t        \
                                 /                  \
                    init<--m<--m<--m<--m<-----------m<--m<--m
                                                    ^
                                                    |
                                               two parents
     */

    private Recipe recipeVersion;
    private String branchLabel;
    private String sha1;
    private List<Node> parents;

    //default constructor for Jackson
    public Node() {
    }

    // a commit object containing the given recipe and branch label
    public Node(Recipe version, String branchLabel) throws NoSuchAlgorithmException {
        this.recipeVersion = version;
        this.branchLabel = branchLabel;
        this.sha1 = HashCodeMaker.sha1(this);
        this.parents = new LinkedList<>(); // root is the only node with no parents (only empty node)
    }

    //REQUIRES: Should always be called in conjunction with isRoot(). This is similar to the hasNext() and next()
    //          methods from Iterable.
    //EFFECTS: returns the first parent of the Node for traversal
    public Node firstParent() {
        return this.getParents().get(0);
    }

    //EFFECTS: check if the Node is the initial (root) node
    @JsonIgnore
    public boolean isInit() {
        return this.parents.isEmpty();
    }

    public void addParent(Node parent) {
        this.parents.add(parent);
    }

    public List<Node> getParents() {
        return parents;
    }

    public Recipe getRecipeVersion() {
        return recipeVersion;
    }

    public String getBranchLabel() {
        return branchLabel;
    }

    public String getSha1() {
        return sha1;
    }

}
