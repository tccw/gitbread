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

    @BeforeEach
    void setUp() {
        try {
            repo = new RecipeDevHistory(new BreadRecipe(1000));
            repo.newBranch("high-hydration-test");
            repo.commit(new BreadRecipe(360, 0.48));
            repo.newBranch("higher-temp-test");
            BreadRecipe highTemp = new BreadRecipe(360, 0.47);
            highTemp.setCookTemp(550);
            repo.commit(highTemp);
            repo.commit(new BreadRecipe(600, 0.51));
            repo.merge("high-hydration-test");
            repo.merge("master");

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
