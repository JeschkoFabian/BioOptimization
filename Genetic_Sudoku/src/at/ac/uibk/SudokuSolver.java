package at.ac.uibk;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

		TreeMap<Integer, int[][]> fitPop = evaluate(population);

		// boolean cond = true;
		int i = 0;
		while (i < 1000) {
			List<int[][]> tmp = recombine(fitPop);

			tmp = mutate(tmp);

			TreeMap<Integer, int[][]> fitTmp = evaluate(tmp);

			fitPop = select(fitPop, fitTmp);

			i++;
		}
		// return first entry (lowest number of mistakes)
		return fitPop.firstEntry().getValue();
	}

	@SuppressWarnings("unused")
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
					// fixed some index problems - please double check!
					stack[initial[i][j] - 1]--;
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
						tmpSudoku[j][k] = tmp + 1;
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
	 * Calculate all contradictions for all sudokus.
	 * 
	 * alternate solution: sum up all rows/columns, need to be sum(1-9), the
	 * closer the better if all are sum(1-9) then optimal
	 * 
	 * @param population
	 * @return 
	 */
	private TreeMap<Integer, int[][]> evaluate(List<int[][]> population) {
		TreeMap<Integer, int[][]> evaluation = new TreeMap<Integer, int[][]>();

		for (int[][] entry : population) {
			int contradictions = evaluateSudoku(entry);

			evaluation.put(contradictions, entry);
		}

		return evaluation;
	}

	/**
	 * Function that evaluates how many contradictions a given sudoku of the
	 * size 9x9 has.
	 * 
	 * @param sudoku
	 *            some completely filled sudoku
	 * @return number of contradictions
	 */
	private int evaluateSudoku(int[][] sudoku) {
		int contradictions = 0;

		boolean[] horizontalNums = getFalseArr();
		boolean[][] verticalNums = new boolean[9][9];

		for (int i = 0; i < 9; i++) {
			verticalNums[i] = getFalseArr();
		}

		// fixed some more index problems
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				// check horizontal
				if (horizontalNums[sudoku[i][j] - 1]) {
					contradictions++;
				} else {
					horizontalNums[sudoku[i][j] - 1] = true;
				}

				// check vertical
				if (verticalNums[i][sudoku[i][j] - 1]) {
					contradictions++;
				} else {
					verticalNums[i][sudoku[i][j] - 1] = true;
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

	/**
	 * Takes a TreeMap as input, chooses two random candidates for the
	 * combination. The smaller one will be chosen as first parent, the process
	 * is repeated for the second parent. Once both parents have been decided, a
	 * random cut point will be defined. Up to the cutpoint all elements from
	 * the parent are taken, from then all the elements from the second are
	 * taken. This is repeated MAX_POPULATION times.
	 * 
	 * @param population
	 *            the initial TreeMap containing data.
	 * @return MAX_POPULATION combined elements
	 */
	private List<int[][]> recombine(TreeMap<Integer, int[][]> population) {
		List<int[][]> children = new ArrayList<int[][]>();

		for (int i = 0; i < MAX_POPULATION; i++) {
			// Choose parents
			List<Integer> keys = new ArrayList<Integer>(population.keySet());

			// TODO: extract to function
			int parent1, parent2;
			int rand1 = r.nextInt(population.size());
			int rand2 = r.nextInt(population.size());

			if (rand1 < rand2) {
				parent1 = keys.get(rand1);
			} else {
				parent1 = keys.get(rand2);
			}

			rand1 = r.nextInt(population.size());
			rand2 = r.nextInt(population.size());

			if (rand1 < rand2) {
				parent2 = keys.get(rand1);
			} else {
				parent2 = keys.get(rand2);
			}

			// combine parents
			// simple cut
			// TODO: extract to function, will implement different combine
			// variants
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
			// Count the amount of each number
			Map<Integer, Integer> countMap = new HashMap<Integer, Integer>();
			for (int i = 0; i < 9; i++) {
				countMap.put(i, 0);
			}
			int[][] temp = new int[9][9];
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					countMap.put(x[i][j] - 1, countMap.get(x[i][j] - 1) + 1);
				}
			}

			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {

					if (initial[i][j] == 0) {
						temp[i][j] = x[i][j];
					} else {
						// based on the amount of each number, the probability
						// for mutation is slightly varied
						// e.g. #8 is 12 times in the current sudoku -> prob.
						// for mutation goes from 1/81 to (12/9)/81
						// still much work to be done here...
						if (r.nextInt(81) < countMap.get(x[i][j] - 1) / 9) {
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

	/**
	 * Takes two TreeMap's as input and will take the ten smallest elements
	 * overall and will return them.
	 * 
	 * @param initial
	 *            first TreeMap to compare
	 * @param evolved
	 *            second TreeMap to compare
	 * @return the MAX_POPULATION smallest entries
	 */
	private TreeMap<Integer, int[][]> select(TreeMap<Integer, int[][]> initial, TreeMap<Integer, int[][]> evolved) {
		TreeMap<Integer, int[][]> output = new TreeMap<Integer, int[][]>();

		List<Integer> keysInitial = new ArrayList<Integer>(initial.keySet());
		List<Integer> keysEvolved = new ArrayList<Integer>(evolved.keySet());

		for (int i = 0; i < MAX_POPULATION; i++) {
			// get the lowest number of failures for each
			int smallestInitial = keysInitial.get(0);
			int smallestEvolved = keysEvolved.get(0);

			// add the smaller to the output and remove the element
			if (smallestInitial < smallestEvolved) {
				output.put(smallestInitial, initial.get(smallestInitial));

				keysInitial.remove(0);
			} else {
				output.put(smallestEvolved, evolved.get(smallestEvolved));

				keysEvolved.remove(0);
			}
		}

		return output;
	}
}
