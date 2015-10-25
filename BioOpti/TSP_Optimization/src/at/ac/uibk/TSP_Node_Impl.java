package at.ac.uibk;

public class TSP_Node_Impl implements TSP_Node{
	private double x, y;
	
	public TSP_Node_Impl(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	@Override
	public double calculateDistance(TSP_Node other) {
		TSP_Node_Impl node = (TSP_Node_Impl) other;
		
		return Math.sqrt(Math.pow(x - node.getX(), 2) + Math.pow(y - node.getY(), 2));
	}

	@Override
	public String toString() {
		return "Node[" + x + ", " + y + "]";
	}
}
