package at.ac.uibk;

public class TSP_Exec {

	public static void main(String[] args) {
		int sz = 25;
		TSP_Node[] nodes = new TSP_Node_Impl[sz];

		for (int i = 0; i < sz; i++) {
			nodes[i] = new TSP_Node_Impl((int) Math.random() * 1000, (int) Math.random() * 1000);
		}
		
		
	}

}
