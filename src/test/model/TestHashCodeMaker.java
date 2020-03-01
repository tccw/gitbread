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

    @BeforeEach
    void setUp() {
        testRecipe = new BreadRecipe(1000);
        stringExpected = "425301351. Mix all ingredients " +
                "2. Knead dough until smooth " +
                "3. Let rise in oiled bowl for 1 hour " +
                "4. Knock back, shape, and let rise for 45 minutes on baking pan lightly covered " +
                "5. Bake 30 minutes at 425Fpan1.00.660.020.00.00.006";
        sha1Expected = "a23257d18ef6a34bb12cac1c1b808b3d912e5e1d";
    }

    @Test
    void TestSha1() {
        try {
            String sha1 = HashCodeMaker.sha1(testRecipe);
            String testCalculatedSHA1 = sha1Helper(stringExpected);
            assertEquals(testCalculatedSHA1, sha1);
            assertEquals(sha1Expected, sha1);
        } catch (NoSuchAlgorithmException e) {
            fail();
        }
    }

    @Test
    void TestBuildString() {
        assertEquals(stringExpected, HashCodeMaker.buildString(testRecipe));
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
