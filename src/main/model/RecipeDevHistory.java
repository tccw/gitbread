package model;

import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RecipeDevHistory {

    private Commit activeCommit;
    private String currentBranch;
    private List<String> branches;
    private LinkedList<Commit> commits;

    public RecipeDevHistory(Recipe recipe) throws NoSuchAlgorithmException {
        branches = new ArrayList<String>();
        branches.add("master");
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
        if (branches.contains(branch)) {
            for (Commit c : commits) {
                if (c.getBranchLabel().equals(branch)) {
                    activeCommit = c;
                    return;
                }
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: create a new branch with the given name if one does not exist
    public void newBranch(String branch) {
        if (branches.contains(branch)) {
            System.out.println("A branch with that name already exists.");
        } else {
            branches.add(branch);
            currentBranch = branch;
        }
    }

    public boolean merge(String branch) {
        try {
            if (branch.equals(currentBranch)) {
                System.out.println("Cannot merge a branch with itself.");
                return false;
            }
            if (branches.contains(branch)) {
                this.currentBranch = branch;
                this.commit(this.activeCommit.getRecipeVersion());// add the commit
                commits.get(commits.size() - 2).setMerged(true);  // set the previous commit as merged.
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

    public List<String> getBranches() {
        return branches;
    }

    public String getCurrentBranch() {
        return currentBranch;
    }
}
