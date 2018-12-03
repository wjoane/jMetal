package org.uma.jmetal.algorithm.multiobjective.moead.alternative;

import org.uma.jmetal.algorithm.impl.AbstractEvolutionaryAlgorithm;
import org.uma.jmetal.algorithm.multiobjective.moead.util.MOEADUtils;
import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.BasicMeasure;
import org.uma.jmetal.measure.impl.SimpleMeasureManager;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.selection.NaryRandomSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.aggregativefunction.AggregativeFunction;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.neighborhood.impl.WeightVectorNeighborhood;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.terminationcondition.TerminationCondition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Alternative implementation of MOEA/D. We have redesigned the code to allow MOEA/D to inherit from
 * the {@link AbstractEvolutionaryAlgorithm} class. The result is a more modular, reusable and
 * extensive code. Features:
 * 1.- Class {@link WeightVectorNeighborhood} is used for weight management
 * 2.- The aggregative function is based on the {@link AggregativeFunction} interface, and it is a parameter of the
 * algorithm.
 * 3.- MOEADAlt implements the {@link Measurable} interface, so it becomes an observable entity.
 * 4.- A map is used to provide external entities (observers) with information of the algorithm at the end of
 * each iteration
 */
public class MOEADAlt
    extends AbstractEvolutionaryAlgorithm<DoubleSolution, List<DoubleSolution>>
    implements Measurable {

  protected enum NeighborType {NEIGHBOR, POPULATION}

  private int evaluations;
  private int populationSize;
  private AggregativeFunction aggregativeFunction;
  private TerminationCondition terminationCondition ;

  private WeightVectorNeighborhood<DoubleSolution> weightVectorNeighborhood;

  private DifferentialEvolutionCrossover crossoverOperator;
  private SelectionOperator<List<DoubleSolution>, List<DoubleSolution>> selectionOperator;
  private MutationOperator<DoubleSolution> mutationOperator;

  private int neighborSize;
  private double neighborhoodSelectionProbability;
  private int maximumNumberOfReplacedSolutions;

  private int currentSubproblem;
  private NeighborType neighborType;

  private SolutionListEvaluator<DoubleSolution> evaluator;

  private Permutation permutation;

  private SimpleMeasureManager measureManager ;
  private BasicMeasure<Map<String, Object>> algorithmDataMeasure ;
  private Map<String, Object> algorithmStatusData ;
  private long initComputingTime ;

  public MOEADAlt(
      DoubleProblem problem,
      int populationSize,
      double neighborhoodSelectionProbability,
      int maximumNumberOfReplacedSolutions,
      int neighborSize,
      AggregativeFunction aggregativeFunction,
      TerminationCondition terminationCondition,
      DifferentialEvolutionCrossover differentialEvolutionCrossover,
      MutationOperator<DoubleSolution> mutationOperator) {
    this.problem = problem;
    this.populationSize = populationSize;
    this.aggregativeFunction = aggregativeFunction;
    this.terminationCondition = terminationCondition ;

    this.neighborhoodSelectionProbability = neighborhoodSelectionProbability;
    this.maximumNumberOfReplacedSolutions = maximumNumberOfReplacedSolutions;
    this.neighborSize = neighborSize ;

    crossoverOperator = differentialEvolutionCrossover ;
    selectionOperator = new NaryRandomSelection<>(2);
    this.mutationOperator = mutationOperator ;

    evaluator = new SequentialSolutionListEvaluator<>();

    weightVectorNeighborhood = new WeightVectorNeighborhood<DoubleSolution>(
        populationSize,
        this.neighborSize);

    permutation = new Permutation(populationSize);

    algorithmStatusData = new HashMap<String, Object>();
    algorithmDataMeasure = new BasicMeasure<>() ;
    measureManager = new SimpleMeasureManager() ;
    measureManager.setPushMeasure("ALGORITHM_DATA", algorithmDataMeasure);
  }

  @Override
  protected void initProgress() {
    evaluations += populationSize;
    for (DoubleSolution solution : population) {
      aggregativeFunction.update(solution.getObjectives());
    }

    updateStatusData();
    algorithmDataMeasure.push(algorithmStatusData);
  }

  @Override
  protected void updateProgress() {
    evaluations++;

    updateStatusData();
    algorithmDataMeasure.push(algorithmStatusData);
  }

  private void updateStatusData() {
    algorithmStatusData.put("EVALUATIONS", evaluations) ;
    algorithmStatusData.put("POPULATION", population) ;
    algorithmStatusData.put("COMPUTING_TIME", System.currentTimeMillis() - initComputingTime) ;
  }

  @Override
  protected boolean isStoppingConditionReached() {
    return terminationCondition.check(algorithmStatusData);
  }

  @Override
  protected List<DoubleSolution> createInitialPopulation() {
    initComputingTime = System.currentTimeMillis() ;
    List<DoubleSolution> population = new ArrayList<>();
    IntStream.range(0, populationSize)
        .forEach(i -> population.add(problem.createSolution()));

    return population;
  }

  @Override
  protected List<DoubleSolution> evaluatePopulation(List<DoubleSolution> population) {
    return evaluator.evaluate(population, getProblem());
  }

  @Override
  protected List<DoubleSolution> selection(List<DoubleSolution> population) {
    currentSubproblem = permutation.getNextElement();
    neighborType = chooseNeighborType();

    List<DoubleSolution> matingPool;
    if (neighborType.equals(NeighborType.NEIGHBOR)) {
      matingPool = selectionOperator
          .execute(weightVectorNeighborhood.getNeighbors(population, currentSubproblem));
    } else {
      matingPool = selectionOperator.execute(population);
    }

    matingPool.add(population.get(currentSubproblem));

    return matingPool;
  }

  @Override
  protected List<DoubleSolution> reproduction(List<DoubleSolution> matingPool) {
    crossoverOperator.setCurrentSolution(population.get(currentSubproblem));

    List<DoubleSolution> offspringPopulation = crossoverOperator.execute(matingPool);
    mutationOperator.execute(offspringPopulation.get(0));

    return offspringPopulation;
  }

  @Override
  protected List<DoubleSolution> replacement(List<DoubleSolution> population,
      List<DoubleSolution> offspringPopulation) {
    DoubleSolution newSolution = offspringPopulation.get(0);

    aggregativeFunction.update(newSolution.getObjectives());

    List<DoubleSolution> newPopulation;
    newPopulation = updateNeighborhood(
        newSolution, population, weightVectorNeighborhood.getNeighborhood()[currentSubproblem]);

    return newPopulation;
  }

  @Override
  public List<DoubleSolution> getResult() {
    return population;
  }

  @Override
  public String getName() {
    return "MOEA/D-DE";
  }

  @Override
  public String getDescription() {
    return "MOEA/D-DE";
  }

  protected NeighborType chooseNeighborType() {
    double rnd = JMetalRandom.getInstance().nextDouble();
    NeighborType neighborType;

    if (rnd < neighborhoodSelectionProbability) {
      neighborType = NeighborType.NEIGHBOR;
    } else {
      neighborType = NeighborType.POPULATION;
    }
    return neighborType;
  }

  protected List<DoubleSolution> updateNeighborhood(
      DoubleSolution newSolution,
      List<DoubleSolution> population,
      int[] neighborhood) {
    int size;
    if (neighborType == NeighborType.NEIGHBOR) {
      size = neighborhood.length;
    } else {
      size = populationSize;
    }

    int[] permutation = new int[size];
    MOEADUtils.randomPermutation(permutation, size);
    int count = 0;

    for (int i = 0; i < size; i++) {
      int k;
      if (neighborType == NeighborType.NEIGHBOR) {
        k = neighborhood[permutation[i]];
      } else {
        k = permutation[i];
      }

      double f1 = aggregativeFunction.compute(
          population.get(k).getObjectives(),
          weightVectorNeighborhood.getWeightVector()[k]);
      double f2 = aggregativeFunction.compute(
          newSolution.getObjectives(),
          weightVectorNeighborhood.getWeightVector()[k]);

      if (f2 < f1) {
        population.set(k, (DoubleSolution) newSolution.copy());
        count++;
      }

      if (count >= maximumNumberOfReplacedSolutions) {
        break;
      }
    }

    return population;
  }

  private static class Permutation {
    private int[] permutation;
    private int counter;

    public Permutation(int size) {
      permutation = new int[size];
      MOEADUtils.randomPermutation(permutation, size);
      counter = 0;
    }

    public int getNextElement() {
      int next = permutation[counter];
      counter++;
      if (counter == permutation.length) {
        MOEADUtils.randomPermutation(permutation, permutation.length);
        counter = 0;
      }

      return next;
    }
  }

  @Override
  public MeasureManager getMeasureManager() {
    return measureManager;
  }
}
