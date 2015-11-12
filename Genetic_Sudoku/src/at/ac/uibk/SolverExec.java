package at.ac.uibk;

public class SolverExec {

	public static void main(String[] args) {
		int[][] initial = { { 0, 0, 6, 0, 8, 9, 4, 0, 7 }, { 0, 0, 0, 0, 7, 0, 0, 6, 0 },
				{ 0, 7, 0, 6, 0, 0, 0, 2, 0 }, { 4, 0, 8, 0, 0, 5, 0, 9, 0 }, { 7, 0, 0, 0, 0, 0, 0, 0, 1 },
				{ 0, 9, 0, 7, 0, 0, 6, 0, 2 }, { 0, 8, 0, 0, 0, 2, 0, 5, 0 }, { 0, 4, 0, 0, 1, 0, 0, 0, 0 },
				{ 3, 0, 2, 4, 5, 0, 9, 0, 0 } };

		SudokuSolver solver = new SudokuSolver(initial);

		Sudoku solution = solver.solve();
		
		System.out.println(solution);
	}

}
