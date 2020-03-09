package ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

//https://stackoverflow.com/questions/14615590/javafx-linechart-hover-values/14623439#14623439
//https://docs.oracle.com/javafx/2/charts/line-chart.htm
public class LineChartSimple extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Line Chart Sample");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");
        //creating the chart
        final LineChart<Number, Number> lineChart =
                new LineChart<Number, Number>(xAxis, yAxis);

        lineChart.setTitle("Stock Monitoring, 2010");
        //defining a series
        XYChart.Series master = new XYChart.Series();
        master.setName("master");
        //populating the series with data
        master.getData().add(new XYChart.Data(1, 1));
        master.getData().add(new XYChart.Data(2, 1));
        master.getData().add(new XYChart.Data(3, 1));
        master.getData().add(new XYChart.Data(4, 1));
        master.getData().add(new XYChart.Data(10, 1));
        master.getData().add(new XYChart.Data(11, 1));

        XYChart.Series highHydrationTest = new XYChart.Series();
        highHydrationTest.setName("My portfolio");
        //populating the series with data
        highHydrationTest.getData().add(new XYChart.Data(4, 1));
        highHydrationTest.getData().add(new XYChart.Data(5, 1.1));
        highHydrationTest.getData().add(new XYChart.Data(6, 1.1));
        highHydrationTest.getData().add(new XYChart.Data(7, 1.1));
        highHydrationTest.getData().add(new XYChart.Data(8, 1.1));
        highHydrationTest.getData().add(new XYChart.Data(9, 1.1));
        highHydrationTest.getData().add(new XYChart.Data(10, 1));


        XYChart.Series highTemp = new XYChart.Series();
        highTemp.setName("My portfolio");
        //populating the series with data
        highTemp.getData().add(new XYChart.Data(6, 1.1));
        highTemp.getData().add(new XYChart.Data(7, 1.2));
        highTemp.getData().add(new XYChart.Data(8, 1.2));


        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.getData().addAll(master, highHydrationTest, highTemp);

        stage.setScene(scene);
        stage.show();
    }


}

