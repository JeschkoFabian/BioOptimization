package at.ac.uibk.bioopt;


public class Tour {

	private int path[];
	private double distArr[][];
	private double distance;

	public Tour(int a[], double[][] distArr2) {
		this.path = a;
		this.distArr = distArr2;
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

	public double getDistance() {
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
			tourString += Integer.toString(i) + " -> ";
		}
		tourString += Integer.toString(this.path[0]);
		tourString += "  |  Distance: " + Math.round(this.getDistance());
		return tourString;
	}

}
