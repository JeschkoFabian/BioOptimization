package at.ac.uibk.bioopt;

import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class Main {
	static int ARR_SIZE = 5;
	static int distArr[][] = new int[ARR_SIZE][ARR_SIZE];
	static int myArr[] = new int[ARR_SIZE];
	static int randArr[] = new int[ARR_SIZE];
	static Map<Integer, Integer> randMap = new HashMap<Integer, Integer>(ARR_SIZE);
	static Map<Integer, Integer> sortedMap = new HashMap<Integer, Integer>(ARR_SIZE);
	static Tour tour;

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		for (int i = 0; i < ARR_SIZE; i++) {
			for (int j = 0; j < ARR_SIZE; j++) {
				if (j > i) {
					int num = (int) (Math.random() * 100);
					distArr[j][i] = num;
					distArr[i][j] = num;
				}
			}
		}

		System.out.println("DISTANCE ARRAY: ");
		for (int i = 0; i < ARR_SIZE; i++) {
			for (int j = 0; j < ARR_SIZE; j++) {
				System.out.print(distArr[i][j] + "\t");
			}
			System.out.println();
		}

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
		
		System.out.print("\nINITIAL PATH:\t");
		for (int i = 0; i < ARR_SIZE; i++) {
			System.out.print(randArr[i] + " --> ");
		}
		System.out.println(randArr[0] + "  |  Distance: " + tour.getDistance()+"\n");

		
		twoOpt();
		
		System.out.println("\nRESULT:\t\t" + tour.toString());

	}

	private static void twoOpt() {

		int size = ARR_SIZE;
		int improve = 0;
		while (improve < 20) {
			double best_distance = tour.getDistance();

			for (int i = 1; i < size-1; i++) {
				for (int k = i + 1; k < size; k++) {
					Tour newTour = twoOptSwap(tour, i, k);

					double new_distance = newTour.getDistance();

					if (new_distance < best_distance) {
						improve = 0;
						tour = newTour;
						best_distance = new_distance;
						System.out.println("IMPROVED PATH:\t" + tour.toString());
					}
				}
			}
			improve++;
		}
	}

	private static Tour twoOptSwap(Tour oldTour, int i, int k) {
		Tour newTour = new Tour(new int[ARR_SIZE], distArr);

		int size = ARR_SIZE;

		for (int c = 0; c <= i - 1; ++c) {
			newTour.setNode(c, oldTour.getNode(c));
		}

		int dec = 0;
		for (int c = i; c <= k; ++c) {
			newTour.setNode(c, oldTour.getNode(k - dec));
			dec++;
		}

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
