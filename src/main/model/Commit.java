package model;

import java.security.NoSuchAlgorithmException;

public class Commit {
    private Recipe recipeVersion;
    private String branchLabel;
    private String sha1;
    private String comment;
    private boolean isMerged;

    // a commit object containing
    public Commit(Recipe version, String  branchLabel) throws NoSuchAlgorithmException {
        this.recipeVersion = version;
        this.branchLabel =  branchLabel;
        this.sha1 = HashCodeMaker.sha1(this.recipeVersion);
        this.comment = "";
        this.isMerged = false;
    }

    public Commit(Recipe version, String  branchLabel, String comment) throws NoSuchAlgorithmException {
        this.recipeVersion = version;
        this.branchLabel =  branchLabel;
        this.sha1 = HashCodeMaker.sha1(this.recipeVersion);
        this.comment = comment;
        this.isMerged = false;
    }

    public Recipe getRecipeVersion() {
        return recipeVersion;
    }

    public void setRecipeVersion(Recipe recipeVersion) {
        this.recipeVersion = recipeVersion;
    }

    public String getBranchLabel() {
        return branchLabel;
    }

    public void setBranchLabel(String branchLabel) {
        this.branchLabel = branchLabel;
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
}
