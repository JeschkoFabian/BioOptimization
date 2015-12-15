package at.ac.uibk;

import java.security.SecureRandom;
import java.util.Arrays;

public class Particle implements Dominatable<Particle> {
	private double[] speed;
	private double[] values;
	private double[] eval;

	// remember local best, dunno if getters are needed
	private Particle best;
	private SecureRandom sr = new SecureRandom();
	private ZDT problem = new ZDT1();

	public Particle(ZDT problem) {
		this.problem = problem;
		int num_vars = problem.getNumberOfVariable();

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
		problem = p.problem;
	}

	private void initValues() {
		for (int i = 0; i < speed.length; i++) {
			double upper = problem.getUpperLimit()[i];
			double lower = problem.getLowerLimit()[i];

			// get random double in the given range
			// somehow this makes ZDT4 drastically worse compared to flat 0-1
			values[i] = sr.nextDouble() * (upper - lower) + lower;
		}
	}

	public void move() {
		for (int i = 0; i < values.length; i++) {
			values[i] += speed[i];
			if (values[i] < problem.getLowerLimit()[i]) {
				values[i] = problem.getLowerLimit()[i];
				speed[i] *= -1;
			}
			if (values[i] > problem.getUpperLimit()[i]) {
				values[i] = problem.getUpperLimit()[i];
				speed[i] *= -1;
			}
		}

		eval = problem.evaluate(values);
		updateBestValue();
	}

	public void updateBestValue() {
		DominationStatus dom = this.compareTo(best);

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

	public void updateSpeed(Particle bestGlobal) {
		double[] globalValues = bestGlobal.getValues();

		double r1 = sr.nextDouble();
		double r2 = sr.nextDouble();
		// double c1 = 1.5 + (2 - 1.5) * sr.nextDouble(); // 2 - first constant
		// double c2 = 1.5 + (2 - 1.5) * sr.nextDouble();
		// double w = 0.1 + (0.5 - 0.1) * sr.nextDouble();

		// for (int i = 0; i < speed.length; i++) {
		// speed[i] = w * speed[i] + c1 * r1 * (best.getValues()[i] -
		// this.values[i]) + c2 * r2
		// * (bestGlobal[i] - this.values[i]);
		// }

		// seems fine, w has a HUGE impact on the results, < 3 & > 6 gives gibberish
		double w = (sr.nextDouble() / 5) + 0.35;
		
		for (int i = 0; i < speed.length; i++) {
			speed[i] = w * speed[i] + 2 * r1 * (best.getValues()[i] - this.values[i]) + 2 * r2
					* (globalValues[i] - this.values[i]);
		}
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
	public DominationStatus compareTo(Particle o) {
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");

		// number format string
		String num = "%.2f";

		for (int i = 0; i < eval.length - 1; i++) {
			sb.append(String.format(num + ", ", eval[i]));
		}

		sb.append(String.format(num + ")", eval[eval.length - 1]));

		return sb.toString();
	}

}
