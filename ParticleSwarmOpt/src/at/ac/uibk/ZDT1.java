package at.ac.uibk;

//  ZDT1.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <juanjod@gmail.com>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

/**
 * Class representing problem ZDT1 2-12-2015. Modified to be used in the
 * Bio-inspired optimization lecture.
 */
public class ZDT1 implements ZDT {
	int numberOfVariables_;
	int numberOfObjectives_;
	int numberOfConstraints_;
	String problemName_;
	double[] upperLimit_;
	double[] lowerLimit_;

	/**
	 * Constructor. Creates a default instance of problem ZDT1 (30 decision
	 * variables)
	 */
	public ZDT1() {
		this(30); // 30 variables by default
	} // ZDT1

	/**
	 * Creates a new instance of problem ZDT1.
	 * 
	 * @param numberOfVariables
	 *            Number of variables.
	 */
	public ZDT1(Integer numberOfVariables) {
		numberOfVariables_ = numberOfVariables;
		numberOfObjectives_ = 2;
		numberOfConstraints_ = 0;
		problemName_ = "ZDT1";

		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];

		// Establishes upper and lower limits for the variables
		for (int var = 0; var < numberOfVariables_; var++) {
			lowerLimit_[var] = 0.0;
			upperLimit_[var] = 1.0;
		} // for
	} // ZDT1

	/**
	 * Evaluates a solution.
	 */
	public double[] evaluate(double[] solution) {
		if (solution == null || solution.length != numberOfVariables_)
			return null;

		double[] f = new double[numberOfObjectives_];
		f[0] = solution[0];
		double g = this.evalG(solution);
		double h = this.evalH(f[0], g);
		f[1] = h * g;

		return f;
	} // evaluate

	/**
	 * Returns the value of the ZDT1 function G.
	 */
	private double evalG(double[] solution) {
		double g = 0.0;
		for (int i = 1; i < solution.length; i++)
			g += solution[i];
		double constant = (9.0 / (numberOfVariables_ - 1));
		g = constant * g;
		g = g + 1.0;
		return g;
	} // evalG

	/**
	 * Returns the value of the ZDT1 function H.
	 * 
	 * @param f
	 *            First argument of the function H.
	 * @param g
	 *            Second argument of the function H.
	 */
	public double evalH(double f, double g) {
		double h = 0.0;
		h = 1.0 - java.lang.Math.sqrt(f / g);
		return h;
	} // evalH

	@Override
	public double[] getUpperLimit() {
		return upperLimit_;
	}

	@Override
	public double[] getLowerLimit() {
		return lowerLimit_;
	}

	@Override
	public int getNumberOfVariable() {
		return numberOfVariables_;
	}
} // ZDT1
