package at.ac.uibk;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;

public class Particle {
	private double[] speed;
	private double[] values;
	private double[] eval;

	// remember local best, dunno if getters are needed
	private Particle best;
	SecureRandom sr = new SecureRandom();
	private ZDT1 problem = new ZDT1();

	public Particle() {
		this(30);
	}

	private Particle(int num_vars) {
		speed = new double[num_vars]; // default set to 0
		values = new double[num_vars]; // randomize these vars
		initValues();
		eval();
		best = this;

	}

	private void initValues() {

		for (int i = 0; i < speed.length; i++) {
			values[i] = sr.nextDouble();
		}
	}

	public void move() {
		for (int i = 0; i < values.length; i++) {
			values[i] += speed[i];
			if (values[i] < problem.lowerLimit_[i]) {
				values[i] = problem.lowerLimit_[i];
				speed[i] *= -1;
			}
			if (values[i] > problem.upperLimit_[i]) {
				values[i] = problem.upperLimit_[i];
				speed[i] *= -1;
			}
		}
	}

	
	public boolean isDominantTo(Particle p2){
		double[] eval1 = this.getEval();
		double[] eval2 = p2.getEval();
		for (int i = 0; i < eval1.length; i++) {
			if (eval1[i] < eval2[i]) {
				return false;
			}
		}
		return true;	
	}

	public void updateBestValue() {
		if (this.isDominantTo(best)) {
			best = this;
		}if(best.isDominantTo(this)){
			return;
		}else{
			if(sr.nextBoolean()){
				best= this;
			}
		}
	}

	public void updateSpeed(double[] bestGlobal) {
		double r1 = sr.nextDouble();
		double r2 = sr.nextDouble();
		double C1 = 1.5 + (2 - 1.5) * sr.nextDouble();
		double C2 = 1.5 + (2 - 1.5) * sr.nextDouble();
		double W = 0.1 + (0.5 - 0.1) * sr.nextDouble();

		for (int i = 0; i < speed.length; i++) {
			speed[i] = W * speed[i] + C1 * r1 * (best.getValues()[i] - this.values[i])
					+ C2 * r2 * (bestGlobal[i] - this.values[i]);
		}
	}

	public void eval() {
		eval = problem.evaluate(values);
	}

	public String toString() {
		return "(" + new BigDecimal(eval[0]).setScale(2, RoundingMode.HALF_UP).doubleValue() + ", " + new BigDecimal(eval[1]).setScale(2, RoundingMode.HALF_UP).doubleValue() + ")";
	}

	// ===========================================
	// getters & setters
	// ===========================================
	public double[] getSpeed() {
		return speed;
	}

	public void setSpeed(double[] speed) {
		this.speed = speed;
	}

	public double[] getValues() {
		return values;
	}

	public void setValues(double[] values) {
		this.values = values;
	}

	public double[] getEval() {
		return eval;
	}

}
