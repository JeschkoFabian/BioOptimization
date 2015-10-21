package at.ac.uibk;

public class TSP_Node_Impl implements TSP_Node{
	private int x, y;
	
	public TSP_Node_Impl(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	@Override
	public int calculateDistance(TSP_Node other) {
		TSP_Node_Impl node = (TSP_Node_Impl) other;
		
		return (int) Math.sqrt(Math.pow(x - node.getX(), 2) + Math.pow(y - node.getY(), 2));
	}

	@Override
	public String toString() {
		return "Node[" + x + ", " + y + "]";
	}
}
