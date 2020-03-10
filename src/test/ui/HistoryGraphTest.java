package ui;

import model.BreadRecipe;
import model.RecipeDevHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

public class HistoryGraphTest {
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
            repo.commit(new BreadRecipe(1000, 0.59));
            repo.newBranch("high-hydration-test");
            repo.commit(new BreadRecipe(360, 0.78));
            repo.commit(new BreadRecipe(360, 0.79));
            repo.checkout("master");
            repo.commit(new BreadRecipe(1000,0.58));
            repo.checkout("high-hydration-test");
            repo.commit(new BreadRecipe(1000, 0.81));
            repo.commit(new BreadRecipe(600, 0.51));
            repo.merge("master");
            repo.commit(new BreadRecipe(650, 0.45));
            System.out.println("wait");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Test
    void TestMakeData() {
        plot = new HistoryGraph(repo);
        Map<String, List<int[]>> data = plot.getData();
        System.out.println("Hold up there!");
    }
}
