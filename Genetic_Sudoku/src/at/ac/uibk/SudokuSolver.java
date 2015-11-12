package at.ac.uibk;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SudokuSolver {
	private final int MAX_POPULATION = 10;
	private SecureRandom r = new SecureRandom();
	private int[][] initial;

	public SudokuSolver(int[][] initial) {
		this.initial = initial;
	}

	public Sudoku solve() {
		List<Sudoku> population = generateInitialPopulation();
		Sudoku best = population.get(0);

		evaluate(population);

		// boolean cond = true;
		int i = 0;
		do {
			List<Sudoku> tmp = recombine(population);

			tmp = mutate(tmp);

			evaluate(tmp);

			population = select(population, tmp);

			if (best.getContradictions() == population.get(0).getContradictions()) {
				i++;
			} else {
				i = 0;
				best = population.get(0);
			}
		} while (i < 100 && best.getContradictions() != 0);

		// return first entry (lowest number of mistakes)
		return best;
	}

	@SuppressWarnings("unused")
	private List<Sudoku> generateRandomInitialPopulation() {
		List<Sudoku> population = new ArrayList<Sudoku>();

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

			population.add(new Sudoku(tmp));
		}

		return population;
	};

	/**
	 * Populate based on the missing elements to have a better overall element
	 * distribution
	 * 
	 * @return
	 */
	private List<Sudoku> generateInitialPopulation() {
		int[] stack = new int[9];

		List<Sudoku> population = new ArrayList<Sudoku>();

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

			population.add(new Sudoku(tmpSudoku));
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
	private void evaluate(List<Sudoku> population) {
		for (Sudoku entry : population) {
			entry.getContradictions();
		}
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
	private List<Sudoku> recombine(List<Sudoku> population) {
		List<Sudoku> children = new ArrayList<Sudoku>();

		for (int i = 0; i < MAX_POPULATION; i++) {
			// Choose parents
			Sudoku parent1, parent2;

			parent1 = getRandomParent(population);
			parent2 = getRandomParent(population);

			// combine parents
			// simple cut
			List<Sudoku> tmp = singleSlice(parent1, parent2);
			children.addAll(tmp);

		}

		return children;
	}

	private Sudoku getRandomParent(List<Sudoku> population) {
		int rand1 = r.nextInt(population.size());
		int rand2 = r.nextInt(population.size());

		if (population.get(rand1).getContradictions() < population.get(rand2).getContradictions()) {
			return population.get(rand1);
		} else {
			return population.get(rand2);
		}
	}

	private List<Sudoku> singleSlice(Sudoku parent1, Sudoku parent2) {
		int cut = r.nextInt(9);

		int[][] tmp1 = new int[9][9];
		int[][] tmp2 = new int[9][9];
		int[][] par1 = parent1.getSudoku();
		int[][] par2 = parent2.getSudoku();

		for (int j = 0; j < 9; j++) {
			if (j < cut) {
				tmp1[j] = par1[j];
				tmp2[j] = par2[j];
			} else {
				tmp1[j] = par2[j];
				tmp2[j] = par1[j];
			}
		}

		// int cut = r.nextInt(81);
		// int[][] tmp = new int[9][9];
		// int[][] par1 = parent1.getSudoku();
		// int[][] par2 = parent2.getSudoku();
		//
		// for (int j = 0; j < 9; j++) {
		// for (int k = 0; k < 9; k++) {
		// if ((j * 9 + k) < cut) {
		// tmp[j][k] = par1[j][k];
		// } else {
		// tmp[j][k] = par2[j][k];
		// }
		// }
		// }

		List<Sudoku> ret = new ArrayList<Sudoku>();
		ret.add(new Sudoku(tmp1));
		ret.add(new Sudoku(tmp2));

		return ret;
	}

	private List<Sudoku> mutate(List<Sudoku> population) {
		// mutate into more feasible!
		List<Sudoku> mutated = new ArrayList<Sudoku>();
		for (Sudoku entry : population) {
			int[][] x = entry.getSudoku();

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
			mutated.add(new Sudoku(temp));
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
	private List<Sudoku> select(List<Sudoku> initial, List<Sudoku> evolved) {
		List<Sudoku> output = new ArrayList<Sudoku>();

		Collections.sort(initial);
		Collections.sort(evolved);

		for (int i = 0; i < MAX_POPULATION; i++) {
			if (initial.get(0).getContradictions() < evolved.get(0).getContradictions()) {
				output.add(initial.get(0));

				initial.remove(0);
			} else {
				output.add(evolved.get(0));

				evolved.remove(0);
			}
		}

		return output;
	}
}
