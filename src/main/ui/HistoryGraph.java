package ui;

/*
Create graphs with master on the bottom
 */

import model.Commit;
import model.RecipeDevHistory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HistoryGraph {
    RecipeDevHistory history;
    Map<String, List<int[]>> data;

    public HistoryGraph(RecipeDevHistory h) {
        history = h;
        data = getIndexes();
    }

    private LinkedHashMap<String, List<int[]>> getIndexes() {
        LinkedHashMap<String, List<int[]>> result = new LinkedHashMap<>();
        int ypos = 0;
        for (int i = 0; i < history.getBranches().size(); i++) {
            List<int[]> indexes = new ArrayList<>();
            int xpos = 0;
            for (Commit c : history.getCommits()) {
                 if (c.getBranchLabel().equals(history.getBranches().get(i))) {
                    indexes.add(new int[]{xpos, ypos});
                }
                xpos++;
            }
            result.put(history.getBranches().get(i), indexes);
            ypos++;
        }
        return result;
    }

    public Map<String, List<int[]>> getData() {
        return data;
    }
}
