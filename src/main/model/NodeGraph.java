package model;

import java.security.NoSuchAlgorithmException;
import java.util.*;

public class NodeGraph {
    private Node activeNode;
    private String currentBranch;
    private Map<String, Node> mostRecentNodesByBranch;

    public NodeGraph() {
    }

    public NodeGraph(Recipe recipe) throws NoSuchAlgorithmException {
        this.activeNode = new Node(recipe, "master");
        this.currentBranch = "master";
        mostRecentNodesByBranch = new HashMap<>();
        mostRecentNodesByBranch.put("master", this.activeNode);
    }

    public void commit(Recipe recipe) throws NoSuchAlgorithmException {
        Node commit = new Node(recipe, this.currentBranch);
        LinkedList<Node> parents = new LinkedList<>();
        parents.add(this.activeNode);
        commit.setParents(parents);
        this.activeNode = commit;
        if (this.mostRecentNodesByBranch.get(this.currentBranch) != null) {
            mostRecentNodesByBranch.replace(this.currentBranch, this.activeNode);
        } else {
            mostRecentNodesByBranch.put(this.currentBranch, this.activeNode);
        }
    }

    //EFFECTS: Makes a new branch and "checks it out" so that new commits will be to this branch
    public void newBranch(String name) {
        if (this.mostRecentNodesByBranch.containsKey(name)) {
            System.out.println("Throw the new exception from before here");
        } else {
            this.currentBranch = name;
        }
    }

    //EFFECTS:
    public void checkout(String branch) {
        if (!this.mostRecentNodesByBranch.containsKey(branch)) {
            System.out.println("Throw custom BranchDoesNotExist exception, ask if you want to create one");
        } else {
            this.currentBranch = branch;
            this.activeNode = mostRecentNodesByBranch.get(this.currentBranch);
        }
    }

    //EFFECTS: merges changes from one branch to another
    public void merge(Node node, String branch) {
        // check which fields have been modified since the branch. If there are any fields that both branches have
        // modified ask the user which they want to keep
        // create a new  node with two parents (the most recent commit of the merging branch and of the merged branch)
        //stub
    }

    public List<Node> getHistory(String branch) {
        if (!this.mostRecentNodesByBranch.containsKey(branch)) {
            return null;
        } else {
            LinkedList<Node> path = new LinkedList<>();
            Node root = this.activeNode;
            while (!root.isRoot()) {
                for (Node node : root.getParents()) {
                    if (root.getBranchLabel().equals(branch)) {
                        path.addFirst(root);
                    }
                    root = node;
                }
            }
            path.addFirst(root);
            return path;
        }
    }

    public Set<String> getBranches() {
        return this.mostRecentNodesByBranch.keySet();
    }


}
