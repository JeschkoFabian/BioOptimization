package at.ac.uibk;

import java.io.IOException;
import java.util.Arrays;

public class Ant_Exec {
	// TODO: move this to Ant_Solver

	public static void main(String[] args) throws IOException, InterruptedException {
		Ant_Solver solver = new Ant_Solver(200);
		solver.solve();
		
		System.out.println("Path with the best Pheromone value: ");
		System.out.println(solver.getBestPathCost() + " - " + Arrays.toString(solver.getBestPath()));
	}

}
