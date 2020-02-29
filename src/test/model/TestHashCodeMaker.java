package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestHashCodeMaker {
    String expected;
    BreadRecipe testRecipe;

    @BeforeEach
    void setUp() {
        testRecipe = new BreadRecipe(1000);
        expected = "425301351. Mix all ingredients " +
                "2. Knead dough until smooth " +
                "3. Let rise in oiled bowl for 1 hour " +
                "4. Knock back, shape, and let rise for 45 minutes on baking pan lightly covered " +
                "5. Bake 30 minutes at 425Fpan0.0061.00.020.660.00.0";
    }

    @Test
    void TestSha1() {
        try {
            String sha1 = HashCodeMaker.sha1(testRecipe);
            String sha1Expected = sha1Helper(expected);
            assertEquals(sha1Expected, sha1);
        } catch (NoSuchAlgorithmException e) {
            fail();
        }

    }

    @Test
    void TestBuildString() {
        try {
            assertEquals(expected, HashCodeMaker.buildString(testRecipe));
        } catch (InvocationTargetException | IllegalAccessException e) {
            fail();
            e.printStackTrace();
        }
    }

    private String sha1Helper(String value) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            fail("Unexpected NoSuchAlgorithmException");
            e.printStackTrace();
        }
        digest.reset();
        digest.update(value.getBytes());
        return String.format("%040x", new BigInteger(1, digest.digest()));
    }
}
