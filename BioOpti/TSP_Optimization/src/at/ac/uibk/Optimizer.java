package at.ac.uibk;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Optimizer {
	private static Random r = new Random();

	private static int[] generateRandomArray(int size) {
		int[] arr = new int[size];

		// arr with 0->1->2....
		for (int i = 0; i < size; i++) {
			arr[i] = (i + 1) % size;
		}

		// randomize by permutations
		for (int i = 0; i < size; i++) {
			// random rounded number
			int toSwitch = r.nextInt(size);

			arr = swap(arr, i, toSwitch);
		}

		return arr;
	}

	// takes an array of nodes as input, returns the order to travel them
	public static int[] optimize(TSP_Node[] input, int iter) {
		int[] ordering = generateRandomArray(input.length);

		double currentDist = calculatePath(ordering, input);
		// System.out.println("Initial Path length: " + (int) currentDist);

		for (int k = 0; k < iter; k++) {
			double tmpDist = currentDist;

			for (int i = 0; i < input.length - 1; i++) {
				for (int j = i + 1; j < input.length; j++) {

					ordering = swap(ordering, i, j);
					double newDist = calculatePath(ordering, input);

					if (newDist >= currentDist) {
						// allow worse switches, the closer the new dist is to
						// the current, the better the chances
						if ((newDist - currentDist) / currentDist > r.nextDouble() / iter)
							ordering = swap(ordering, i, j);
					} else {
						currentDist = newDist;
					}
				}
			}

			// cancel earlier if nothing better can be achieved
			if (tmpDist == currentDist)
				break;
		}
		// System.out.println("Optimized path length: " + (int) currentDist);

		return ordering;
	}

	public static int[] optimize2(TSP_Node[] input, int iter) {
		int[] ordering = generateRandomArray(input.length);

		double currentDist = calculatePath(ordering, input);

		for (int i = 0; i < iter; i++) {
			int middle = r.nextInt(input.length);
			List<int[]> neighbors = new ArrayList<>();

			for (int j = 0; j < input.length / 2; j++) {
				int left = (middle - j < 0) ? middle - j + input.length : middle - j;
				int right = (middle + j) % input.length;

				neighbors.add(swap(ordering, middle, left));
				neighbors.add(swap(ordering, middle, right));

				// take random neighbor
				int rand = r.nextInt(neighbors.size());
				double newDist = calculatePath(neighbors.get(rand), input);
				
				// if better, keep dist and choose new start location
				if (newDist < currentDist){
					currentDist = newDist;
					ordering = neighbors.get(rand);
					neighbors.clear();
					
					j = 0;
					middle = r.nextInt(input.length);
				}
			}

		}

		return ordering;
	}

	private static int[] swap(int[] arr, int x, int y) {
		int tmp = arr[x];
		arr[x] = arr[y];
		arr[y] = tmp;

		return arr;
	}

	public static double calculatePath(int[] order, TSP_Node[] nodes) {
		double dist = 0;

		for (int i = 0; i < order.length; i++) {
			dist += nodes[order[i]].calculateDistance(nodes[order[(i + 1) % order.length]]);
		}
		return dist;
	}

}
