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
		Scanner sc = new Scanner(problem);

		while (sc.hasNextInt()) {
			int pos = sc.nextInt();
			nodes[pos - 1] = new TSP_Node_Impl(sc.nextDouble(), sc.nextDouble());
		}

		sc.close();

		double avg = 0;
		double bestDist = Double.MAX_VALUE;
		int[] bestOrder = new int[0];
		// set number of iterations
		int iter = 100;
		for (int i = 0; i < iter; i++) {
			int[] tmpOrder = Optimizer.optimize(nodes);

			double tmpDist = Optimizer.calculatePath(tmpOrder, nodes);
			avg += tmpDist;

			if (tmpDist < bestDist) {
				bestDist = tmpDist;
				bestOrder = tmpOrder;
				
				System.out.println((int) bestDist);
				System.out.println(Arrays.toString(bestOrder));
			}
		}

		System.out.println("<=============Solution============>");
		System.out.println("Average Dist: " + (int) (avg / iter));
		System.out.println("Best Dist: " + (int) bestDist);
		System.out.println("Best Order: " + Arrays.toString(bestOrder));

//		System.out.println("Best known for eil51: 426");
	}
}
