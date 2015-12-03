package at.ac.uibk;

import java.security.SecureRandom;
import java.util.Arrays;

public class Particle {
	private double[] speed;
	private double[] values;
	
	// remember local best, dunno if getters are needed
	private double[] bestEval;
	private double[] bestValues;
	
	private ZDT1 problem = new ZDT1();
	
	public Particle (int num_vars){
		speed = new double[num_vars];	// default set to 0
		values = new double[num_vars]; 	// randomize these vars
		initValues();
		
		bestValues = Arrays.copyOf(values, num_vars);
		bestEval = new double[2];
	}
	
	private void initValues(){
		SecureRandom sr = new SecureRandom();
		for (int i = 0; i < speed.length; i ++){
			values[i] = sr.nextDouble();
		}
		
		bestEval[0] = Double.MAX_VALUE;
		bestEval[1] = Double.MAX_VALUE;
	}

	
	public void move(){
		for (int i = 0; i < values.length; i++){
			values[i] += speed[i];
		}
		
		// maybe not always take the next not dominated.
		if (!isDominatedBy(bestEval)){
			bestValues = Arrays.copyOf(values, values.length);
			// need to set bestEval without recalculating..
		}
	}
	 
	// seems bad, need to restructure probably
	public boolean isDominatedBy(double[] eval){
		double[] currentEval = problem.evaluate(values);
		
		for (int i = 0; i < eval.length; i++){
			if (currentEval[i] < eval[i]){
				return false;
			}
		}
		
		return true;
	}

	//===========================================
	// getters & setters 
	//===========================================
	public double[] getSpeed(){
		return speed;
	}
	
	public void setSpeed(double[] speed){
		this.speed = speed;
	}
	
	public double[] getValues() {
		return values;
	}

	public void setValues(double[] values) {
		this.values = values;
	}
}
