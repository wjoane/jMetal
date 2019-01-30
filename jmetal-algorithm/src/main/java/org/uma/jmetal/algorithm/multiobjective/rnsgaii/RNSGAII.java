package org.uma.jmetal.algorithm.multiobjective.rnsgaii;

import org.uma.jmetal.algorithm.InteractiveAlgorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.BasicMeasure;
import org.uma.jmetal.measure.impl.CountingMeasure;
import org.uma.jmetal.measure.impl.DurationMeasure;
import org.uma.jmetal.measure.impl.SimpleMeasureManager;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.RankingAndPreferenceSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.DominanceComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.terminationcondition.TerminationCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
@SuppressWarnings("serial")
public class RNSGAII<S extends Solution<?>> extends NSGAII<S> implements
    InteractiveAlgorithm<S,List<S>>, Measurable {

  private List<Double> interestPoint;
  private double epsilon;

  /**
   * Constructor
   */
  public RNSGAII(Problem<S> problem, int populationSize,
                 int matingPoolSize, int offspringPopulationSize,
                 TerminationCondition terminationCondition,
                 CrossoverOperator<S> crossoverOperator, MutationOperator<S> mutationOperator,
                 SelectionOperator<List<S>, S> selectionOperator, SolutionListEvaluator<S> evaluator,
                 List<Double> interestPoint, double epsilon) {
    super(problem,populationSize, offspringPopulationSize, terminationCondition, crossoverOperator,
            mutationOperator,selectionOperator, new DominanceComparator<S>(), evaluator);
    this.interestPoint = interestPoint;
    this.epsilon = epsilon;
  }

  @Override
  public void updatePointOfInterest(List<Double> newReferencePoints){
    this.interestPoint = newReferencePoints;
  }

  @Override protected List<S> replacement(List<S> population, List<S> offspringPopulation) {
    List<S> jointPopulation = new ArrayList<>();
    jointPopulation.addAll(population);
    jointPopulation.addAll(offspringPopulation);

    RankingAndPreferenceSelection<S> rankingAndCrowdingSelection ;
    rankingAndCrowdingSelection = new RankingAndPreferenceSelection<S>(getMaxPopulationSize(), interestPoint, epsilon) ;

    return rankingAndCrowdingSelection.execute(jointPopulation) ;
  }

  @Override public String getName() {
    return "RNSGAII" ;
  }

  @Override public String getDescription() {
    return "Reference Point Based Nondominated Sorting Genetic Algorithm version II" ;
  }
}
