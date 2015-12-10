package at.ac.uibk;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.Arrays;

public class Particle implements Dominatable<Particle> {
	private double[] speed;
	private double[] values;
	private double[] eval;

	// remember local best, dunno if getters are needed
	private Particle best;
	private SecureRandom sr = new SecureRandom();
	private ZDT1 problem = new ZDT1();

	public Particle() {
		this(30);
	}

	private Particle(int num_vars) {
		speed = new double[num_vars]; // defaults to 0
		values = new double[num_vars]; // randomize these vars
		initValues();
		eval = problem.evaluate(values);

		best = new Particle(this);
	}

	public Particle(Particle p) {
		speed = Arrays.copyOf(p.getSpeed(), p.getSpeed().length);
		values = Arrays.copyOf(p.getValues(), p.getValues().length);
		eval = Arrays.copyOf(p.getEval(), p.getEval().length);
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

		eval = problem.evaluate(values);
		updateBestValue();
	}

	public void updateBestValue() {
		DominationStatus dom = this.dominateable(best);

		// positive is this dominates
		if (dom.equals(DominationStatus.DOMINATES)) {
			best = new Particle(this);
		}
		if (dom.equals(DominationStatus.DOMINATED)) {
			return;
		}

		if (dom.equals(DominationStatus.NONDOMINATABLE) && sr.nextBoolean()) {
			best = new Particle(this);
		}
	}

	public void updateSpeed(double[] bestGlobal) {
		double r1 = sr.nextDouble();
		double r2 = sr.nextDouble();
		double c1 = 1.5 + (2 - 1.5) * sr.nextDouble(); // 2 - first constant
		double c2 = 1.5 + (2 - 1.5) * sr.nextDouble();
		double w = 0.1 + (0.5 - 0.1) * sr.nextDouble();

		for (int i = 0; i < speed.length; i++) {
			speed[i] = w * speed[i] + c1 * r1 * (best.getValues()[i] - this.values[i]) + c2 * r2
					* (bestGlobal[i] - this.values[i]);
		}
	}

	// TODO: String.format
	public String toString() {
		return "(" + new BigDecimal(eval[0]).setScale(2, RoundingMode.HALF_UP).doubleValue() + ", "
				+ new BigDecimal(eval[1]).setScale(2, RoundingMode.HALF_UP).doubleValue() + ")";
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

	@Override
	public DominationStatus dominateable(Particle o) {
		// returns negative if this is dominated, positive if the other and 0 if
		// non
		double[] e = o.getEval();

		boolean isDominated = true;
		boolean dominates = true;

		for (int i = 0; i < eval.length; i++) {
			if (eval[i] > e[i])
				dominates = false;
			
			if (eval[i] < e[i])
				isDominated = false;
		}

		if (dominates)
			return DominationStatus.DOMINATES;
		if (isDominated)
			return DominationStatus.DOMINATED;
		return DominationStatus.NONDOMINATABLE;
	}
}
