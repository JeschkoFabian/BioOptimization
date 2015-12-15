package at.ac.uibk;

import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class PSO_Exec_Chart extends Application {

	private static List<Particle> result;
	private final String PROBLEM = "ZDT1";

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) {
		PSO_Solver solver = new PSO_Solver();

		int swarmSize = 100;
		int archiveSize = 100;
		int generations = 1000;
		if (PROBLEM.equals("ZDT1")) {
			result = solver.solve(swarmSize, archiveSize, generations, new ZDT1());
			stage.setTitle("Pareto Front for ZDT1");
		} else {
			result = solver.solve(swarmSize, archiveSize, generations, new ZDT4());
			stage.setTitle("Pareto Front for ZDT4");
		}

		// defining the axes
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("X");
		// creating the chart
		final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

		lineChart.setTitle("Pareto Front");
		// defining a series
		XYChart.Series<Number, Number> series = new XYChart.Series<>();
		series.setName("Particles");
		// populating the series with data

		for (Particle p : result) {
			series.getData().add(new XYChart.Data<Number, Number>(p.getEval()[0], p.getEval()[1]));
		}

		Scene scene = new Scene(lineChart, 800, 600);
		lineChart.getData().add(series);

		stage.setScene(scene);
		stage.show();
	}

}
