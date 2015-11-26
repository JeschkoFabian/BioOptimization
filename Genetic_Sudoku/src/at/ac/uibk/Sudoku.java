package at.ac.uibk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sudoku implements Comparable<Sudoku> {
	private int contradictions = -1;
	private int[][] sudoku;
	private Map<Integer, List<Integer>> impossibleValues;

	public Sudoku(int[][] sudoku) {
		this.sudoku = sudoku;
		//calculateImpossibleValues();
		//fillAdditionalValues();
	}

	public int[][] getSudoku() {
		return sudoku;
	}

	public void setSudoku(int[][] sudoku) {
		contradictions = -1;
		this.sudoku = sudoku;
	}

	public int get(int x, int y) {
		return sudoku[x][y];
	}

	public void set(int x, int y, int val) {
		if (val > 0 && val <= 9)
			sudoku[x][y] = val;
		else
			throw new IllegalArgumentException("Value not between 1 and 9");
	}

	/**
	 * Function that evaluates how many contradictions a given Sudoku of the
	 * size 9x9 has.
	 * 
	 * Does not check the integrity of the 3x3 boxes. Reason being, we generate
	 * valid boxes and never apply changes that destroy the validity. If the box
	 * was invalid per definition... dunno, but it is filled as correctly as
	 * possible afterwards.
	 * 
	 * @param sudoku
	 *            some completely filled sudoku
	 * @return number of contradictions
	 */
	public int getContradictions() {
		if (contradictions != -1)
			return contradictions;

		int contradictions = 0;

		boolean[] horizontalNums;
		boolean[][] verticalNums = new boolean[9][9];

		for (int i = 0; i < 9; i++) {
			verticalNums[i] = getFalseArr();
		}

		// fixed some more index problems
		for (int i = 0; i < 9; i++) {
			horizontalNums = getFalseArr();

			for (int j = 0; j < 9; j++) {
				// check horizontal
				if (horizontalNums[sudoku[i][j] - 1]) {
					contradictions++;
				} else {
					horizontalNums[sudoku[i][j] - 1] = true;
				}

				// check vertical
				if (verticalNums[j][sudoku[i][j] - 1]) {
					contradictions++;
				} else {
					verticalNums[j][sudoku[i][j] - 1] = true;
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

	public void calculateImpossibleValues() {

		impossibleValues = new HashMap<Integer, List<Integer>>();
		for (int k = 0; k < 9; k++) {
			int xOff = (k % 3) * 3;
			int yOff = (k / 3) * 3;
			for (int x = xOff; x < xOff + 3; x++) {
				for (int y = yOff; y < yOff + 3; y++) {
					if (sudoku[x][y] == 0) {
						List<Integer> impossible = new ArrayList<Integer>();
						impossible.addAll(getRow(x, y));
						impossible.addAll(getColumn(x, y));
						impossible.addAll(getSubgrid(xOff, yOff, x, y));
						impossibleValues.put(x * 9 + y, impossible);
					}
				}
			}
		}
		System.out.println(impossibleValues.toString());

	}

	private List<Integer> getSubgrid(int xOff, int yOff, int x, int y) {
		List<Integer> impossibleValues = new ArrayList<Integer>();
		for (int i = xOff; i < xOff + 3; i++) {
			for (int j = yOff; j < yOff + 3; j++) {
				if (sudoku[i][j] != 0 && i != x && j != y) {
					impossibleValues.add(sudoku[i][j]);
				}
			}
		}
		return impossibleValues;
	}

	private List<Integer> getColumn(int x, int y) {
		List<Integer> impossibleValues = new ArrayList<Integer>();
		for (int i = 0; i < 9; i++) {
			if (i != y && sudoku[x][i] != 0) {
				impossibleValues.add(sudoku[x][i]);
			}

		}
		return impossibleValues;
	}

	private List<Integer> getRow(int x, int y) {
		List<Integer> impossibleValues = new ArrayList<Integer>();
		for (int i = 0; i < 9; i++) {
			if (i != x && sudoku[i][y] != 0) {
				impossibleValues.add(sudoku[i][y]);
			}

		}
		return impossibleValues;
	}

	public List<Integer> getImpossibleValues(int x, int y) {
		return impossibleValues.get(x * 9 + y);
	}

	private void fillAdditionalValues() {
		for (int i = 0; i < 9; i++) {
			int xOff = (i % 3) * 3;
			int yOff = (i / 3) * 3;
			for (int x = xOff; x < xOff + 3; x++) {
				for (int y = yOff; y < yOff + 3; y++) {
					if (sudoku[x][y] != 0) {
						for (int j = 1; j <= 9; j++) {
							if (checkIfImpossibleInRow(x, y, j)
									|| checkIfImpossibleInColumn(x, y, j)
									|| checkIfImpossibleInSubgrid(x, y, j,
											xOff, yOff)) {
								sudoku[x][y] = j;
								break;
							}
						}
					}
				}
			}
		}
	}

	private boolean checkIfImpossibleInSubgrid(int x, int y, int j, int xOff,
			int yOff) {
		boolean impossible = true;
		for (int i = xOff; i < xOff + 3; i++) {
			for (int k = yOff; k < yOff + 3; k++) {
				if (sudoku[i][k] != 0) {
					if (!getImpossibleValues(i, k).contains(j) && i != x
							&& k != y) {
						impossible = false;
					}
				}
			}
		}
		return impossible;
	}

	private boolean checkIfImpossibleInColumn(int x, int y, int j) {
		boolean impossible = true;
		for (int i = 0; i < 9; i++) {
			if (sudoku[x][i] != 0) {
				if (i != y && !getImpossibleValues(x, i).contains(j)) {
					impossible = false;
				}
			}
		}
		return impossible;
	}

	private boolean checkIfImpossibleInRow(int x, int y, int j) {
		boolean impossible = true;
		for (int i = 0; i < 9; i++) {
			if (sudoku[i][y] != 0) {
				if (i != x && !getImpossibleValues(i, y).contains(j)) {
					impossible = false;
				}
			}
		}
		return impossible;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String line = "+-------+-------+-------+" + System.lineSeparator();

		sb.append("Contradictions: " + getContradictions()
				+ System.lineSeparator());

		sb.append(line);

		for (int i = 0; i < 9; i++) {
			sb.append("| ");
			for (int j = 0; j < 9; j++) {
				if (j != 8)
					if ((j + 1) % 3 == 0)
						sb.append(sudoku[i][j] + " | ");
					else
						sb.append(sudoku[i][j] + " ");
				else {
					sb.append(sudoku[i][j]);
				}
			}
			sb.append(" |" + System.lineSeparator());

			if ((i + 1) % 3 == 0 && i != 8) {
				sb.append(line);
			}
		}
		sb.append(line);

		return sb.toString();
	}

	@Override
	public int compareTo(Sudoku o) {
		// if this less then o -> negative return
		return this.getContradictions() - o.getContradictions();
	}

}
