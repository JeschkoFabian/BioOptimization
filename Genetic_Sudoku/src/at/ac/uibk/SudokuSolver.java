package at.ac.uibk;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SudokuSolver {
	private final int MAX_POPULATION = 10;
	private SecureRandom r = new SecureRandom();
	private int[][] initial;
	List<int[][]> population;

	public SudokuSolver(int[][] initial) {
		this.initial = initial;
	}

	public int[][] solve() {
		population = generateInitialPopulation();

		int[] fitPop = evaluate(population);

		//boolean cond = true;
		int i = 0;
		int fittest = Integer.MAX_VALUE;
		int [][] solution = new int[9][9];
		while (i<10000) {
			List<int[][]> tmp = recombine(fitPop, population);

			tmp = mutate(tmp);

			int[] fitTmp = evaluate(tmp);

			population = select(population, tmp);
			
			//get the current fittest solution
			for (int x =0; x<10;x++) {
				if (fitTmp[x] < fittest) {
					fittest = fitTmp[x];
					solution = population.get(x);
				}
			}
			i++;
		}
		//System.out.println(fittest);
		return solution;
	}

	private List<int[][]> generateRandomInitialPopulation() {
		List<int[][]> population = new ArrayList<int[][]>();

		for (int i = 0; i < MAX_POPULATION; i++) {
			int[][] tmp = new int[9][9];

			for (int j = 0; j < 9; j++) {
				for (int k = 0; k < 9; k++) {
					if (initial[j][k] == 0) {
						tmp[j][k] = r.nextInt(9) + 1;
					} else {
						tmp[j][k] = initial[j][k];
					}
				}
			}

			population.add(tmp);
		}

		return population;
	};

	/**
	 * Populate based on the missing elements to have a better overall element
	 * distribution
	 * 
	 * @return
	 */
	private List<int[][]> generateInitialPopulation() {
		int[] stack = new int[9];

		List<int[][]> population = new ArrayList<int[][]>();

		for (int i = 0; i < 9; i++) {
		stack[i] = 9;
		}
		
		// remove initial elements from the stack
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (initial[i][j] != 0) {
					//fixed some index problems - please double check!
					stack[initial[i][j]-1]--;
				}
			}
		}

		for (int i = 0; i < MAX_POPULATION; i++) {
			int[] tmpStack = Arrays.copyOf(stack, stack.length);
			int[][] tmpSudoku = copyArr(initial);

			// fill randomly with the remaining
			for (int j = 0; j < 9; j++) {
				for (int k = 0; k < 9; k++) {
					if (initial[j][k] == 0) {
						int tmp = r.nextInt(9);
						while (stack[tmp] < 1) {
							tmp = r.nextInt(9);
						}
						tmpStack[tmp]--;
						tmpSudoku[j][k] = tmp+1;
					}
				
				}
			}

			population.add(tmpSudoku);
		}

		return population;
	}

	private int[][] copyArr(int[][] toCopy) {
		int[][] copied = new int[toCopy.length][];

		for (int i = 0; i < toCopy.length; i++) {
			copied[i] = Arrays.copyOf(toCopy[i], toCopy[i].length);
		}

		return copied;
	}

	/**
	 * return number of contradictions in each solution
	 * 
	 * alternate solution: sum up all rows/columns, need to be sum(1-9), the
	 * closer the better if all are sum(1-9) then optimal
	 * 
	 * @param population
	 */
	private int[] evaluate(List<int[][]> population) {
		int[] contradictions = new int[population.size()];

		for (int x = 0; x < population.size(); x++) {
			int[][] entry = population.get(x);

			boolean[] horizontalNums = getFalseArr();
			boolean[][] verticalNums = new boolean[9][9];

			for (int i = 0; i < 9; i++) {
				verticalNums[i] = getFalseArr();
			}

			//fixed some more index problems
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					// check horizontal
					if (horizontalNums[entry[i][j] - 1]) {
						contradictions[x]++;
					} else {
						horizontalNums[entry[i][j] - 1] = true;
					}

					// check vertical
					if (verticalNums[i][entry[i][j] - 1]) {
						contradictions[x]++;
					} else {
						verticalNums[i][entry[i][j] - 1] = true;
					}
				}
			}
		}

		return contradictions;
	}

	private boolean[] getFalseArr() {
		boolean[] arr = new boolean[9];

		for (int i = 0; i < 9; i++) {
			arr[i] = false;
		}

		return arr;
	}

	private List<int[][]> recombine(int[] eval, List<int[][]> population) {
		List<int[][]> children = new ArrayList<int[][]>();

		for (int i = 0; i < population.size(); i++) {
			// Choose parents

			int parent1, parent2;
			int rand1 = r.nextInt(population.size());
			int rand2 = r.nextInt(population.size());

			if (eval[rand1] < eval[rand2]) {
				parent1 = rand1;
			} else {
				parent1 = rand2;
			}

			rand1 = r.nextInt(population.size());
			rand2 = r.nextInt(population.size());

			if (eval[rand1] < eval[rand2]) {
				parent2 = rand1;
			} else {
				parent2 = rand2;
			}

			// combine parents
			// simple cut
			int cut = r.nextInt(81);
			int[][] temp = new int[9][9];
			int[][] par1 = population.get(parent1);
			int[][] par2 = population.get(parent2);
			for (int j = 0; j < 9; j++) {
				for (int k = 0; k < 9; k++) {
					if ((j * 9 + k) < cut) {
						temp[j][k] = par1[j][k];
					} else {
						temp[j][k] = par2[j][k];
					}
				}
			}
			children.add(temp);

		}

		return children;
	}

	private List<int[][]> mutate(List<int[][]> population) {
		// mutate into more feasible!
		List<int[][]> mutated = new ArrayList<int[][]>();
		for (int[][] x : population) {
			//Count the amount of each number
			Map<Integer, Integer> countMap = new HashMap<Integer, Integer>();
			for (int i = 0; i < 9; i++) {
				countMap.put(i, 0);
			}
			int[][] temp = new int[9][9];
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					countMap.put(x[i][j]-1, countMap.get(x[i][j]-1) + 1);
				}
			}

			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {

					if (initial[i][j] == 0) {
						temp[i][j] = x[i][j];
					} else {
						//based on the amount of each number, the probability for mutation is slightly varied
						//e.g. #8 is 12 times in the current sudoku -> prob. for mutation goes from 1/81 to (12/9)/81
						//still much work to be done here...
						if (r.nextInt(81) < countMap.get(x[i][j]-1) / 9) {
							temp[i][j] = r.nextInt(9) + 1;
						} else {
							temp[i][j] = x[i][j];
						}
					}
				}
			}
			mutated.add(temp);
		}
		return mutated;
	}

	private List<int[][]> select(List<int[][]> initial, List<int[][]> evolved) {
		return initial;
	}
}
