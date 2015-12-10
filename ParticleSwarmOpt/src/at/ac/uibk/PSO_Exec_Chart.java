package at.ac.uibk;

import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class PSO_Exec_Chart extends Application{
	
	
	private static List<Particle> result;

	public static void main(String[] args) {

		PSO_Solver solver = new PSO_Solver();
		result = solver.solve(100, 100, 600);
		launch();
	}

	@Override
	public void start(Stage stage) {
		stage.setTitle("Pareto Front for ZDT1");
		// defining the axes
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("X");
		// creating the chart
		final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);

		lineChart.setTitle("Pareto Front");
		// defining a series
		XYChart.Series series = new XYChart.Series();
		series.setName("Particles");
		// populating the series with data
		
		for (Particle p : result){
			series.getData().add(new XYChart.Data<>(p.getEval()[0], p.getEval()[1]));
		}
		
		Scene scene = new Scene(lineChart, 800, 600);
		lineChart.getData().add(series);

		stage.setScene(scene);
		stage.show();
	}

}
