package model;

import java.security.NoSuchAlgorithmException;
import java.util.*;

public class GraphHistory {
    private Node activeNode;
    private String currentBranch;
    private Map<Node, List<Node>> nodes;

    public GraphHistory() {
    }

    public GraphHistory(Recipe recipe) throws NoSuchAlgorithmException {
        Node root = new Node(recipe, "master");
        this.activeNode = root;
        this.currentBranch = "master";
        this.nodes = new LinkedHashMap<>();
        this.nodes.put(root, new LinkedList<Node>()); // only the root commit has an empty parents list
    }

    public void commit(Recipe recipe) throws NoSuchAlgorithmException {
        Node node = new Node(recipe, this.currentBranch);
        LinkedList<Node> parents = new LinkedList<>();
        parents.add(this.activeNode);
        this.nodes.put(node, parents);
        this.activeNode = node;
    }

    public List<Node> getParents(Node node) {
        return this.nodes.get(node); //may need to override equals and hashcode
    }

    public Node getRoot() throws NoSuchAlgorithmException {
        Node root = activeNode;
        while (!this.nodes.get(root).isEmpty()) {
            root = this.nodes.get(root).get(0);
        }
        return root;
    }

    public void newBranch(String branchName) {
        this.currentBranch = branchName;
    }

    public int size() {
        return this.nodes.size();
    }



}
