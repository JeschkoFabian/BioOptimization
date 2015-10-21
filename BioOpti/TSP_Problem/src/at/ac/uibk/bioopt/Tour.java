package at.ac.uibk.bioopt;


public class Tour {

	private int path[];
	private int distArr[][];
	private int distance;

	public Tour(int a[], int b[][]) {
		this.path = a;
		this.distArr = b;
	}

	private void calculateDistance() {
		this.distance = 0;
		for (int i = 0; i < this.path.length; i++) {
			int src = path[i % this.path.length];
			int dst = path[(i + 1)% this.path.length];
			this.distance += distArr[src][dst];
		}
		//System.out.println("Distance: " + distance);
	}

	public int[] getPath() {
		return path;
	}

	public void setPath(int[] path) {
		this.path = path;
	}

	public int getDistance() {
		calculateDistance();
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getNode(int c) {
		// TODO Auto-generated method stub
		return path[c];
	}

	public void setNode(int c, int node) {
		path[c] = node;
		
	}
	
	public String toString(){
		String tourString = new String();
		for (int i : this.path){
			tourString += Integer.toString(i) + " --> ";
		}
		tourString += Integer.toString(this.path[0]);
		tourString += "  |  Distance: " + this.getDistance();
		return tourString;
	}

}
