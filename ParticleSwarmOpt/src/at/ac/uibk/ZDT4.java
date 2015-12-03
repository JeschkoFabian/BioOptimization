package at.ac.uibk;
//  ZDT4.java
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
 * Class representing problem ZDT4
 * 2-12-2015. Modified to be used in the Bio-inspired optimization lecture. 
 */
public class ZDT4{
   int numberOfVariables_;
   int numberOfObjectives_;
   int numberOfConstraints_;
   String problemName_;
   double [] upperLimit_;
   double [] lowerLimit_;    
     
 /**
  * Constructor.
  * Creates a default instance of problem ZDT4 (10 decision variables)
  */
  public ZDT4() {
    this(10); // 10 variables by default
  } // ZDT4
  
 /** 
  * Creates a instance of problem ZDT4.
  * @param numberOfVariables Number of variables.
  */
  public ZDT4(Integer numberOfVariables) {
    numberOfVariables_  = numberOfVariables;
    numberOfObjectives_ = 2;
    numberOfConstraints_= 0;
    problemName_        = "ZDT4";

    upperLimit_ = new double[numberOfVariables_];
    lowerLimit_ = new double[numberOfVariables_];
        
    lowerLimit_[0] = 0.0;
    upperLimit_[0] = 1.0;
    for (int var = 1; var < numberOfVariables_; var++){
      lowerLimit_[var] = -5.0;
      upperLimit_[var] =  5.0;
    } //for        
  } //ZDT4

  /** 
  * Evaluates a solution 
  */    
  public double [] evaluate(double  [] solution){
    if (solution==null || solution.length != numberOfVariables_)
	return null;

    
    double [] f = new double[numberOfObjectives_] ; 
    f[0]        = solution[0]     ;
    double g    = this.evalG(solution)                 ;
    double h    = this.evalH(f[0],g)              ;
    f[1]        = h * g                           ;   
    
    return f;
  } //evaluate
    
  /**
  * Returns the value of the ZDT4 function G.

  */  
  public double evalG(double [] solution){
    double g = 0.0;
    for (int var = 1; var < numberOfVariables_; var++)
      g += Math.pow(solution[var],2.0) + 
          - 10.0 * Math.cos(4.0*Math.PI*solution[var]);
    
    double constante = 1.0 + 10.0*(numberOfVariables_ - 1);
    return g + constante;
  } // evalG
    
  /**
  * Returns the value of the ZDT4 function H.
  * @param f First argument of the function H.
  * @param g Second argument of the function H.
  */
  public double evalH(double f, double g){
    return 1.0 - Math.sqrt(f/g);
  } // evalH      
} // ZDT4
