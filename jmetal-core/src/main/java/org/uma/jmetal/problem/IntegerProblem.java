package org.uma.jmetal.problem;

import org.uma.jmetal.solution.IntegerSolution;

import java.util.List;

/**
 * Interface representing integer problems
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public interface IntegerProblem extends Problem<IntegerSolution> {
  List<Integer> getLowerBounds() ;
  List<Integer> getUpperBounds() ;
}
