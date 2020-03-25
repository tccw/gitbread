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
        commit.addParent(this.activeNode);
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

    //EFFECTS: change to the most recent node of the selected branch
    public void checkout(String branch) {
        if (!this.mostRecentNodesByBranch.containsKey(branch)) {
            System.err.println("Throw custom BranchDoesNotExist exception, ask if you want to create one");
        } else {
            this.currentBranch = branch;
            this.activeNode = mostRecentNodesByBranch.get(this.currentBranch);
        }
    }

    //EFFECTS: merges the current branch with the given branch and checks out the merged to branch.
    //          Currently merging is only for a single user so there are no checks for merge conflicts.
    public void merge(String branch) throws NoSuchAlgorithmException {
        if (!this.mostRecentNodesByBranch.containsKey(branch)) {
            System.err.println("Throw custom BranchDoesNotExist exception, ask if you want to create one");
        } else {
            Node mergingNode = this.activeNode; // get the merging node
            this.checkout(branch);              // checkout the merged-to branch so commit goes to the correct branch
            this.commit(mergingNode.getRecipeVersion());   // commit the branch
            this.activeNode.addParent(mergingNode);        // add the merging branch as another parent
        }
    }


    public List<Node> getBranchHistory(String branch) {
        if (!this.mostRecentNodesByBranch.containsKey(branch)) {
            System.err.println("Throw custom BranchDoesNotExist exception, ask if you want to create one");
            return null;
        } else {
            LinkedList<Node> path = new LinkedList<>();
            Node root = this.mostRecentNodesByBranch.get(branch);
            while (!root.isRoot()) {
                for (Node node : root.getParents()) {
                    path.addFirst(root);
                    root = node;
                }
            }
            path.addFirst(root);
            return path;
        }
    }

    public List<Node> getNodeHistory(Node node) {
        if (!node.isRoot()) {
            LinkedList<Node> path = new LinkedList<>();
            Node current = node;
            while (!node.isRoot()) {
                for (Node n : current.getParents()) {
                    path.addFirst(current);
                    current = n;
                }
            }
            path.addFirst(current);
            return path;
        } else {
            return null;
        }
    }

    public Set<String> getBranches() {
        return this.mostRecentNodesByBranch.keySet();
    }

    //EFFECTS: traverse backwards through the graph and get the branch point for the given node. Return null otherwise.
    /*
    This method is used within merge() to determine if the merging branch has modified any of the same fields which
    were modified in the branch to be merged into after the original branch point.
     */
    private Node getBranchPoint(Node node, String mergeToBranch) {
        String branch = node.getBranchLabel();
        Node branchPoint = null;
        while (node.getBranchLabel().equals(branch)) {
            for (Node n : node.getParents()) {
                if (n.getParents().size() == 1 && n.getBranchLabel().equals(mergeToBranch)) {
                    return n;
                }
                node = n;
            }
        }
        return branchPoint;
    }


}
