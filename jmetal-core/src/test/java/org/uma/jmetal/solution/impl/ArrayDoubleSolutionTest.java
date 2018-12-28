package org.uma.jmetal.solution.impl;

import org.junit.Before;
import org.junit.Test;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class ArrayDoubleSolutionTest {
  private DoubleProblem problem ;

  @Before
  public void setup() {
    problem = new MockedDoubleProblem() ;
  }


  @Test
  public void shouldConstructorCreateAnObject() {
    DoubleSolution solution = problem.createSolution() ;

    assertNotNull(solution);
  }

  @Test
  public void shouldCopyConstructorCreateAnIdenticalSolution() {
    DoubleSolution solution = problem.createSolution() ;

    assertEquals(solution, solution.copy());
  }

  @Test
  public void shouldGetLowerBoundReturnTheRightValue() {
    DoubleSolution solution = problem.createSolution() ;

    assertEquals(problem.getLowerBounds().get(0), solution.getLowerBound(0));
    assertEquals(problem.getLowerBounds().get(1), solution.getLowerBound(1));
    assertEquals(problem.getLowerBounds().get(2), solution.getLowerBound(2));
  }

  @Test
  public void shouldGetUpperBoundReturnTheRightValue() {
    DoubleSolution solution = problem.createSolution() ;

    assertEquals(problem.getUpperBounds().get(0), solution.getUpperBound(0));
    assertEquals(problem.getUpperBounds().get(1), solution.getUpperBound(1));
    assertEquals(problem.getUpperBounds().get(2), solution.getUpperBound(2));
  }

  @SuppressWarnings("serial")
  private class MockedDoubleProblem extends AbstractDoubleProblem {
    List<Double> lowerLimit ;
    List<Double> upperLimit ;

    public MockedDoubleProblem() {
      setNumberOfVariables(3);
      setNumberOfObjectives(2);
      setNumberOfConstraints(0);

      lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
      upperLimit = new ArrayList<>(getNumberOfVariables()) ;

      lowerLimit.add(-4.0);
      lowerLimit.add(-3.0);
      lowerLimit.add(-2.0);
      upperLimit.add(4.0);
      upperLimit.add(5.0);
      upperLimit.add(6.0);

      setLowerLimit(lowerLimit);
      setUpperLimit(upperLimit);
    }

    @Override
    public void evaluate(DoubleSolution solution) {
    }

    @Override
    public DoubleSolution createSolution() {
      return new ArrayDoubleSolution(getNumberOfVariables(), getNumberOfObjectives(), lowerLimit, upperLimit)  ;
    }
  }
}