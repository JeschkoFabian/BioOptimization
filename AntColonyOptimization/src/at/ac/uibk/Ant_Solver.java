package at.ac.uibk;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Ant_Solver {
	private List<TSP_Node> nodes;
	private double pheromone[][];

	private final int ANT_NUM;
	private final int ITER_NUM;

	private double bestPathCost = Double.MAX_VALUE;
	private int[] bestPath = null;
	
	public Ant_Solver(int ants, int iter) {
		ANT_NUM = ants;
		ITER_NUM = iter;
	}

	public void solve() {
		try {
			int sz = 225;
			nodes = new ArrayList<TSP_Node>(sz);
			pheromone = new double[sz][sz];
			// init all pheros
			for (int i = 0; i < sz; i++){
				Arrays.fill(pheromone[i], 1);
			}

			String problem = new String(Files.readAllBytes(new File("ts255.tsp").toPath()));
			Scanner sc = new Scanner(problem);
			sc.useLocale(Locale.US);
			while (sc.hasNextInt()) {
				int pos = sc.nextInt();
				nodes.add(pos - 1, new TSP_Node_Impl(sc.nextDouble(), sc.nextDouble()));
			}

			sc.close();

			for (int i = 0; i < ITER_NUM; i++) {
				List<Ant> ants = createAnts(ANT_NUM);

				for (Ant a : ants) {
					a.start();
				}

				for (Ant a : ants) {
					a.join();

					double cost = a.calculatePath();
					if (cost < bestPathCost){
						bestPathCost = cost;
						bestPath = a.getPath();
					}
					
					a.updatePheromones();
				}

				evaporatePheromone();
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// TODO: implement
	public void evaporatePheromone() {
		for (int i = 0; i < pheromone.length; i++){
			for (int j = 0; j < pheromone[i].length; j++){
				pheromone[i][j] /= 2;
			}
		}
	}

	public List<Ant> createAnts(int sz) {
		List<Ant> ants = new ArrayList<Ant>(sz);

		for (int i = 0; i < sz; i++) {
			ants.add(new Ant(nodes, pheromone));
		}

		return ants;
	}

	public double getBestPathCost() {
		return bestPathCost;
	}

	public int[] getBestPath() {
		return bestPath;
	}
}
