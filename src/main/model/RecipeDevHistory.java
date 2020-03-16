package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;



public class RecipeDevHistory {
    private Commit activeCommit;
    private String currentBranch;
    private LinkedList<Commit> commits;

    public RecipeDevHistory() {
    }

    public RecipeDevHistory(Recipe recipe) throws NoSuchAlgorithmException {
        commits = new LinkedList<Commit>();
        commits.add(new Commit(recipe, "master"));
        activeCommit = commits.get(0);
        currentBranch = commits.get(0).getBranchLabel();
    }

    //MODIFIES: this
    //EFFECTS: Commits changes to the recipe history within the currently active branch and moves the activeCommit
    // pointer to that commit.
    public boolean commit(Recipe r) throws NoSuchAlgorithmException {
        if ((activeCommit.getSha1().equals(HashCodeMaker.sha1(r))
                && currentBranch.equals(activeCommit.getBranchLabel())) || (activeCommit.isMerged())) {
            return false; // in the case of the first commit the condition is always true...
        } else {
            commits.addFirst(new Commit(r, currentBranch));
            activeCommit = commits.get(0);
            return true;
        }
    }

    //MODIFIES: this
    //EFFECTS: checks out the most recent commit with the given branch label.
    public void checkout(String branch) {
        List<String> branches = this.getBranches();
        if (branches.contains(branch)) {
            for (Commit c : commits) {
                if (c.getBranchLabel().equals(branch)) {
                    activeCommit = c;
                    currentBranch = branch;
                    return;
                }
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: create a new branch with the given name if one does not exist
    public void newBranch(String branch) {
        List<String> branches = this.getBranches();
        if (branches.contains(branch)) {
            System.err.println("A branch with that name already exists.");
        } else {
            branches.add(branch);
            currentBranch = branch;
        }
    }

    public boolean merge(String branch) {
        List<String> branches = this.getBranches();
        try {
            if (branch.equals(currentBranch)) {
                System.err.println("Cannot merge a branch with itself.");
                return false;
            }
            if (branches.contains(branch)) {
                this.currentBranch = branch;
                this.commit(this.activeCommit.getRecipeVersion());// add the commit
                commits.get(1).setMerged(true);  // set the previous commit as merged.
                return true;
            } else {
                return false;
            }
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    //EFFECTS: counts the number of times the recipe has been attempted
    public int totalAttempts() {
        int count = 0;
        for (Commit c : this.commits) {
            count += c.getRecipeVersion().countAttempts();
        }
        return count;
    }

    //EFFECTS: attempts a recipe from the recipe history list
    public void attempt(Clock clock) {
        Recipe recipe = activeCommit.getRecipeVersion();
        recipe.addAttempt(clock);
    }

    public int size() {
        return this.commits.size();
    }

    @JsonIgnore
    public List<String> getBranches() {
        List<String> result = new ArrayList<>();
        for (int i = commits.size() - 1; i >= 0; i--) {
            if (!result.contains(commits.get(i).getBranchLabel())) {
                result.add(commits.get(i).getBranchLabel());
            }
        }
        return result;
    }

    @JsonSerialize
    public String getCurrentBranch() {
        return currentBranch;
    }

    @JsonSerialize
    public LinkedList<Commit> getCommits() {
        return commits;
    }

    @JsonSerialize
    public Commit getActiveCommit() {
        return activeCommit;
    }
}
