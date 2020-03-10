package model;

import java.security.NoSuchAlgorithmException;

public class Commit {
    private Recipe recipeVersion;
    private String branchLabel;
    private String sha1;
    private String comment;
    private boolean isMerged;

    //default constructor for Jackson
    public Commit() {
    }

    // a commit object containing the given recipe and branch label
    public Commit(Recipe version, String branchLabel) throws NoSuchAlgorithmException {
        this.recipeVersion = version;
        this.branchLabel = branchLabel;
        this.sha1 = HashCodeMaker.sha1(this.recipeVersion);
        this.comment = "";
        this.isMerged = false;
    }

    public Commit(Recipe version, String branchLabel, String comment) throws NoSuchAlgorithmException {
        this.recipeVersion = version;
        this.branchLabel = branchLabel;
        this.sha1 = HashCodeMaker.sha1(this.recipeVersion);
        this.comment = comment;
        this.isMerged = false;
    }

    public Recipe getRecipeVersion() {
        return recipeVersion;
    }

    public String getBranchLabel() {
        return branchLabel;
    }

    public String getSha1() {
        return sha1;
    }

    public String getComment() {
        return comment;
    }

    public boolean isMerged() {
        return isMerged;
    }

    public void setMerged(boolean merged) {
        isMerged = merged;
    }

    public void setBranchLabel(String branchLabel) {
        this.branchLabel = branchLabel;
    }

    public void setRecipeVersion(Recipe recipeVersion) {
        this.recipeVersion = recipeVersion;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
