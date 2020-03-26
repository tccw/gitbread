package model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

public class Node {
    /*
    Each node points to its parent node. Nodes can have multiple parents (branches that merge).
    Only the root will have an empty parents list.
    A node can be the parent of any number of nodes, but each node will only ever have a maximum of two parents
    root<---node1<---node2<---node3
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
        this.sha1 = HashCodeMaker.sha1(this.recipeVersion);
        this.parents = new LinkedList<>(); // root is the only node with no parents (only empty node)
    }

    //EFFECTS: check if the Node is the initial (root) node
    @JsonIgnore
    public boolean isRoot() {
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

//    public void setBranchLabel(String branchLabel) {
//        this.branchLabel = branchLabel;
//    }

//    public void setRecipeVersion(Recipe recipeVersion) {
//        this.recipeVersion = recipeVersion;
//    }

//    public void setSha1(String sha1) {
//        this.sha1 = sha1;
//    }

}
