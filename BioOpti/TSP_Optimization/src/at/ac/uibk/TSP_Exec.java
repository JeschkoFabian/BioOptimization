package at.ac.uibk;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Scanner;

public class TSP_Exec {

	public static void main(String[] args) throws IOException {
		int sz = 225;
		TSP_Node[] nodes = new TSP_Node_Impl[sz];

		String problem = new String(Files.readAllBytes(new File("ts255.tsp").toPath()));

//		String problem = "1 37 52 " + "2 49 49 " + "3 52 64 " + "4 20 26 " + "5 40 30 " + "6 21 47 " + "7 17 63 "
//				+ "8 31 62 " + "9 52 33 " + "10 51 21 " + "11 42 41 " + "12 31 32 " + "13 5 25 " + "14 12 42 "
//				+ "15 36 16 " + "16 52 41 " + "17 27 23 " + "18 17 33 " + "19 13 13 " + "20 57 58 " + "21 62 42 "
//				+ "22 42 57 " + "23 16 57 " + "24 8 52 " + "25 7 38 " + "26 27 68 " + "27 30 48 " + "28 43 67 "
//				+ "29 58 48 " + "30 58 27 " + "31 37 69 " + "32 38 46 " + "33 46 10 " + "34 61 33 " + "35 62 63 "
//				+ "36 63 69 " + "37 32 22 " + "38 45 35 " + "39 59 15 " + "40 5 6 " + "41 10 17 " + "42 21 10 "
//				+ "43 5 64 " + "44 30 15 " + "45 39 10 " + "46 32 39 " + "47 25 32 " + "48 25 55 " + "49 48 28 "
//				+ "50 56 37 " + "51 30 40";
		Scanner sc = new Scanner(problem);

		while (sc.hasNextInt()) {
			int pos = sc.nextInt();
			nodes[pos - 1] = new TSP_Node_Impl(sc.nextDouble(), sc.nextDouble());
		}

		sc.close();

		double avg = 0;
		double bestDist = Double.MAX_VALUE;
		int[] bestOrder = new int[0];
		int iter = 20;
		for (int i = 0; i < iter; i++) {
			int[] tmpOrder = Optimizer.optimize2(nodes, 100);

			double tmpDist = Optimizer.calculatePath(tmpOrder, nodes);
			avg += tmpDist;

			if (tmpDist < bestDist) {
				bestDist = tmpDist;
				bestOrder = tmpOrder;
			}
		}

		System.out.println("Average Dist: " + (int) (avg / iter));
		System.out.println("Best Dist: " + (int) bestDist);
		System.out.println("Best Order: " + Arrays.toString(bestOrder));

//		System.out.println("Best known for eil51: 426");
	}
}
