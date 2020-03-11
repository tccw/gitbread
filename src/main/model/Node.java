package model;

import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

public class Node {
    private Recipe recipeVersion;
    private String branchLabel;
    private String sha1;
    private List<Node> parents;

    /*
    Each node points to its parent node. Nodes can have multiple parents (branches that merge).
    Only the root will have an empty parents list.
    root<---node1<---node2<---node3
     */

    public Node(Recipe recipeVersion) throws NoSuchAlgorithmException {
        this.recipeVersion = recipeVersion;
        this.branchLabel = "master";
        this.sha1 = HashCodeMaker.sha1(this.recipeVersion);
        this.parents = new LinkedList<Node>(); // root pointer list is always empty
    }

    public void commit(Node node) {
        // stub
    }

}
