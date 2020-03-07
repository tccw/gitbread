package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

public class TestRecipeDevHistory {

    private Clock clock = Clock.fixed(LocalDateTime.of(2020, 2,14,12,10)
            .toInstant(ZoneOffset.UTC), ZoneId.of("UTC"));
    private Recipe testRecipe;
    private RecipeDevHistory repo;


    @BeforeEach
    void setUp() {
        testRecipe = new BreadRecipe(1000);
        try {
            repo = new RecipeDevHistory(testRecipe);
        } catch (NoSuchAlgorithmException e) {
            fail("Unexpected NoSuchAlgorithmException.");
        }
    }

    @Test
    void TestAddSameBranchRecipeDevHistory() {
        try {
            assertTrue(repo.commit(new BreadRecipe(550, 0.56)));
            assertEquals(2, repo.size());
        } catch (NoSuchAlgorithmException e) {
            fail("Unexpected NoSuchAlgorithmException.");
        }
    }

    @Test
    void TestAddSameBranchSameRecipeFail() {
        try {
            assertFalse(repo.commit(new BreadRecipe(1000)));
            assertFalse(repo.commit(new BreadRecipe(504)));
        } catch (NoSuchAlgorithmException e) {
            fail("Unexpected NoSuchAlgorithmException.");
        }
    }

    @Test
    void TestMultiAddSameBranchRecipeDevHistory() {
        try {
            assertTrue(repo.commit(new BreadRecipe(550, 0.67)));
            assertTrue(repo.commit(new BreadRecipe(550, 0.43)));
            assertTrue(repo.commit(new BreadRecipe(550, 072)));
            assertEquals(4, repo.size());
            assertEquals(1, repo.getBranches().size());
            assertEquals("master", repo.getCurrentBranch());
        } catch (NoSuchAlgorithmException e) {
            fail("Unexpected NoSuchAlgorithmException.");
        }
    }

    @Test
    void TestCreateNewBranchNoCommit() {
        repo.newBranch("high-hydration-test");
        assertEquals("high-hydration-test", repo.getCurrentBranch());
        assertEquals(2, repo.getBranches().size());
        assertEquals(1, repo.size());
    }

    @Test
    void TestCreateNewBranchMultiCommit() {
        try {
            repo.newBranch("low-hydration-test");
            repo.commit(new BreadRecipe(400, 0.45));
            repo.commit(new BreadRecipe(400, 0.52));
        } catch (NoSuchAlgorithmException e) {
            fail("Unexpected NoSuchAlgorithmException.");
        }
    }

    @Test
    void SingleCommitMergeTest() {
        try {
            repo.newBranch("high-hydration-test");
            repo.commit(new BreadRecipe(360, 0.48));
            assertTrue(repo.merge("master"));
            assertEquals(3, repo.size());
            assertEquals(2, repo.getBranches().size());
        } catch (NoSuchAlgorithmException e) {
            fail("Unexpected NoSuchAlgorithmException.");
        }
    }

    @Test
    void MultiCommitMultiBranchMergeTest() {
        try {
            repo.newBranch("high-hydration-test");
            repo.commit(new BreadRecipe(360, 0.48));
            repo.newBranch("higher-temp-test");
            BreadRecipe highTemp = new BreadRecipe(360, 0.48);
            highTemp.setCookTemp(550);
            repo.commit(highTemp);
            repo.commit(new BreadRecipe(600, 0.51));
            assertTrue(repo.merge("high-hydration-test"));
            assertEquals("high-hydration-test", repo.getCurrentBranch());
            assertTrue(repo.merge("master"));
            assertEquals(6, repo.size());
            assertEquals(3, repo.getBranches().size());
        } catch (NoSuchAlgorithmException e) {
            fail("Unexpected NoSuchAlgorithmException.");
        }
    }

    @Test
    void SingleAttemptAndCount() {
        repo.attempt(clock);
    }

}
