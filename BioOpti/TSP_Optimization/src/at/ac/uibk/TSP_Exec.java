package at.ac.uibk;

import java.util.Arrays;

public class TSP_Exec {

	public static void main(String[] args) {
		int sz = 25;
		TSP_Node[] nodes = new TSP_Node_Impl[sz];

		for (int i = 0; i < sz; i++) {
//			int x = (int) (Math.random() * 1000);
//			int y = (int) (Math.random() * 1000);
			nodes[i] = new TSP_Node_Impl(i, 0);
		}
		
		int[] path = Optimizer.optimize(nodes);
		int distance = Optimizer.calculatePath(path, nodes);
		
		System.out.println("Path distance: " +  distance);
		System.out.println("Order: " + Arrays.toString(path));
	}

}
