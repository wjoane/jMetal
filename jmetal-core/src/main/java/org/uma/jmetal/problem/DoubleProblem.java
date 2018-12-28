package org.uma.jmetal.problem;

import org.uma.jmetal.solution.DoubleSolution;

import java.util.List;

/**
 * Interface representing continuous problems
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public interface DoubleProblem extends Problem<DoubleSolution> {
  List<Double> getLowerBounds() ; ;
  List<Double> getUpperBounds() ;
}
