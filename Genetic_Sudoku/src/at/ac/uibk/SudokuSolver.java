package at.ac.uibk;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SudokuSolver {
	private final int MAX_POPULATION = 10;
	private SecureRandom r = new SecureRandom();
	private int[][] initial;

	public SudokuSolver(int[][] initial) {
		this.initial = initial;
	}

	/**
	 * Will solve the initial sudoku.
	 * 
	 * First of all it will generate the initial population and evaluate it,
	 * remembering the best solution.
	 * 
	 * Then a new population will be created through recombining, and afterwards
	 * be mutated. Then the new population will be evaluated and the best
	 * solutions out of the new and old population selected. If there is better
	 * solution found, remember it.
	 * 
	 * This process will be repeated until either 0 contradictions are reached
	 * or no better solution was found for x iterations.
	 * 
	 * @return the best solution found
	 */
	public Sudoku solve() {
		List<Sudoku> population = generateInitialSubgridPopulation();
		evaluate(population);

		Sudoku best = population.get(0);

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

		} while (i < 50 && best.getContradictions() != 0);

		return best;
	}

	@SuppressWarnings("unused")
	/**
	 * NOT USED!
	 * 
	 * Will fill the sudoku completely random. Pretty poor for actual use.
	 * 
	 * @return some population
	 */
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

	// @SuppressWarnings("unused")
	/**
	 * NOTE: This function is currently in use!
	 * 
	 * Generates MAX_POPULATION filled sudokus. In each subgrid the sudokus get
	 * a value that is not used yet assigned. So if a solution exists, the
	 * subgrids will automatically be correct.
	 * 
	 * @return List of filled sudokus
	 */
	private List<Sudoku> generateInitialSubgridPopulation() {
		int[] stack = new int[9];

		List<Sudoku> population = new ArrayList<Sudoku>();

		for (int i = 0; i < MAX_POPULATION; i++) {
			int[][] tmpSudoku = copyArr(initial);

			// iterate over each 3x3 grid
			for (int j = 0; j < 9; j++) {
				// offsets for the subgrids
				int xOff = (j % 3) * 3;
				int yOff = (j / 3) * 3;

				// (re)set stack
				for (int k = 0; k < 9; k++) {
					stack[k] = 1;
				}

				for (int x = xOff; x < xOff + 3; x++) {
					for (int y = yOff; y < yOff + 3; y++) {
						if (tmpSudoku[x][y] != 0) {
							stack[tmpSudoku[x][y] - 1]--;
						}
					}
				}

				for (int x = xOff; x < xOff + 3; x++) {
					for (int y = yOff; y < yOff + 3; y++) {
						if (tmpSudoku[x][y] == 0) {
							int tmp = r.nextInt(9);
							while (stack[tmp] < 1) {
								tmp = r.nextInt(9);
							}
							stack[tmp]--;
							tmpSudoku[x][y] = tmp + 1;
						}
					}
				}
			}

			population.add(new Sudoku(tmpSudoku));
		}

		return population;
	}

	/**
	 * NOT USED!
	 * 
	 * Populate based on the missing elements in the current row to have a
	 * better overall element distribution. Proved to be a poor choice for the
	 * current solution, might be good for another approach.
	 * 
	 * @return some population
	 */
	@SuppressWarnings("unused")
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

	/**
	 * Just creates a new int[][] array out of the given one, proved to be
	 * useful.
	 * 
	 * @param toCopy
	 *            arr to copy
	 * @return the copied arr
	 */
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
	 * Not REALLY necessary but here for the algorithms sake. Does not influence
	 * the performance anyways.
	 * 
	 * @param population
	 *            some sudokus
	 */
	private void evaluate(List<Sudoku> population) {
		for (Sudoku entry : population) {
			entry.getContradictions();
		}
	}

	/**
	 * Will select two random parents (according to two tournament) and apply
	 * the {@link #singleSlice(Sudoku, Sudoku) singleSlice} to generate two
	 * children. These children will be added to the new population.
	 * 
	 * The process is repeated MAX_POPULATION times to generate an array with
	 * twice the size of the initial one.
	 * 
	 * @param population
	 *            the actual unmodified population
	 * @return the bred solutions
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

	/**
	 * Takes two random sudokus and returns the one with fewer contradictions.
	 * (Two tournament)
	 * 
	 * @param population
	 *            all current sudokus
	 * @return a random sudoku
	 */
	private Sudoku getRandomParent(List<Sudoku> population) {
		int rand1 = r.nextInt(population.size());
		int rand2 = r.nextInt(population.size());

		if (population.get(rand1).getContradictions() < population.get(rand2).getContradictions()) {
			return population.get(rand1);
		} else {
			return population.get(rand2);
		}
	}

	/**
	 * Takes two sudokus, generates a random number between 1 and 9 and slices
	 * those at that point (in terms of subgrids). Then the first n subgrids are
	 * taken from each parent and combined with the remaining subgrids of the
	 * other parent. Those two solutions will then be returned.
	 * 
	 * @param parent1
	 *            first parent
	 * @param parent2
	 *            second parent
	 * @return both children
	 */
	private List<Sudoku> singleSlice(Sudoku parent1, Sudoku parent2) {
		int cut = r.nextInt(9);

		int[][] tmp1 = new int[9][9];
		int[][] tmp2 = new int[9][9];
		int[][] par1 = parent1.getSudoku();
		int[][] par2 = parent2.getSudoku();

		for (int i = 0; i < 9; i++) {

			// offsets for the subgrids
			int xOff = (i % 3) * 3;
			int yOff = (i / 3) * 3;

			for (int x = xOff; x < xOff + 3; x++) {
				for (int y = yOff; y < yOff + 3; y++) {
					if (i < cut) {
						tmp1[x][y] = par1[x][y];
						tmp2[x][y] = par2[x][y];
					} else {
						tmp1[x][y] = par2[x][y];
						tmp2[x][y] = par1[x][y];
					}
				}
			}
		}

		List<Sudoku> ret = new ArrayList<Sudoku>();
		ret.add(new Sudoku(tmp1));
		ret.add(new Sudoku(tmp2));

		return ret;
	}

	/**
	 * For each sudoku iterate over the subgrids and with a probability of 1/9
	 * swap two random elements in it (that are not fixed).
	 *
	 * 
	 * @param population
	 *            the crossover list
	 * @return the mutated crossover list
	 */
	private List<Sudoku> mutate(List<Sudoku> population) {
		for (Sudoku sudoku : population) {
			for (int i = 0; i < 9; i++) {
				if (r.nextDouble() > 1.0 / 9) {
					continue;
				}

				// offsets for the subgrids
				int xOff = (i % 3) * 3;
				int yOff = (i / 3) * 3;

				int[][] tmp = sudoku.getSudoku();
				boolean foundOne = false;

				while (!foundOne) {
					int x1 = xOff + r.nextInt(3);
					int x2 = xOff + r.nextInt(3);

					int y1 = yOff + r.nextInt(3);
					int y2 = yOff + r.nextInt(3);

					if (initial[x1][y1] == 0 && initial[x2][y2] == 0) {
						int tmpVal = tmp[x1][y1];
						tmp[x1][y1] = tmp[x2][y2];
						tmp[x2][y2] = tmpVal;

						foundOne = true;
					}
				}

				sudoku.setSudoku(tmp);
			}
		}
		return population;
	}

	/**
	 * Takes two lists of sudokus and will select the MAX_POPULATION best
	 * solutions and return them.
	 * 
	 * 
	 * @param initial
	 *            the unmodified sudokus
	 * @param evolved
	 *            the evolved sudokus
	 * @return best solutions
	 */
	private List<Sudoku> select(List<Sudoku> initial, List<Sudoku> evolved) {
		List<Sudoku> output = new ArrayList<Sudoku>();

		// can be sorted due to implementing comparable on contradictions
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
