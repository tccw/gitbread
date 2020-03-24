package model;

import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

public class NodeGraph {
    private Node activeNode;
    private String currentBranch;

    public NodeGraph() {
    }

    public NodeGraph(Recipe recipe) throws NoSuchAlgorithmException {
        this.activeNode = new Node(recipe, "master");
        this.currentBranch = "master";
    }

    public void commit(Recipe recipe) throws NoSuchAlgorithmException {
        Node commit = new Node(recipe, this.currentBranch);
        LinkedList<Node> parents = new LinkedList<>();
        parents.add(commit);
        commit.setParents(parents);
        this.activeNode = commit;
    }

    public void newBranch(String name) {
        this.currentBranch = name;
    }

    public List<Node> getHistory() {
        LinkedList<Node> history = new LinkedList<>();
        Node root = this.activeNode;
        while (!root.isRoot()) {
            history.add(root);
            root = root.getParents().get(0);
        }
        return history;
    }


}
