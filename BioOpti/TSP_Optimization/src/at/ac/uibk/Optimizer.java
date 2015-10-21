package at.ac.uibk;

public class Optimizer {


	private static int[] generateRandomArray(int size){
		int[] arr = new int[size];
		
		// arr with 0->1->2....
		for (int i = 0; i < size; i++){
			arr[i] = (i + 1) % size;
		}

		// randomize by permutations
		for (int i = 0; i < size; i++){
			// random rounded number
			int toSwitch = (int) (Math.random() * size);
			
			int tmp = arr[i];
			arr[i] = arr[toSwitch];
			arr[toSwitch] = tmp;
		}
		
		return arr;
	}
	
	// takes an array of nodes as input, returns the order to travel them
	public static int[] optimize(TSP_Node[] input){
		int[] ordering = generateRandomArray(input.length);
		
		
		return ordering;
	}
	
	public static int calculatePath(int[] order, TSP_Node[] nodes){
		int dist = 0;
		
		for (int i = 0; i < order.length; i++){
			dist += nodes[i].calculateDistance(nodes[order[i]]);
		}
		
		return dist;
	}
	
}
