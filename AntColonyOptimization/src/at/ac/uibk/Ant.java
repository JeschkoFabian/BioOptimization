package at.ac.uibk;

import java.security.SecureRandom;
import java.util.List;

public class Ant extends Thread{
	private SecureRandom rand = new SecureRandom();
	// given, nodes stays constant, pheromone will be updated
	private final List<TSP_Node> nodes;
	private double[][] pheromone;
	
	private int[] path;
	private double cost;
	private int moves;
	
	private List<TSP_Node> remaining;

	public Ant(final List<TSP_Node> nodes, double[][] pheromone) {
		this.nodes = nodes;
		this.pheromone = pheromone;
		
		for (TSP_Node n : nodes){
			remaining.add(n);
		}
		
		path = new int[nodes.size()];
		cost = 0;
		moves = 0;
	}

	@Override
	public void run() {
		constructSolution();
	}

	public void constructSolution() {
		// random starting point
		path[0] = rand.nextInt(path.length);
		
		remaining.remove(nodes.get(path[0]));
		
		for (int i = 1; i < nodes.size(); i++){
			double bestCost = Double.MAX_VALUE;
			
			for(TSP_Node node : remaining){
				double tmpCost = calcCost(node);
				
				if (tmpCost < bestCost){
					bestCost = tmpCost;
					
					path[i] = nodes.indexOf(node);
				}
			}
			
			remaining.remove(nodes.get(path[i]));
		}
	}
	
	public void updatePheromones(){
		
	}

	private double calcCost(TSP_Node node) {
		double dist = nodes.get(path[moves]).calculateDistance(node);

		return cost + dist;
	}
}