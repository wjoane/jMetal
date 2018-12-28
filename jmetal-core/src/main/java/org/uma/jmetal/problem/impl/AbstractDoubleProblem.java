package org.uma.jmetal.problem.impl;

import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.impl.DefaultDoubleSolution;

import java.util.List;

@SuppressWarnings("serial")
public abstract class AbstractDoubleProblem extends AbstractGenericProblem<DoubleSolution>
  implements DoubleProblem {

  protected List<Double> lowerBounds ;
  protected List<Double> upperBounds ;

  /* Getters */
	@Override
	public List<Double> getUpperBounds() {
		return upperBounds ;
	}

	@Override
	public List<Double> getLowerBounds() {
		return lowerBounds ;
	}

  /* Setters */
  protected void setLowerLimit(List<Double> lowerLimit) {
    this.lowerBounds = lowerLimit;
  }

  protected void setUpperLimit(List<Double> upperLimit) {
    this.upperBounds = upperLimit;
  }

  @Override
  public DoubleSolution createSolution() {
    return new DefaultDoubleSolution(this.getNumberOfVariables(), this.getNumberOfObjectives(),
        this.getLowerBounds(), this.getUpperBounds())  ;
  }
}
