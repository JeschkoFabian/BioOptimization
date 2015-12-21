package at.ac.uibk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
 * Archive (10): [(0.00, 1.00), (0.06, 0.76), (0.15, 0.61), (0.26, 0.49), (0.39,
 * 0.37), (0.53, 0.27), (0.68, 0.18), (0.84, 0.09), (1.00, 0.00)]
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
	// in case of annealing, bigger gen number is actually great, but still has
	// diminishing returns after a few thousand
	private static final int GENERATIONS = 5000;

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) {

		ZDT zdt;
		int total = GRAPHS_HORIZONTAL * GRAPHS_VERTICAL;
		GridPane grid = new GridPane();
		ExecutorService es = Executors.newFixedThreadPool(total);
		
		// use concurrent list because of thread safety
		List<LineChart<Number, Number>> charts = Collections.synchronizedList(new ArrayList<>());

		if (PROBLEM.equals("ZDT1")) {
			stage.setTitle("Pareto Front for ZDT1");
			zdt = new ZDT1();
		} else {
			stage.setTitle("Pareto Front for ZDT4");
			zdt = new ZDT4();
		}

		System.out.println("Starting computation of the " + PROBLEM + " problem with " + total + " threads.\n");

		for (int i = 0; i < GRAPHS_HORIZONTAL; i++) {
			for (int j = 0; j < GRAPHS_VERTICAL; j++) {

				Runnable r = new Runnable() {
					@Override
					public void run() {
						PSO_Solver solver = new PSO_Solver();
						result = solver.solve(SWARM_SZ, ARCHIVE_SZ, GENERATIONS, zdt);

						// defining the axes
						final NumberAxis xAxis = new NumberAxis();
						final NumberAxis yAxis = new NumberAxis();

						// weird, scaling the axis changes the whole graph
						// scaling
						// xAxis.setAutoRanging(false);
						// yAxis.setAutoRanging(false);

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

						// can't be added directly because javaFX does not support thread safety
						charts.add(lineChart);
					}
				};

				es.execute(r);
			}
		}

		es.shutdown();
		try {
			es.awaitTermination(5, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
		}

		// add all charts
		for (int i = 0; i < GRAPHS_HORIZONTAL; i++) {
			for (int j = 0; j < GRAPHS_VERTICAL; j++) {
				grid.add(charts.get(i + j * GRAPHS_HORIZONTAL), i, j);
			}
		}

		Scene scene = new Scene(grid, 800, 600);

		stage.setScene(scene);
		stage.show();
	}

}
