package model;

import org.junit.jupiter.api.BeforeEach;

import java.security.NoSuchAlgorithmException;

public class TestCommit {

    private RecipeDevHistory repo;

    @BeforeEach
    void setUp() {
        try {
            repo = new RecipeDevHistory(new BreadRecipe(1000));
            repo.commit(new BreadRecipe(1000, 0.60));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
