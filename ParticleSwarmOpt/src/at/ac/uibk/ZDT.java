package at.ac.uibk;

public interface ZDT {

	public double[] evaluate(double[] solution);

	public double[] getUpperLimit();

	public double[] getLowerLimit();
	
	public int getNumberOfVariable();
}
