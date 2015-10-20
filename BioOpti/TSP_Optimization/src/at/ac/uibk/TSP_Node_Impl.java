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
		
		return (int) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}

}
