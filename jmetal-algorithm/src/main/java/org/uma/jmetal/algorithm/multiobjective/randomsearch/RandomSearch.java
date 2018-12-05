package org.uma.jmetal.algorithm.multiobjective.randomsearch;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.BasicMeasure;
import org.uma.jmetal.measure.impl.SimpleMeasureManager;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.archive.impl.NonDominatedSolutionListArchive;
import org.uma.jmetal.util.terminationcondition.TerminationCondition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements a simple random search algorithm.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
@SuppressWarnings("serial")
public class RandomSearch<S extends Solution<?>> implements Algorithm<List<S>>, Measurable {
  private Problem<S> problem;
  private Map<String, Object> algorithmStatusData;
  private TerminationCondition terminationCondition;
  private long initComputingTime;
  private int evaluations;

  protected SimpleMeasureManager measureManager ;
  protected BasicMeasure<Map<String, Object>> algorithmDataMeasure ;

  NonDominatedSolutionListArchive<S> nonDominatedArchive;

  /**
   * Constructor
   */
  public RandomSearch(Problem<S> problem, TerminationCondition terminationCondition) {
    this.problem = problem;
    this.terminationCondition = terminationCondition;
    nonDominatedArchive = new NonDominatedSolutionListArchive<S>();
    this.evaluations = 0;
    this.algorithmStatusData = new HashMap<>();

    algorithmDataMeasure = new BasicMeasure<>() ;
    measureManager = new SimpleMeasureManager() ;
    measureManager.setPushMeasure("ALGORITHM_DATA", algorithmDataMeasure);
  }

  /* Getter */
  public int getEvaluations() {
    return evaluations;
  }

  @Override
  public void run() {
    initComputingTime = System.currentTimeMillis();

    updateStatusData();

    S newSolution;
    while (!terminationCondition.check(algorithmStatusData)) {
      newSolution = problem.createSolution();
      problem.evaluate(newSolution);
      evaluations++;
      nonDominatedArchive.add(newSolution);

      updateStatusData();
    }
  }

  private void updateStatusData() {
    algorithmStatusData.put("EVALUATIONS", evaluations);
    algorithmStatusData.put("POPULATION", nonDominatedArchive.getSolutionList());
    algorithmStatusData.put("COMPUTING_TIME", System.currentTimeMillis() - initComputingTime);

    algorithmDataMeasure.push(algorithmStatusData);
  }

  @Override
  public List<S> getResult() {
    return nonDominatedArchive.getSolutionList();
  }

  @Override
  public String getName() {
    return "Random search";
  }

  @Override
  public String getDescription() {
    return "Multi-objective random search algorithm";
  }

  @Override
  public MeasureManager getMeasureManager() {
    return measureManager ;
  }
}
