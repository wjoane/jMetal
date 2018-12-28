package org.uma.jmetal.problem.impl;

import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.solution.impl.DefaultIntegerSolution;

import java.util.List;

@SuppressWarnings("serial")
public abstract class AbstractIntegerProblem extends AbstractGenericProblem<IntegerSolution>
  implements IntegerProblem {

  protected List<Integer> lowerBounds;
  protected List<Integer> upperBounds;

  /* Getters */
	@Override
	public List<Integer> getUpperBounds() {
		return upperBounds ;
	}

	@Override
	public List<Integer> getLowerBounds() {
		return lowerBounds ;
	}

  /* Setters */
  protected void setLowerBounds(List<Integer> lowerBounds) {
    this.lowerBounds = lowerBounds;
  }

  protected void setUpperBounds(List<Integer> upperBounds) {
    this.upperBounds = upperBounds;
  }

  @Override
  public IntegerSolution createSolution() {
    return new DefaultIntegerSolution(getNumberOfVariables(), getNumberOfObjectives(), lowerBounds, upperBounds) ;
  }

}
