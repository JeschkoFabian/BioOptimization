package at.ac.uibk;

import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class PSO_Exec_Chart extends Application {

	private static List<Particle> result;
	private static final String PROBLEM = "ZDT4";

	private static final int GRAPHS_VERTICAL = 2;
	private static final int GRAPHS_HORIZONTAL = 2;

	// more swarm -> better result
	private static final int SWARM_SZ = 100;
	// no apparent benefit with bigger archive size
	private static final int ARCHIVE_SZ = 100;
	// gets stuck after 800-1500 gens, so more is useless
	private static final int GENERATIONS = 1000;

	
	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) {

		GridPane grid = new GridPane();

		for (int i = 0; i < GRAPHS_HORIZONTAL; i++) {
			for (int j = 0; j < GRAPHS_VERTICAL; j++) {
				PSO_Solver solver = new PSO_Solver();

				if (PROBLEM.equals("ZDT1")) {
					result = solver.solve(SWARM_SZ, ARCHIVE_SZ, GENERATIONS, new ZDT1());
					stage.setTitle("Pareto Front for ZDT1");
				} else {
					result = solver.solve(SWARM_SZ, ARCHIVE_SZ, GENERATIONS, new ZDT4());
					stage.setTitle("Pareto Front for ZDT4");
				}

				// defining the axes
				final NumberAxis xAxis = new NumberAxis();
				final NumberAxis yAxis = new NumberAxis();

				// weird, scaling the axis changes the whole graph scaling
//				xAxis.setAutoRanging(false);
//				yAxis.setAutoRanging(false);

				xAxis.setLowerBound(0);
				xAxis.setUpperBound(1.05);

				yAxis.setLowerBound(result.get(result.size() - 1).getEval()[1] - 0.05);
				yAxis.setUpperBound(result.get(0).getEval()[1] + 0.05);
				
				// xAxis.setLabel("X");
				// creating the chart
				final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

				lineChart.setTitle("Pareto Front");
				// defining a series
				XYChart.Series<Number, Number> series = new XYChart.Series<>();
				// series.setName("Particles");
				// populating the series with data

				for (Particle p : result) {
					series.getData().add(new XYChart.Data<Number, Number>(p.getEval()[0], p.getEval()[1]));
				}
				lineChart.getData().add(series);
				lineChart.setLegendVisible(false);

				grid.add(lineChart, i, j);
			}
		}

		Scene scene = new Scene(grid, 800, 600);

		stage.setScene(scene);
		stage.show();
	}

}
