package org.uma.jmetal.operator.impl.selection;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.test.util.ReflectionTestUtils;
import org.uma.jmetal.operator.selection.impl.RankingAndDensityEstimatorSelection;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;

/**
 * @author Antonio J. Nebro
 * @version 1.0
 */
public class RankingAndDensityEstimatorSelectionTest {
  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Test
  public void shouldExecuteRaiseAnExceptionIfTheSolutionListIsNull() {
    exception.expect(JMetalException.class);
    exception.expectMessage(containsString("The solution list is null"));

    RankingAndDensityEstimatorSelection<Solution<?>> selection = new RankingAndDensityEstimatorSelection<Solution<?>>(4) ;
    selection.execute(null) ;
  }

  @Test
  public void shouldExecuteRaiseAnExceptionIfTheSolutionListIsEmpty() {
    exception.expect(JMetalException.class);
    exception.expectMessage(containsString("The solution list is empty"));

    RankingAndDensityEstimatorSelection<DoubleSolution> selection = new RankingAndDensityEstimatorSelection<DoubleSolution>(4) ;
    List<DoubleSolution> list = new ArrayList<>() ;

    selection.execute(list) ;
  }

  @Test
  public void shouldDefaultConstructorReturnASingleSolution() {
    RankingAndDensityEstimatorSelection<Solution<?>> selection = new RankingAndDensityEstimatorSelection<Solution<?>>(1) ;

    int result = (int) ReflectionTestUtils.getField(selection, "solutionsToSelect");
    int expectedResult = 1 ;
    assertEquals(expectedResult, result) ;
  }

  @Test
  public void shouldNonDefaultConstructorReturnTheCorrectNumberOfSolutions() {
    int solutionsToSelect = 4 ;
    RankingAndDensityEstimatorSelection<Solution<?>> selection = new RankingAndDensityEstimatorSelection<Solution<?>>(solutionsToSelect) ;

    int result = (int)ReflectionTestUtils.getField(selection, "solutionsToSelect");
    assertEquals(solutionsToSelect, result) ;
  }
}
