package at.ac.uibk;

public class Sudoku implements Comparable<Sudoku> {
	private int contradictions = -1;
	private float deviation = -1;
	private int[][] sudoku;

	public Sudoku(int[][] sudoku) {
		this.sudoku = sudoku;
	}

	public int[][] getSudoku() {
		return sudoku;
	}

	public void setSudoku(int[][] sudoku) {
		deviation = -1;
		contradictions = -1;
		this.sudoku = sudoku;
	}

	public int get(int x, int y) {
		return sudoku[x][y];
	}

	public void set(int x, int y, int val) {
		if (val > 0 && val <= 9){
			sudoku[x][y] = val;
			deviation = -1;
			contradictions = -1;
		}else
			throw new IllegalArgumentException("Value not between 1 and 9");
	}

	public float getDeviation() {
		if (deviation != -1)
			return deviation;

		deviation = 0;

		int[] columns = new int[9];

		for (int i = 0; i < 9; i++) {
			int row = 0;

			for (int j = 0; j < 9; j++) {
				row += sudoku[i][j];
				columns[j] += sudoku[i][j];
			}

			deviation += Math.pow(row - 45, 2);
		}

		for (int i = 0; i < 9; i++) {
			deviation += Math.pow(columns[i] - 45, 2);
		}
		
		deviation /= 18;
		
		return deviation;
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
	
	public float getFitness(){
//		return getContradictions();
		return getContradictions() + getDeviation() /10;
	}

	private boolean[] getFalseArr() {
		boolean[] arr = new boolean[9];

		for (int i = 0; i < 9; i++) {
			arr[i] = false;
		}

		return arr;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String line = "+-------+-------+-------+" + System.lineSeparator();

		sb.append("Contradictions: " + getContradictions() + System.lineSeparator());
		sb.append("Deviation: " + getDeviation() + System.lineSeparator());

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
//		return this.getContradictions() - o.getContradictions();
		if (this.getFitness() < o.getFitness())
			return -1;
		if (this.getFitness() > o.getFitness())
			return +1;
		return 0;
	}

}
