package at.ac.uibk;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SolverExec {

	public static void main(String[] args) {
		int[][] initial = { { 0, 0, 6, 0, 8, 9, 4, 0, 7 }, { 0, 0, 0, 0, 7, 0, 0, 6, 0 },
				{ 0, 7, 0, 6, 0, 0, 0, 2, 0 }, { 4, 0, 8, 0, 0, 5, 0, 9, 0 }, { 7, 0, 0, 0, 0, 0, 0, 0, 1 },
				{ 0, 9, 0, 7, 0, 0, 6, 0, 2 }, { 0, 8, 0, 0, 0, 2, 0, 5, 0 }, { 0, 4, 0, 0, 1, 0, 0, 0, 0 },
				{ 3, 0, 2, 4, 5, 0, 9, 0, 0 } };

		SudokuSolver asd = new SudokuSolver(initial);
		Sudoku out = asd.solve(50, 15, false);

		System.out.println("=========================");
		System.out.println("Assignment sudoku");
		System.out.println(out);
		System.out.println("Iterations: " + asd.getIterations());

		File folder = new File("sudokus");
		File[] listOfSudokus = folder.listFiles();

		// int[][] asd = { { 5, 3, 4, 6, 7, 8, 9, 1, 2 }, { 6, 7, 2, 1, 9, 5, 3,
		// 4, 8 },
		// { 1, 9, 8, 3, 4, 2, 5, 6, 7 }, { 8, 5, 9, 7, 6, 1, 4, 2, 3 }, { 4, 2,
		// 6, 8, 5, 3, 7, 9, 1 },
		// { 7, 1, 3, 9, 2, 4, 8, 5, 6 }, { 9, 6, 1, 5, 3, 7, 2, 8, 4 }, { 2, 8,
		// 7, 4, 1, 9, 6, 3, 5 },
		// { 3, 4, 5, 2, 8, 6, 1, 7, 9 } };
		// Sudoku test = new Sudoku(asd);
		//
		// System.out.println(test.getDeviation() + " " +
		// test.getContradictions());

		// run the whole thing a few times.

		System.out.println("=========================");

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
				Sudoku s = new Sudoku(tmp);
				s.calculateAdditionalValues();
				SudokuSolver solver = new SudokuSolver(s.getSudoku());

				// 5 iterations with 5 consecutive errors as break off and
				// by creating an elite set
				Sudoku solution = solver.solve(5, 15, true);

				// similar results for both
				// Sudoku solution = solver.solve(50, 15, false);

				System.out.println(listOfSudokus[k].getName());
				System.out.println(solution);
				System.out.println("Iterations: " + solver.getIterations());

				System.out.println("=========================");

			} catch (FileNotFoundException e) {
			}
		}

		System.out.println("FIN");
	}
}
