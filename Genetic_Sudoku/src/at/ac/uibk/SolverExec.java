package at.ac.uibk;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SolverExec {

	public static void main(String[] args) {
		File folder = new File("sudokus");
		File[] listOfSudokus = folder.listFiles();
		final int TRIES = 1;
		int[][] results = new int[TRIES][listOfSudokus.length];

		// run the whole thing a few times.
		for (int x = 0; x < TRIES; x++) {

			// System.out.println("=========================");

			for (int k = 0; k < listOfSudokus.length; k++) {

				try {
					Scanner sc = new Scanner(listOfSudokus[k]);
					int[][] tmp = new int[9][9];

					for (int i = 0; i < 9; i++) {
						if (sc.hasNextLine()) {
							String line = sc.nextLine();
							for (int j = 0; j < 9; j++) {
								tmp[i][j] = line.charAt(j) - 48;
							}
						}
					}

					sc.close();

					SudokuSolver solver = new SudokuSolver(tmp);

					Sudoku solution = solver.solve(50);
					results[x][k] = solver.getIterations();
					 System.out.println(listOfSudokus[k].getName());
					 System.out.println(solution);
					 System.out.println("Iterations: " +
					 solver.getIterations());
					

					// int best = Integer.MAX_VALUE;
					// for (int i = 0; i < 50; i++){
					// Sudoku solution = solver.solve();
					//
					// if (solution.getContradictions() < best){
					// best = solution.getContradictions();
					//
					// System.out.print(solution);
					//
					// }
					//
					// if (solution.getContradictions() == 0){
					// System.out.println("Iterations: " +
					// solver.getIterations());
					// break;
					// }
					//
					// }
					//
					// System.out.println("=========================");

				} catch (FileNotFoundException e) {
				}
			}
		}
		System.out.println("Average results for " + TRIES + " calculations:");
		System.out.println("----------------");
		int wholeSum = 0;
		for (int j = 0; j < listOfSudokus.length; j++) {
			int sum = 0;
			for (int i = 0; i < TRIES; i++) {
				sum += results[i][j];
			}
			wholeSum += sum;
			System.out.println("Sudoku #" + (j + 1) + ": " + sum / TRIES);
		}

		System.out.println("----------------");
		System.out.println("TOTAL AVERAGE: " + wholeSum / (TRIES * listOfSudokus.length));

		//
		// int[][] initial = { { 0, 0, 6, 0, 8, 9, 4, 0, 7 }, { 0, 0, 0, 0, 7,
		// 0, 0, 6, 0 },
		// { 0, 7, 0, 6, 0, 0, 0, 2, 0 }, { 4, 0, 8, 0, 0, 5, 0, 9, 0 }, { 7, 0,
		// 0, 0, 0, 0, 0, 0, 1 },
		// { 0, 9, 0, 7, 0, 0, 6, 0, 2 }, { 0, 8, 0, 0, 0, 2, 0, 5, 0 }, { 0, 4,
		// 0, 0, 1, 0, 0, 0, 0 },
		// { 3, 0, 2, 4, 5, 0, 9, 0, 0 } };

	}

}
