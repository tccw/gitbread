package model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashCodeMaker {

    //method take from here: http://oliviertech.com/java/generate-SHA1-hash-from-a-String/
    public static String sha1(Recipe recipe) throws NoSuchAlgorithmException {
        java.lang.String value = "";
        try {
            value = buildString(recipe);
//            System.out.println(value);
        } catch (InvocationTargetException e) {
            System.out.println("InvocationTargetException during hash generation");
        } catch (IllegalAccessException e) {
            System.out.println("IllegalAccessException during hash generation");
        }
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(value.getBytes());
        return String.format("%040x", new BigInteger(1, digest.digest()));
    }


    //MODIFIES:
    //EFFECTS:
    // help iterating through specific methods here: https://stackoverflow.com/questions/11224217/
    //TODO: determine why this is creating a different SHA-1 code every time it runs.
    public static String buildString(Recipe recipe) throws InvocationTargetException, IllegalAccessException {
        StringBuilder string = new StringBuilder();
        string.append(recipe.getCookTemp());
        string.append(recipe.getCookTime());
        string.append(recipe.getPrepTime());
        string.append(recipe.getInstructions());
        if (recipe.getClass() == BreadRecipe.class) {
            for (Method m : recipe.getClass().getMethods()) {
                if (m.getName().startsWith("get")
                        && (m.getName().contains("Fraction") || (m.getName().contains("CookingVessel")))) {
                    string.append(m.invoke(recipe));
                }
            }
        }
        String result = string.toString();
        return result;
    }

    public static byte[] buildStringBytes(Recipe recipe) throws InvocationTargetException, IllegalAccessException {
        StringBuilder string = new StringBuilder("UTF-8");
        string.append(recipe.getCookTemp());
        string.append(recipe.getCookTime());
        string.append(recipe.getPrepTime());
        string.append(recipe.getInstructions());

        /*
        Returns an array containing Method objects reflecting all the public member methods of the class or interface
        represented by this Class object, including those declared by the class or interface and those inherited from
        superclasses and superinterfaces. Array classes return all the (public) member methods inherited from the
        Object class. **The elements in the array returned are not sorted and are not in any particular order.**
        This method returns an array of length 0 if this Class object represents a class or interface that has no
        public member methods, or if this Class object represents a primitive type or void.
         */
        //TODO: fix this so that the order is maintained

        if (recipe.getClass() == BreadRecipe.class) {
            for (Method m : recipe.getClass().getMethods()) {
                if (m.getName().startsWith("get")
                        && (m.getName().contains("Fraction") || (m.getName().contains("Vessel")))) {
                    string.append(m.invoke(recipe));
                }
            }
        }
        return string.toString().getBytes();
    }
}

