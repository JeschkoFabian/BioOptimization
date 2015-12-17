package at.ac.uibk;

import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


/**
 * Best solution for ZDT4 yet:
 * 
 * Archive (100): [(0.00, 1.00), (0.00, 0.96), (0.00, 0.94), (0.01, 0.92), (0.01, 0.90), (0.02, 0.87), (0.02, 0.86), (0.03, 0.84), (0.03, 0.82), (0.04, 0.80), (0.05, 0.79), (0.05, 0.77), (0.06, 0.76), (0.07, 0.74), (0.07, 0.73), (0.08, 0.72), (0.09, 0.70), (0.10, 0.69), (0.10, 0.68), (0.11, 0.67), (0.12, 0.65), (0.13, 0.64), (0.14, 0.63), (0.14, 0.62), (0.15, 0.61), (0.16, 0.60), (0.17, 0.59), (0.18, 0.58), (0.19, 0.57), (0.20, 0.55), (0.21, 0.54), (0.22, 0.53), (0.23, 0.52), (0.24, 0.51), (0.25, 0.50), (0.26, 0.49), (0.27, 0.48), (0.28, 0.47), (0.29, 0.46), (0.30, 0.45), (0.31, 0.44), (0.32, 0.43), (0.33, 0.42), (0.34, 0.42), (0.35, 0.41), (0.36, 0.40), (0.37, 0.39), (0.38, 0.38), (0.39, 0.37), (0.40, 0.37), (0.42, 0.36), (0.43, 0.35), (0.44, 0.34), (0.45, 0.33), (0.46, 0.32), (0.47, 0.31), (0.48, 0.31), (0.49, 0.30), (0.51, 0.29), (0.52, 0.28), (0.53, 0.27), (0.54, 0.27), (0.55, 0.26), (0.56, 0.25), (0.58, 0.24), (0.59, 0.23), (0.60, 0.23), (0.61, 0.22), (0.62, 0.21), (0.63, 0.20), (0.65, 0.20), (0.66, 0.19), (0.67, 0.18), (0.68, 0.18), (0.69, 0.17), (0.70, 0.16), (0.71, 0.16), (0.73, 0.15), (0.74, 0.14), (0.75, 0.13), (0.76, 0.13), (0.78, 0.12), (0.79, 0.11), (0.81, 0.10), (0.82, 0.10), (0.83, 0.09), (0.85, 0.08), (0.86, 0.07), (0.87, 0.07), (0.89, 0.06), (0.90, 0.05), (0.91, 0.05), (0.92, 0.04), (0.94, 0.03), (0.95, 0.03), (0.96, 0.02), (0.97, 0.01), (0.99, 0.01), (1.00, 0.00)]
 * 
 * 
 * @author fabian
 *
 */
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
	// in case of annealing, bigger gen number is actually great, but still has diminishing returns after a few thousand
	private static final int GENERATIONS = 3000;

	
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
