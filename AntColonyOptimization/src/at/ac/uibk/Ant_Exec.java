package at.ac.uibk;

import java.io.IOException;

public class Ant_Exec {
	// TODO: move this to Ant_Solver

	public static void main(String[] args) throws IOException, InterruptedException {
		Ant_Solver solver = new Ant_Solver(10);
		
		System.out.println("Path with the best Pheromone value: ");
		
		System.out.println(solver.solve());
	}

}
