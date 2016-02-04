package at.ac.uibk;

import java.io.IOException;
import java.util.Arrays;

public class Ant_Exec {
	// TODO: move this to Ant_Solver

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("Starting ant colony optimization.");
		Ant_Solver solver = new Ant_Solver(180, 400);
		solver.solve();
		
		System.out.println("Shortest path found by the ACO algorithm: ");
		System.out.println(solver.getBestPathCost() + " - " + Arrays.toString(solver.getBestPath()));
	}

}
