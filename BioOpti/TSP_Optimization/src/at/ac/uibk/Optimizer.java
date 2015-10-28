package at.ac.uibk;

import java.util.Random;
import java.util.TreeMap;

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

	// takes an array of nodes as input, returns the order to travel them,
	// linear stuff
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

	private static int factor = 10;

	// neighborhood search, somewhat ok results now 4088 best
	public static int[] optimize2(TSP_Node[] input, int iter) {
		int[] currentOrder = generateRandomArray(input.length);
		double currentDist = calculatePath(currentOrder, input);

		int i = 0;
		boolean swapMode = true;

		while (i < factor && swapMode) {
			double oldDist = currentDist;

			// int middle = r.nextInt(input.length);
			TreeMap<Double, int[]> neighbors = new TreeMap<Double, int[]>();

			for (int j = 0; j < input.length; j++) {
				// maybe switch getNeighbors to use normal swap at a certain
				// threshold
				neighbors = getNeighbors(j, currentOrder, input, currentDist, swapMode);

				if (neighbors.size() > 0 && neighbors.firstKey() < currentDist) {
					// double rand = r.nextDouble();
					// if (rand < 0.05 && neighbors.size() > 2) {
					// double tmp =
					// neighbors.higherKey(neighbors.higherKey(neighbors.firstKey()));
					// currentDist = tmp;
					// ordering = neighbors.get(currentDist);
					// } else if (rand < 0.15 && neighbors.size() > 1) {
					// double tmp = neighbors.higherKey(neighbors.firstKey());
					// currentDist = tmp;
					// ordering = neighbors.get(currentDist);
					// } else {
					currentDist = neighbors.firstKey();
					currentOrder = neighbors.firstEntry().getValue();
					// }
				}
			}

			// cancel earlier if nothing better can be achieved (3 strikes)
			if (oldDist == currentDist) {
				i++;
			}

			// reset strikes, change swap function
			if (i == factor && swapMode) {
				i = 0;
				swapMode = false;
			}
		}

		return currentOrder;
	}

	// one way to implement a neighbor function
	private static TreeMap<Double, int[]> getNeighbors(int index, int[] order, TSP_Node[] nodes,
			double currentDist, boolean swapMode) {
		TreeMap<Double, int[]> neighbors = new TreeMap<>();

		int rand = r.nextInt(order.length);

		int i = 1;
		// to get better solutions but slow the process, remove neighbors.size
		// check
		while (neighbors.size() < 3 && i < order.length / factor) {
			// big speedup by choosing a good swap, random is used to offset the
			// starting point, that way it wont use the first x entries
			// repeatedly, even though they are probably maximized

//			int left = (index - i + order.length) % order.length;
			int left = index;
			int right = (index + rand + i) % order.length;
			int[] newOrder;

			if (swapMode) {
				newOrder = swapBetween(order, left, right);
			} else {
				newOrder = swap(order, left, right);
			}

			double newDist = calculatePath(newOrder, nodes);

			if (newDist < currentDist) {
				neighbors.put(newDist, newOrder);
			}

			i++;
		}

		return neighbors;
	}

	// should use a calc diff function
	private static int[] swap(int[] arr, int x, int y) {
		int[] ret = new int[arr.length];

		for (int i = 0; i < arr.length; i++) {
			if (i == x) {
				ret[i] = arr[y];
			} else if (i == y) {
				ret[i] = arr[x];
			} else {
				ret[i] = arr[i];
			}
		}

		return ret;
	}

	private static int[] swapBetween(int[] arr, int x, int y) {
		int[] ret = new int[arr.length];

		if (x > y) {
			int tmp = x;
			x = y;
			y = tmp;
		}

		for (int i = 0; i < x; i++) {
			ret[i] = arr[i];
		}

		int dec = 0;
		for (int i = x; i <= y; i++) {
			ret[i] = arr[y - dec];
			dec++;
		}

		for (int i = y + 1; i < arr.length; i++) {
			ret[i] = arr[i];
		}

		return ret;
	}

	public static double calculatePath(int[] order, TSP_Node[] nodes) {
		double dist = 0;

		for (int i = 0; i < order.length; i++) {
			dist += nodes[order[i]].calculateDistance(nodes[order[(i + 1) % order.length]]);
		}
		return dist;
	}

}
