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
			int toSwitch = (int) (Math.random() * size + 0.5);
			
			int tmp = arr[i];
			arr[i] = arr[toSwitch];
			arr[toSwitch] = tmp;
		}
		
		return arr;
	}
	
	// takes an array of nodes as input, returns the order to travel them
	public static int[] optimize(TSP_Node[] input){
		int[] ordering = generateRandomArray(input.length);
		
		// meh needs something better and some thinking^^
		for (int i = 0; i < input.length; i++){
			TSP_Node current = input[i];
			int dist = Integer.MAX_VALUE;
			TSP_Node tmp;
			
			for (int j = 0; j < input.length; j++){
				if (i == j)
					continue;
				
				if (current.calculateDistance(input[j]) < dist){
					
				}
			}
		}
		
		return ordering;
	}
	
	
}
