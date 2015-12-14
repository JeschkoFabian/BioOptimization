package at.ac.uibk;

/**
 * 
 * @author fabian
 *
 */
public class PSO_Exec {

	public static void main(String[] args) {

		PSO_Solver solver = new PSO_Solver();
		solver.solve(100, 100, 600, new ZDT1());
	}

}
