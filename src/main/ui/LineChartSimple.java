package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import model.BreadRecipe;
import model.RecipeDevCollection;
import model.RecipeDevHistory;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

//https://stackoverflow.com/questions/14615590/javafx-linechart-hover-values/14623439#14623439
//https://docs.oracle.com/javafx/2/charts/line-chart.htm
public class LineChartSimple extends Application {
    RecipeDevHistory repo;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Map<String, List<int[]>> data = setUp();
        stage.setTitle("Line Chart Sample");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("commit");
        //creating the chart
        final LineChart<Number, Number> lineChart =
                new LineChart<Number, Number>(xAxis, yAxis);

        lineChart.setTitle("branch graph test");
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(-1);
        yAxis.setUpperBound(10);
        //defining a series
        XYChart.Series master = new XYChart.Series();
        master.setName("master");
        //populating the series with data
        for (int i = 0; i < data.get("master").size(); i++) {
            int x = data.get("master").get(i)[0];
            int y = data.get("master").get(i)[1];
            master.getData().add(new XYChart.Data(x, y));
        }

        XYChart.Series highHydrationTest = new XYChart.Series();
        highHydrationTest.setName("high-hydration-test");
        //populating the series with data
        for (int i = 0; i < data.get("high-hydration-test").size(); i++) {
            int x = data.get("high-hydration-test").get(i)[0];
            int y = data.get("high-hydration-test").get(i)[1];
            highHydrationTest.getData().add(new XYChart.Data(x, y));
        }

        XYChart.Series highTemp = new XYChart.Series();
        highTemp.setName("high-temp");
        //populating the series with data
//        for (int i = 0; i < data.get("high-temp").size(); i++) {
//            int x = data.get("high-temp").get(i)[0];
//            int y = data.get("high-temp").get(i)[1];
//            highTemp.getData().add(new XYChart.Data(x, y));
//        }

        Scene scene = new Scene(lineChart, 800, 400);
        lineChart.getData().addAll(master, highHydrationTest);

        stage.setScene(scene);
        stage.show();
    }

    private Map<String, List<int[]>> setUp() {
        try {
            HistoryGraph plot;
            repo = new RecipeDevHistory(new BreadRecipe(1000));
            repo.commit(new BreadRecipe(1000, 0.60));
            repo.commit(new BreadRecipe(1000, 0.59));
            repo.newBranch("high-hydration-test");
            repo.commit(new BreadRecipe(360, 0.78));
            repo.commit(new BreadRecipe(360, 0.79));
            repo.checkout("master");
            repo.commit(new BreadRecipe(1000, 0.58));
            repo.checkout("high-hydration-test");
            repo.commit(new BreadRecipe(1000, 0.81));
            repo.commit(new BreadRecipe(600, 0.51));
//            repo.newBranch("high-temp");
//            repo.commit(new BreadRecipe(1000,0.76));
//            repo.commit(new BreadRecipe(1000,0.72));
//            repo.checkout("high-hydration-test");
            repo.merge("master");
            repo.commit(new BreadRecipe(650, 0.45));

            plot = new HistoryGraph(repo);
            return plot.getData();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


}

