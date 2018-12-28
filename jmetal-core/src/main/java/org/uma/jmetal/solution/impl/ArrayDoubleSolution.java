package org.uma.jmetal.solution.impl;

import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.util.*;

/**
 * Implementation of {@link DoubleSolution} using arrays.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
@SuppressWarnings("serial")
public class ArrayDoubleSolution implements DoubleSolution {
  private double[] objectives;
  private double[] variables;
  private Map<Object, Object> attributes ;

  private List<Double> lowerBounds ;
  private List<Double> upperBounds ;

  /**
   * Constructor
   */
  public ArrayDoubleSolution(int numberOfVariables, int numberOfObjectives, List<Double> lowerBounds, List<Double> upperBounds) {
    attributes = new HashMap<>() ;

    objectives = new double[numberOfObjectives] ;
    variables = new double[numberOfVariables] ;

    this.lowerBounds = lowerBounds ;
    this.upperBounds = upperBounds ;

    for (int i = 0; i < numberOfVariables; i++) {
      variables[i] = JMetalRandom.getInstance().nextDouble(lowerBounds.get(i), upperBounds.get(i)) ;
    }
  }

  /**
   * Copy constructor
   * @param solution to copy
   */
  public ArrayDoubleSolution(ArrayDoubleSolution solution) {
    this(solution.getNumberOfVariables(), solution.getNumberOfObjectives(), solution.lowerBounds, solution.upperBounds) ;

    for (int i = 0; i < solution.getNumberOfVariables(); i++) {
      variables[i] = solution.getVariableValue(i) ;
    }

    for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
      objectives[i] = solution.getObjective(i) ;
    }

    attributes = new HashMap<Object, Object>(solution.attributes) ;
  }

  @Override
  public void setObjective(int index, double value) {
    objectives[index] = value ;
  }

  @Override
  public double getObjective(int index) {
    return objectives[index];
  }

  @Override
  public List<Double> getVariables() {
    List<Double> vars = new ArrayList<>(getNumberOfVariables()) ;
    for (int i = 0 ; i < getNumberOfVariables(); i++) {
      vars.add(variables[i]) ;
    }
    return vars ;
  }

  @Override
  public double[] getObjectives() {
    return objectives ;
  }

  @Override
  public Double getVariableValue(int index) {
    return variables[index];
  }

  @Override
  public void setVariableValue(int index, Double value) {
    variables[index] = value ;
  }

  @Override
  public String getVariableValueString(int index) {
    return getVariableValue(index).toString() ;
  }

  @Override
  public int getNumberOfVariables() {
    return variables.length;
  }

  @Override
  public int getNumberOfObjectives() {
    return objectives.length;
  }

  @Override
  public Solution<Double> copy() {
    return new ArrayDoubleSolution(this);
  }

  @Override
  public void setAttribute(Object id, Object value) {
    attributes.put(id, value) ;
  }

  @Override
  public Object getAttribute(Object id) {
    return attributes.get(id) ;
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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ArrayDoubleSolution that = (ArrayDoubleSolution) o;
    return Arrays.equals(variables, that.variables);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(variables);
  }
}
