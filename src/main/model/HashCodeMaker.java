package model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashCodeMaker {

    //method take from here: http://oliviertech.com/java/generate-SHA1-hash-from-a-String/
    //EFFECTS: calculates a SHA-1 hashcode for version control
    //TODO: pass this method the branch label as well
    @JsonProperty("node")
    public static String sha1(Node node) throws NoSuchAlgorithmException {
        String value = buildString(node);
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(value.getBytes());
        return String.format("%040x", new BigInteger(1, digest.digest()));
    }

    // help iterating through specific methods here: https://stackoverflow.com/questions/11224217/
    // the stackoverflow link uses getMethods() which does not return the methods in any particular order
    //REQUIRES:
    //EFFECTS: generates a string from a recipe to be used to calculate an SHA-1 hashcode for version control. For the
    // purposes of a breadRecipe, only changes in the Baker's Percentages, the instructions, cooking vessel,
    // or cook/prep time and cooking temperature are considered version changes. This code focuses on the content
    // of the fields, and is not the same as the Java hashcode which is unique to an instance of an object.
    private static String buildString(Node node) {
        Recipe recipe = node.getRecipeVersion();
        StringBuilder string = new StringBuilder();
        string.append(node.getBranchLabel());
        string.append(recipe.getCookTemp());
        string.append(recipe.getCookTime());
        string.append(recipe.getPrepTime());
        string.append(recipe.getInstructions());
        if (recipe.getClass() == BreadRecipe.class) {
            string.append(breadBuilder((BreadRecipe) (recipe)));
        }
        //TODO: add the branch to this to make more unique SHA1 codes
        return string.toString();
    }

    private static String breadBuilder(BreadRecipe r) {
        return r.getCookingVessel()
                + r.getFlourFraction()
                + r.getWaterFraction()
                + r.getSaltFraction()
                + r.getSugarFraction()
                + r.getFatFraction()
                + r.getYeastFraction()
                + r.getInstructions();
    }
}

