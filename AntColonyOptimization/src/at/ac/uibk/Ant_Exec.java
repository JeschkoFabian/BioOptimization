package at.ac.uibk;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Ant_Exec {
	private static List<TSP_Node> nodes;
	private static double pheromone[][];
	
	private final static int ANT_NUM = 10;

	public static void main(String[] args) throws IOException, InterruptedException {
		int sz = 225;
		nodes = new ArrayList<TSP_Node>();
		pheromone = new double[sz][sz];
		

		ExecutorService es = Executors.newFixedThreadPool(ANT_NUM);
		
		String problem = new String(Files.readAllBytes(new File("ts255.tsp").toPath()));
		Scanner sc = new Scanner(problem);

		while (sc.hasNextInt()) {
			int pos = sc.nextInt();
			nodes.set(pos - 1, new TSP_Node_Impl(sc.nextDouble(), sc.nextDouble()));
		}

		sc.close();

		
		for (int i = 0; i < 10; i++){
			List<Ant> ants = createAnts(ANT_NUM);

			for (Ant a: ants){
				es.execute(a);
			}
			
			for (Ant a: ants){
				a.join();
				
				a.updatePheromones();
			}
			
			evaporatePheromone();
		}
	}
	
	public static void evaporatePheromone(){
		
	}
	
	public static List<Ant> createAnts(int sz){
		List<Ant> ants = new ArrayList<Ant>(sz);
		
		for (int i = 0; i < sz; i ++){
			ants.add(new Ant(nodes, pheromone));
		}
		
		return ants;
	}

}
