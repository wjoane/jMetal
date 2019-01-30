package org.uma.jmetal.algorithm.multiobjective.nsgaii;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.terminationcondition.impl.TerminationByComputingTime;

import java.util.Comparator;
import java.util.List;

/**
 * This class shows a version of NSGA-II having a stopping condition depending on run-time
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
@SuppressWarnings("serial")
public class NSGAIIStoppingByTime<S extends Solution<?>> extends NSGAII<S> {
  /**
   * Constructor
   */
  public NSGAIIStoppingByTime(Problem<S> problem, int populationSize,
                              int maxComputingTime, int offspringPopulationSize,
                              CrossoverOperator<S> crossoverOperator, MutationOperator<S> mutationOperator,
                              SelectionOperator<List<S>, S> selectionOperator, Comparator<S> dominanceComparator,
                              SolutionListEvaluator<S> evaluator) {
    super(problem, populationSize, offspringPopulationSize,
            new TerminationByComputingTime(maxComputingTime) ,
            crossoverOperator, mutationOperator,
        selectionOperator, dominanceComparator, evaluator);
  }
}
