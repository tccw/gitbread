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

    // this should work but the logic is for a reversed version of the history I am recording
    //TODO: change logic to work backwards.
    private LinkedHashMap<String, List<int[]>> getIndexes() {
        LinkedHashMap<String, List<int[]>> result = new LinkedHashMap<>();
        int ypos = 0;
        boolean first = true;
        Commit previous = new Commit();
        for (int i = 0; i < history.getBranches().size(); i++) {
            List<int[]> indexes = new ArrayList<>();
            String branch = history.getBranches().get(i);
            int xpos = 0;
            for (Commit c : history.getCommits()) {
                if (first) {
                    indexes.add(new int[]{xpos, ypos});
                    first = false;
                } else if (c.getBranchLabel().equals(branch)) {
                    if (c.isMerged() && (ypos != 0)) {
                        indexes.add(new int[]{xpos, ypos - 1});
                    } else if (!c.isMerged() && !previous.getBranchLabel().equals(branch)) {
                        int lastIndex = indexes.size() - 1;
                        if (lastIndex < 0) {
                            indexes.add(new int[]{xpos, ypos});
                        } else {
                            int prevXpos = indexes.get(lastIndex)[0];
                            indexes.add(new int[]{xpos - prevXpos, ypos});
                        }
                    } else {
                        indexes.add(new int[]{xpos, ypos});
                    }
                }
                xpos++;
                previous = c;
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
