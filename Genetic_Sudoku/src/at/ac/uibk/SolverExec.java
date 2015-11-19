package at.ac.uibk;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SolverExec {

	public static void main(String[] args) {
		File folder = new File("sudokus");
		
		File[] listOfSudokus = folder.listFiles();
		System.out.println("=========================");
				
		for (File f : listOfSudokus){
			try {
				Scanner sc = new Scanner(f);
				int [][] tmp = new int[9][9];
				
				for (int i = 0; i < 9; i++){
					for (int j = 0; j < 9; j++){
						if (sc.hasNext("[0-9]")){
							tmp[i][j] = Integer.valueOf(sc.next("[0-9]"));
						}
					}
				}
				
				sc.close();
				
				SudokuSolver solver = new SudokuSolver(tmp);

				Sudoku solution = solver.solve(50);
				
				System.out.println(solution);
				System.out.println("Iterations: " + solver.getIterations());
//				
				
//				int best = Integer.MAX_VALUE;
//				for (int i = 0; i < 50; i++){
//					Sudoku solution = solver.solve();
//					
//					if (solution.getContradictions() < best){
//						best = solution.getContradictions();
//						
//						System.out.print(solution);
//						
//					}
//					
//					if (solution.getContradictions() == 0){
//						System.out.println("Iterations: " + solver.getIterations());
//						break;
//					} 
//					
//				}
//				
				System.out.println("=========================");
						
			} catch (FileNotFoundException e) {
			}
		}
		
		
//		
//		int[][] initial = { { 0, 0, 6, 0, 8, 9, 4, 0, 7 }, { 0, 0, 0, 0, 7, 0, 0, 6, 0 },
//				{ 0, 7, 0, 6, 0, 0, 0, 2, 0 }, { 4, 0, 8, 0, 0, 5, 0, 9, 0 }, { 7, 0, 0, 0, 0, 0, 0, 0, 1 },
//				{ 0, 9, 0, 7, 0, 0, 6, 0, 2 }, { 0, 8, 0, 0, 0, 2, 0, 5, 0 }, { 0, 4, 0, 0, 1, 0, 0, 0, 0 },
//				{ 3, 0, 2, 4, 5, 0, 9, 0, 0 } };

	}

}
