package at.ac.uibk;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class Ant extends Thread {
	private SecureRandom rand = new SecureRandom();
	// given, nodes stays constant, pheromone will be updated
	private final List<TSP_Node> nodes;
	private double[][] pheromone;
	private int[] path;
	private List<TSP_Node> remaining;

	public Ant(final List<TSP_Node> nodes, double[][] pheromone) {
		this.nodes = nodes;
		this.pheromone = pheromone;

		remaining = new ArrayList<TSP_Node>(nodes);

		path = new int[nodes.size()];
	}

	@Override
	public void run() {
		constructSolution();
	}

	public void constructSolution() {
		// random starting point
		path[0] = rand.nextInt(path.length);

		remaining.remove(nodes.get(path[0]));

		for (int i = 1; i < nodes.size(); i++) {
			// input node from last step
			TSP_Node node = selectNextNode(nodes.get(path[i - 1]));

			path[i] = nodes.indexOf(node);

			remaining.remove(node);
		}
	}

	public void updatePheromones() {
		double contribution = 500/calculatePath();
		//System.out.println(contribution);
		for (int i = 0; i < path.length - 1; i++) {
			pheromone[path[i]][path[i + 1]] += contribution;
			pheromone[path[i + 1]][path[i]] += contribution;
		}
		pheromone[path[path.length - 1]][path[0]] += contribution;
		pheromone[path[0]][path[path.length - 1]] += contribution;
	}

	private TSP_Node selectNextNode(TSP_Node node) {
//		if (rand.nextDouble() < 0.01){
//			return remaining.get(rand.nextInt(remaining.size()));
//		}
		
		double[] cost = new double[remaining.size()];
		double totalCost = 0;

		for (int i = 0; i < remaining.size(); i++) {
			TSP_Node toVisit = remaining.get(i);

			double tmp = /*this.pow(*/pheromone[nodes.indexOf(node)][nodes.indexOf(toVisit)]/*, 1)*/
					* this.pow(1 / node.calculateDistance(toVisit), 5);

			totalCost += tmp;
			cost[i] = tmp;
		}

		double prob = rand.nextDouble();

		for (int i = 0; i < remaining.size(); i++) {
			prob -= cost[i] / totalCost;

			if (prob <= 0) {
				return remaining.get(i);
			}
		}

		return remaining.get(remaining.size() - 1);
	}

	public double calculatePath() {
		double cost = 0;

		for (int i = 0; i < nodes.size() - 1; i++) {
			cost += nodes.get(path[i]).calculateDistance(nodes.get(path[i + 1]));
		}
		cost += nodes.get(path[nodes.size() - 1]).calculateDistance(nodes.get(path[0]));

		return cost;
	}
	
	public int[] getPath(){
		return path;
	}
	
	public double pow(final double a, final double b) {
		final int x = (int) (Double.doubleToLongBits(a) >> 32);
		final int y = (int) (b * (x - 1072632447) + 1072632447);
		return Double.longBitsToDouble(((long) y) << 32);
	}
}