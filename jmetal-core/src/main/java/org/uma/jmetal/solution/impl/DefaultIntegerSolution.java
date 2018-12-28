package org.uma.jmetal.solution.impl;

import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.util.HashMap;
import java.util.List;

/**
 * Defines an implementation of an integer solution
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
@SuppressWarnings("serial")
public class DefaultIntegerSolution
    extends AbstractGenericSolution<Integer>
    implements IntegerSolution {

  List<Integer> lowerBounds ;
  List<Integer> upperBounds ;

  /** Constructor */
  public DefaultIntegerSolution(int numberOfVariables, int numberOfObjectives, List<Integer> lowerBounds, List<Integer>upperBounds) {
    super(numberOfVariables, numberOfObjectives) ;

    if (numberOfVariables != lowerBounds.size()) {
      throw new JMetalException("The number of lower bounds is not equal to the number of objectives: " +
          lowerBounds.size() + " -> " +  numberOfObjectives) ;
    } else if (numberOfVariables != upperBounds.size()) {
      throw new JMetalException("The number of upper bounds is not equal to the number of objectives: " +
          upperBounds.size() + " -> " +  numberOfObjectives) ;
    }

    this.lowerBounds = lowerBounds ;
    this.upperBounds = upperBounds ;

    for (int i = 0 ; i < numberOfVariables; i++) {
      int value = JMetalRandom.getInstance().nextInt(lowerBounds.get(i), upperBounds.get(i)) ;
      setVariableValue(i, value) ;
    }
  }

  /** Copy constructor */
  public DefaultIntegerSolution(DefaultIntegerSolution solution) {
    super(solution.getNumberOfVariables(), solution.getNumberOfObjectives()) ;

    for (int i = 0; i < solution.getNumberOfVariables(); i++) {
      setVariableValue(i, solution.getVariableValue(i));
    }

    for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
      setObjective(i, solution.getObjective(i)) ;
    }

    attributes = new HashMap<Object, Object>(solution.attributes) ;
  }

  @Override
  public Integer getLowerBound(int index) {
    return this.lowerBounds.get(index) ;
  }

  @Override
  public Integer getUpperBound(int index) {
    return this.upperBounds.get(index) ;
  }


  @Override
  public DefaultIntegerSolution copy() {
    return new DefaultIntegerSolution(this);
  }

  @Override
  public String getVariableValueString(int index) {
    return getVariableValue(index).toString() ;
  }
}
