package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

    //EFFECTS: Makes a new branch and "checks it out" so that new commits will be to this branch
    public void newBranch(String name) throws BranchAlreadyExistsException {
        if (this.mostRecentNodesByBranch.containsKey(name)) {
            throw new BranchAlreadyExistsException();
        } else {
            this.currentBranch = name;
        }
    }

    //EFFECTS: change to the most recent node of the selected branch
    public void checkout(String branch) throws BranchDoesNotExistException {
        if (!this.mostRecentNodesByBranch.containsKey(branch)) {
            throw new BranchDoesNotExistException();
        } else {
            this.currentBranch = branch;
            this.activeNode = mostRecentNodesByBranch.get(this.currentBranch);
        }
    }

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
        if (!this.mostRecentNodesByBranch.containsKey(branch)) {
            throw new BranchDoesNotExistException();
        } else {
            LinkedList<Node> path = new LinkedList<>();
            Node root = this.mostRecentNodesByBranch.get(branch);
            while (!root.isRoot()) {
                // default behavior is to follow the first parent
                path.addFirst(root);
                root = root.getParents().get(0);
            }
            path.addFirst(root);
            return path;
        }
    }

    //EFFECTS: returns the change history of the given node
    public List<Node> getNodeHistory(Node node) {
        if (!node.isRoot()) {
            LinkedList<Node> path = new LinkedList<>();
            while (!node.isRoot()) {
                // default behavior is to follow the first parent
                path.addFirst(node);
                node = node.getParents().get(0);
            }
            path.addFirst(node);
            return path;
        } else {
            return null;
        }
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

    //EFFECTS: returns a list of all attempts sorted by date (oldest -> newest)
    public List<Attempt> getAttempts() {
        List<Node> accumulator = new ArrayList<>();
        List<Attempt> attempts = new ArrayList<>();
        for (String branch : this.mostRecentNodesByBranch.keySet()) {
            Node node = this.mostRecentNodesByBranch.get(branch);
            while (!node.isRoot() && !accumulator.contains(node)) {
                attempts.addAll(node.getRecipeVersion().getAttemptHistory());
                accumulator.add(node);
                node = node.getParents().get(0);
            }
        }
        return attempts;
    }

    //EFFECTS: returns the number of nodes in the node graph
    public int size() {
        List<Node> accumulator = new ArrayList<>();
        for (String branch : this.mostRecentNodesByBranch.keySet()) {
            Node node = this.mostRecentNodesByBranch.get(branch);
            while (!node.isRoot() && !accumulator.contains(node)) {
                accumulator.add(node);
                node = node.getParents().get(0);
            }
        }
        return accumulator.size() + 1; //TODO: fix this so that the root also gets added
    }

    @Override
    public void save(FileWriter fileWriter) throws IOException {
        fileWriter.write(this.toJson());
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = JsonMapper.builder().build();
        registerObjectMapper(mapper);
        return mapper.writeValueAsString(this);
    }

    private static void registerObjectMapper(ObjectMapper mapper) {
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        mapper.registerSubtypes(
                RecipeDevCollection.class,
                Node.class,
                NodeGraph.class,
                Recipe.class,
                BreadRecipe.class,
                Attempt.class,
                Ingredient.class);
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