package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestHashCodeMaker {
    String stringExpected;
    String sha1Expected;
    BreadRecipe testRecipe;
    Node node;

    @BeforeEach
    void setUp() {
        try {
            testRecipe = new BreadRecipe(1000);
            node = new Node(testRecipe, "master");
            stringExpected = "master"
                    + "425301351. Mix all ingredients "
                    + "2. Knead dough until smooth "
                    + "3. Let rise in oiled bowl for 1 hour "
                    + "4. Knock back, shape, and let rise for 45 minutes on baking pan lightly covered "
                    + "5. Bake 30 minutes at 425Fpan1.00.660.020.00.00.0061. Mix all ingredients 2. "
                    + "Knead dough until smooth 3. Let rise in oiled bowl for 1 hour 4. Knock back, "
                    + "shape, and let rise for 45 minutes on baking pan lightly covered 5. Bake 30 minutes at 425F";
            sha1Expected = "e1f63769ecd5b6b6c69dd9b517a7d988caf10ff8";
        } catch (NoSuchAlgorithmException e) {
            fail();
        }
    }

    @Test
    void TestSha1() {
        try {
            String sha1 = HashCodeMaker.sha1(node);
            String testCalculatedSHA1 = sha1Helper(stringExpected);
            assertEquals(testCalculatedSHA1, sha1);
            assertEquals(sha1Expected, sha1);
        } catch (NoSuchAlgorithmException e) {
            fail();
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

    @Test
    void TestDummyConstructor() {
        HashCodeMaker hashCodeMaker = new HashCodeMaker();
    }
}
