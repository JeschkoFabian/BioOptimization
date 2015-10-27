package at.ac.uibk.bioopt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;

public class Main {
	static int ARR_SIZE = 225;
	static double distArr[][] = new double[ARR_SIZE][ARR_SIZE];
	static int myArr[] = new int[ARR_SIZE];
	static int randArr[] = new int[ARR_SIZE];
	static Map<Integer, Integer> randMap = new HashMap<Integer, Integer>(ARR_SIZE);
	static Map<Integer, Integer> sortedMap = new HashMap<Integer, Integer>(ARR_SIZE);
	static Tour tour;;

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {
		List<Node> nodes = new ArrayList<Node>();
		BufferedReader br = new BufferedReader(new FileReader("tsp225.tsp"));
		try {
			String line = br.readLine();

			while (!line.toString().contains("EOF")) {
				if (Character.isDigit(line.charAt(0)) || Character.isSpaceChar(line.charAt(0))) {

					StringTokenizer Tok = new StringTokenizer(line);
					List<Double> list = new ArrayList<Double>();
					while (Tok.hasMoreElements()) {
						String tok = Tok.nextElement().toString();
						list.add(Double.parseDouble(tok));
					}
					nodes.add(new Node(list.get(0), list.get(1), list.get(2)));

				}
				line = br.readLine();
			}
		} finally {
			br.close();
		}

		// All distances are added to a distance array
		for (int i = 0; i < ARR_SIZE; i++) {
			for (int j = 0; j < ARR_SIZE; j++) {
				if (j > i) {
					distArr[j][i] = getDistance(nodes.get(j), nodes.get(i));
					distArr[i][j] = distArr[j][i];
				}
			}
		}

		// Calculation of a random initial route
		for (int i = 0; i < ARR_SIZE; i++) {
			myArr[i] = i;
		}

		for (int i = 0; i < ARR_SIZE; i++) {
			randMap.put(i, (int) (Math.random() * 100));
		}
		sortedMap = sortByValue(randMap);

		int j = 0;
		for (int i : sortedMap.keySet()) {
			randArr[j++] = i;
		}

		tour = new Tour(randArr, distArr);

		System.out.print("\nINITIAL PATH:\t" + tour.toString());

		// start of the actual algorithm
		twoOpt();

		System.out.println("\nRESULT:\t\t" + tour.toString());

	}

	private static double getDistance(Node node, Node node2) {

		return Math.sqrt(Math.pow(node.getX() - node2.getX(), 2) + Math.pow(node.getY() - node2.getY(), 2));
	}

	private static void twoOpt() {

		int size = ARR_SIZE;
		int init_threshold_val = 8;
		int max_threshold_val = 32;
		int improve = 0;
		while (improve < 2) {
			for (int i = 1; i < size - 1; i++) {
				int threshold = init_threshold_val;
				int k = i + 1;
				while (k < size) {
					List<Tour> shorterTours = new ArrayList<Tour>();
					// Shorter tours than our current shortest tour are searched
					// by exchanging the path between node i and node k+l, where
					// l goes from 0 to our current threshold
					for (int l = 0; l < threshold && k + l < size; l++) {
						Tour temp_tour = twoOptSwap(tour, i, k + l);
						if (temp_tour.getDistance() < tour.getDistance()) {
							shorterTours.add(temp_tour);
						}
					}
					// If there is no shorter tour found...
					if (shorterTours.isEmpty()) {
						// ... the threshold is increased and more nodes are
						// taken into consideration, or...
						if (threshold < max_threshold_val) {
							threshold *= 2;
							continue;
						} else {
							// ... the threshold is decreased to its initial
							// value, when it was already at its maximum value
							threshold = init_threshold_val;
							k++;
							continue;
						}
						// If there is at least one shorter tour found...
					} else {
						Tour temp_tour = tour;
						for (Tour x : shorterTours) {
							if (x.getDistance() < temp_tour.getDistance()) {
								temp_tour = x;
							}
						}
						// ... the shortest new found tour becomes our new tour
						tour = temp_tour;
						k++;
						improve = 0;
						threshold = init_threshold_val;
						// System.out.println("IMPROVED PATH:\t" +
						// tour.toString());

					}
				}
			}
			improve++;
		}
	}

	private static Tour twoOptSwap(Tour oldTour, int i, int k) {
		Tour newTour = new Tour(new int[ARR_SIZE], distArr);

		int size = ARR_SIZE;
		// route from [0] to [i-1] is added in order to the new path
		for (int c = 0; c <= i - 1; ++c) {
			newTour.setNode(c, oldTour.getNode(c));
		}

		// route from [i] to [k] is added in reverse order to the new path
		int dec = 0;
		for (int c = i; c <= k; ++c) {
			newTour.setNode(c, oldTour.getNode(k - dec));
			dec++;
		}

		// route from [k+1] to end is added in order to the new path
		for (int c = k + 1; c < size; ++c) {
			newTour.setNode(c, oldTour.getNode(c));
		}

		return newTour;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map sortByValue(Map unsortMap) {
		List list = new LinkedList(unsortMap.entrySet());

		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
			}
		});

		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
}
