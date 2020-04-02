package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.BranchAlreadyExistsException;
import exceptions.BranchDoesNotExistException;
import persistence.Saveable;

import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.util.*;

public class NodeGraph implements Saveable {
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

    //MODIFIES: this
    //EFFECTS: Makes a new branch and "checks it out" so that new commits will be to this branch
    public void newBranch(String name) throws BranchAlreadyExistsException {
        if (this.mostRecentNodesByBranch.containsKey(name)) {
            throw new BranchAlreadyExistsException();
        } else {
            this.currentBranch = name;
        }
    }

    //MODIFIES: this
    //EFFECTS: change to the most recent node of the selected branch
    public void checkout(String branch) throws BranchDoesNotExistException {
        if (!this.mostRecentNodesByBranch.containsKey(branch)) {
            throw new BranchDoesNotExistException();
        } else {
            this.currentBranch = branch;
            this.activeNode = mostRecentNodesByBranch.get(this.currentBranch);
        }
    }

    //EFFECTS:
//    public Node getBySHA(String sha1) {
//        // stub
//        for (String branch : this.mostRecentNodesByBranch.keySet()) {
//            List<Node> nodes = this.getBranchHistory(branch);
//
//        }
//    }

    //MODIFIES: this
    //EFFECTS: merge the given branch into the current branch.
    //         Currently merging is only for a single user so there are no checks for merge conflicts.
    public void merge(String branch) throws NoSuchAlgorithmException, BranchDoesNotExistException {
        if (!this.mostRecentNodesByBranch.containsKey(branch)) {
            throw new BranchDoesNotExistException();
        } else {
            Node mergingNode = this.mostRecentNodesByBranch.get(branch); // get the merging node
            this.commit(mergingNode.getRecipeVersion().copy());          // commit the branch
            this.activeNode.addParent(mergingNode);                      // add the merging branch as another parent
        }
    }

    //EFFECTS: returns the change history of the given branch
    public List<Node> getBranchHistory(String branch) throws BranchDoesNotExistException {
        LinkedList<Node> path = new LinkedList<>();
        if (!this.mostRecentNodesByBranch.containsKey(branch)) {
            throw new BranchDoesNotExistException();
        } else {
            Node node = this.mostRecentNodesByBranch.get(branch);
            buildFirstParentPath(path, node);
            return path;
        }
    }

    //EFFECTS: returns the change history of the given node
    public List<Node> getNodeHistory(Node node) {
        LinkedList<Node> path = new LinkedList<>();
        if (!node.isInit()) {
            buildFirstParentPath(path, node);
            return path;
        } else {
            return null;
        }
    }

    //EFFECTS: builds a complete path
    private void buildFirstParentPath(LinkedList<Node> path, Node node) {
        while (!node.isInit()) {
            // default behavior is to follow the first parent
            path.addFirst(node);
            node = node.firstParent();
        }
        path.addFirst(node);
    }

    @JsonIgnore
    public Set<String> getBranches() {
        return this.mostRecentNodesByBranch.keySet();
    }

    public void attempt(Clock clock) {
        activeNode.getRecipeVersion().addAttempt(clock);
    }

    //EFFECTS: returns the total number of attempts (for all recipe versions) in a node graph
    public int totalAttempts() {
        return getAttempts().size();
    }

    //EFFECTS: returns a list of all attempts sorted by date (most recent -> oldest)
    public List<Attempt> getAttempts() {
        List<Node> accumulator = new ArrayList<>();
        List<Attempt> attempts = new ArrayList<>();
        for (String branch : this.mostRecentNodesByBranch.keySet()) {
            Node node = this.mostRecentNodesByBranch.get(branch);
            while (!node.isInit() && !accumulator.contains(node)) {
                attempts.addAll(node.getRecipeVersion().getAttemptHistory());
                accumulator.add(node);
                node = node.firstParent();
            } // likely don't need this but there were edge cases I didn't fully understand which this solved.
            if (node.isInit() && !attempts.containsAll(node.getRecipeVersion().getAttemptHistory())) {
                attempts.addAll(node.getRecipeVersion().getAttemptHistory());
            }
        }
        Collections.sort(attempts);
        Collections.reverse(attempts);
        return attempts;
    }

    public Node getNodeOfAttempt(Attempt attempt) {
        List<Node> accumulator = new ArrayList<>();
        for (String branch : this.mostRecentNodesByBranch.keySet()) {
            Node node = this.mostRecentNodesByBranch.get(branch);
            while (!node.isInit() && !accumulator.contains(node)) {
                if (node.getRecipeVersion().getAttemptHistory().contains(attempt)) {
                    return node;
                }
                accumulator.add(node);
                node = node.firstParent();
            }
            if (node.isInit() && !accumulator.contains(node)
                    && node.getRecipeVersion().getAttemptHistory().contains(attempt)) {
                return node;
            }
        }
        return null;
    }

    //EFFECTS: returns the number of nodes in the node graph
    public int size() {
        List<Node> accumulator = new ArrayList<>();
        for (String branch : this.mostRecentNodesByBranch.keySet()) {
            Node node = this.mostRecentNodesByBranch.get(branch);
            while (!node.isInit() && !accumulator.contains(node)) {
                accumulator.add(node);
                node = node.firstParent();
            }
        }
        return accumulator.size() + 1; //TODO: fix this so that the root also gets added
    }

    @Override
    public void save(FileWriter fileWriter) throws IOException {
        fileWriter.write(this.toJson());
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = ObjectMapperSingleton.getInstance();
        return mapper.writeValueAsString(this);
    }

    public Node getActiveNode() {
        return activeNode;
    }

    public String getCurrentBranch() {
        return currentBranch;
    }

    public Map<String, Node> getMostRecentNodesByBranch() {
        return mostRecentNodesByBranch;
    }
}