package at.ac.uibk.bioopt;

public class Node {
	
	private double id, x, y;
	
	public Node(Double double1, Double double2, Double double3){
		this.id = double1;
		this.x = double2;
		this.y = double3;
	}

	public double getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}
