package org.uma.jmetal.solution.impl;

import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.util.HashMap;
import java.util.List;

/**
 * Defines an implementation of a double solution
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
@SuppressWarnings("serial")
public class DefaultDoubleSolution 
    extends AbstractGenericSolution<Double>
    implements DoubleSolution {

  protected List<Double> lowerBounds ;
  protected List<Double> upperBounds ;

  /** Constructor */
  public DefaultDoubleSolution(int numberOfVariables, int numberOfObjectives, List<Double> lowerBounds, List<Double> upperBounds) {
    super(numberOfVariables, numberOfObjectives) ;

    if (numberOfVariables != lowerBounds.size()) {
      throw new JMetalException("The number of lower bounds is not equal to the number of variables: " +
          lowerBounds.size() + " -> " +  numberOfVariables) ;
    } else if (numberOfVariables != upperBounds.size()) {
      throw new JMetalException("The number of upper bounds is not equal to the number of variables: " +
          upperBounds.size() + " -> " +  numberOfVariables) ;
    }

    this.lowerBounds = lowerBounds ;
    this.upperBounds = upperBounds ;

    for (int i = 0 ; i < numberOfVariables; i++) {
      Double value = JMetalRandom.getInstance().nextDouble(lowerBounds.get(i), upperBounds.get(i)) ;
      setVariableValue(i, value) ;
    }
  }

  /** Copy constructor */
  public DefaultDoubleSolution(DefaultDoubleSolution solution) {
    super(solution.getNumberOfVariables(), solution.getNumberOfObjectives()) ;

    for (int i = 0; i < solution.getNumberOfVariables(); i++) {
      setVariableValue(i, solution.getVariableValue(i));
    }

    for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
      setObjective(i, solution.getObjective(i)) ;
    }

    lowerBounds = solution.lowerBounds ;
    upperBounds = solution.upperBounds ;

    attributes = new HashMap<Object, Object>(solution.attributes) ;
  }

  @Override
  public Double getLowerBound(int index) {
    return this.lowerBounds.get(index) ;
  }

  @Override
  public Double getUpperBound(int index) {
    return this.upperBounds.get(index) ;
  }

  @Override
  public DefaultDoubleSolution copy() {
    return new DefaultDoubleSolution(this);
  }

  @Override
  public String getVariableValueString(int index) {
    return getVariableValue(index).toString() ;
  }
}
