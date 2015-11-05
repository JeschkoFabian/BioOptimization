package at.ac.uibk;

public class SolverExec {

	public static void main(String[] args) {
		int[][] initial = { { 0, 0, 6, 0, 8, 9, 4, 0, 7 }, { 0, 0, 0, 7, 0, 0, 6, 0 },
				{ 0, 7, 0, 6, 0, 0, 0, 2, 0 }, { 4, 0, 8, 0, 0, 5, 0, 9, 0 }, { 7, 0, 0, 0, 0, 0, 0, 0, 1 },
				{ 0, 9, 0, 7, 0, 0, 6, 0, 2 }, { 0, 8, 0, 0, 0, 2, 0, 5, 0 }, { 0, 4, 0, 0, 1, 0, 0, 0, 0 },
				{ 3, 0, 2, 4, 5, 0, 9, 0, 0 } };

		SudokuSolver solver = new SudokuSolver(initial);

		int[][] solution = solver.solve();
		
		for (int i = 0; i < 9; i++){
			for (int j = 0; j < 9; j++){
				System.out.print(solution[i][j] + ", ");
			}
			System.out.println("");
		}
	}

}
