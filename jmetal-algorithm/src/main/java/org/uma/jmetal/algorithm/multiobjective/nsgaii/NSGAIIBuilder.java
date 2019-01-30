package org.uma.jmetal.algorithm.multiobjective.nsgaii;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.DominanceComparator;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.terminationcondition.TerminationCondition;
import org.uma.jmetal.util.terminationcondition.impl.TerminationByEvaluations;

import java.util.Comparator;
import java.util.List;

/**
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class NSGAIIBuilder<S extends Solution<?>> implements AlgorithmBuilder<NSGAII<S>> {
  public enum NSGAIIVariant {NSGAII, SteadyStateNSGAII, Measures, NSGAII45}

  /**
   * NSGAIIBuilder class
   */
  private final Problem<S> problem;
  private TerminationCondition terminationCondition ;
  private int populationSize;
  protected int matingPoolSize;
  protected int offspringPopulationSize ;

  private CrossoverOperator<S>  crossoverOperator;
  private MutationOperator<S> mutationOperator;
  private SelectionOperator<List<S>, S> selectionOperator;
  private SolutionListEvaluator<S> evaluator;
  private Comparator<S> dominanceComparator ;

  private NSGAIIVariant variant;

  /**
   * NSGAIIBuilder constructor
   */
  public NSGAIIBuilder(Problem<S> problem,
                       int populationSize,
                       TerminationCondition terminationCondition,
                       CrossoverOperator<S> crossoverOperator,
                       MutationOperator<S> mutationOperator) {
    this.problem = problem;
    this.terminationCondition = terminationCondition ;
    this.populationSize = populationSize;
    offspringPopulationSize = populationSize ;
    this.crossoverOperator = crossoverOperator ;
    this.mutationOperator = mutationOperator ;
    selectionOperator = new BinaryTournamentSelection<S>(new RankingAndCrowdingDistanceComparator<S>()) ;
    evaluator = new SequentialSolutionListEvaluator<S>();
    dominanceComparator = new DominanceComparator<>()  ;

    this.variant = NSGAIIVariant.NSGAII ;
  }


  public NSGAIIBuilder<S> setOffspringPopulationSize(int offspringPopulationSize) {
    if (offspringPopulationSize < 0) {
      throw new JMetalException("Offspring population size is negative: " + populationSize);
    }

    this.offspringPopulationSize = offspringPopulationSize;

    return this;
  }

  public NSGAIIBuilder<S> setSelectionOperator(SelectionOperator<List<S>, S> selectionOperator) {
    if (selectionOperator == null) {
      throw new JMetalException("selectionOperator is null");
    }
    this.selectionOperator = selectionOperator;

    return this;
  }

  public NSGAIIBuilder<S> setSolutionListEvaluator(SolutionListEvaluator<S> evaluator) {
    if (evaluator == null) {
      throw new JMetalException("evaluator is null");
    }
    this.evaluator = evaluator;

    return this;
  }

  public NSGAIIBuilder<S> setDominanceComparator(Comparator<S> dominanceComparator) {
    if (dominanceComparator == null) {
      throw new JMetalException("dominanceComparator is null");
    }
    this.dominanceComparator = dominanceComparator ;

    return this;
  }


  public NSGAIIBuilder<S> setVariant(NSGAIIVariant variant) {
    this.variant = variant;

    return this;
  }

  public NSGAII<S> build() {
    NSGAII<S> algorithm = null ;
    if (variant.equals(NSGAIIVariant.NSGAII)) {
      algorithm = new NSGAII<S>(problem, populationSize, offspringPopulationSize,
              terminationCondition, crossoverOperator, mutationOperator, selectionOperator, dominanceComparator, evaluator);
    } else if (variant.equals(NSGAIIVariant.Measures)) {
      algorithm = new NSGAIIMeasures<S>(problem, populationSize, offspringPopulationSize, terminationCondition,
              crossoverOperator, mutationOperator, selectionOperator, dominanceComparator, evaluator);
    }

    return algorithm ;
  }

  /* Getters */
  public Problem<S> getProblem() {
    return problem;
  }

  public int getPopulationSize() {
    return populationSize;
  }

  public CrossoverOperator<S> getCrossoverOperator() {
    return crossoverOperator;
  }

  public MutationOperator<S> getMutationOperator() {
    return mutationOperator;
  }

  public SelectionOperator<List<S>, S> getSelectionOperator() {
    return selectionOperator;
  }

  public SolutionListEvaluator<S> getSolutionListEvaluator() {
    return evaluator;
  }
}
