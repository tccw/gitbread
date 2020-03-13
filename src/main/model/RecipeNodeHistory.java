//package model;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//
//import java.security.NoSuchAlgorithmException;
//import java.time.Clock;
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//
//
//public class RecipeNodeHistory {
//    private Commit activeCommit;
//    private String currentBranch;
//    private LinkedList<Node> nodes;
//
//    public RecipeNodeHistory() {
//    }
//
//    public RecipeNodeHistory(Recipe recipe) throws NoSuchAlgorithmException {
//        nodes = new LinkedList<Node>();
//        nodes.add(new Node(recipe));
//        activeCommit = nodes.get(0);
//        currentBranch = nodes.get(0).getBranchLabel();
//    }
//
//    //MODIFIES: this
//    //EFFECTS: Commits changes to the recipe history within the currently active branch and moves the activeCommit
//    // pointer to that commit.
//    public boolean commit(Recipe r) throws NoSuchAlgorithmException {
//        if ((activeCommit.getSha1().equals(HashCodeMaker.sha1(r))
//                && currentBranch.equals(activeCommit.getBranchLabel())) || (activeCommit.isMerged())) {
//            return false; // in the case of the first commit the condition is always true...
//        } else {
//            nodes.addFirst(new Commit(r, currentBranch));
//            activeCommit = nodes.get(0);
//            return true;
//        }
//    }
//
//    //MODIFIES: this
//    //EFFECTS: checks out the most recent commit with the given branch label.
//    public void checkout(String branch) {
//        List<String> branches = this.getBranches();
//        if (branches.contains(branch)) {
//            for (Commit c : nodes) {
//                if (c.getBranchLabel().equals(branch)) {
//                    activeCommit = c;
//                    currentBranch = branch;
//                    return;
//                }
//            }
//        }
//    }
//
//    //MODIFIES: this
//    //EFFECTS: create a new branch with the given name if one does not exist
//    public void newBranch(String branch) {
//        List<String> branches = this.getBranches();
//        if (branches.contains(branch)) {
//            System.out.println("A branch with that name already exists.");
//        } else {
//            branches.add(branch);
//            currentBranch = branch;
//        }
//    }
//
//    public boolean merge(String branch) {
//        List<String> branches = this.getBranches();
//        try {
//            if (branch.equals(currentBranch)) {
//                System.out.println("Cannot merge a branch with itself.");
//                return false;
//            }
//            if (branches.contains(branch)) {
//                this.currentBranch = branch;
//                this.commit(this.activeCommit.getRecipeVersion());// add the commit
//                nodes.get(1).setMerged(true);  // set the previous commit as merged.
//                return true;
//            } else {
//                return false;
//            }
//        } catch (NoSuchAlgorithmException e) {
//            return false;
//        }
//    }
//
//    //EFFECTS: counts the number of times the recipe has been attempted
//    public int totalAttempts() {
//        int count = 0;
//        for (Commit c : this.nodes) {
//            count += c.getRecipeVersion().countAttempts();
//        }
//        return count;
//    }
//
//    //EFFECTS: attempts a recipe from the recipe history list
//    public void attempt(Clock clock) {
//        Recipe recipe = activeCommit.getRecipeVersion();
//        recipe.addAttempt(clock);
//    }
//
//    public int size() {
//        return this.nodes.size();
//    }
//
//    @JsonIgnore
//    public List<String> getBranches() {
//        List<String> result = new ArrayList<>();
//        for (int i = nodes.size() - 1; i >= 0; i--) {
//            if (!result.contains(nodes.get(i).getBranchLabel())) {
//                result.add(nodes.get(i).getBranchLabel());
//            }
//        }
//        return result;
//    }
//
//    @JsonSerialize
//    public String getCurrentBranch() {
//        return currentBranch;
//    }
//
//    @JsonSerialize
//    public LinkedList<Commit> getNodes() {
//        return nodes;
//    }
//
//    @JsonSerialize
//    public Commit getActiveCommit() {
//        return activeCommit;
//    }
//}
