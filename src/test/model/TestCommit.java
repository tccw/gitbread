package model;

import org.junit.jupiter.api.BeforeEach;
import ui.HistoryGraph;

import java.security.NoSuchAlgorithmException;

public class TestCommit {

    private RecipeDevHistory repo;
    private HistoryGraph plot;

    /*
            4--5--7
           /       \
    1--2--3--6------8--9


     */
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
