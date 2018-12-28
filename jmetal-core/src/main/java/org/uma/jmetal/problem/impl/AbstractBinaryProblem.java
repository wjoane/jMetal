package org.uma.jmetal.problem.impl;

import org.uma.jmetal.problem.BinaryProblem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.impl.DefaultBinarySolution;

import java.util.List;

@SuppressWarnings("serial")
public abstract class AbstractBinaryProblem extends AbstractGenericProblem<BinarySolution>
  implements BinaryProblem {

  private int[] bitsPerVariable ;

  public void setBitsPerVariable(int[]bitsPerVariable) {
    this.bitsPerVariable = bitsPerVariable ;
  }

  @Override
  public int getNumberOfBits(int index) {
    return bitsPerVariable[index] ;
  }
  
  @Override
  public int getTotalNumberOfBits() {
  	int count = 0 ;
  	for (int i = 0; i < this.getNumberOfVariables(); i++) {
  		count += this.bitsPerVariable[i] ;
  	}
  	
  	return count ;
  }

  @Override
  public BinarySolution createSolution() {
    return new DefaultBinarySolution(getNumberOfVariables(), getNumberOfObjectives(), bitsPerVariable)  ;
  }
}
