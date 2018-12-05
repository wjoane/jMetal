package org.uma.jmetal.runner.multiobjective;

import org.uma.jmetal.algorithm.multiobjective.randomsearch.RandomSearch;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.*;
import org.uma.jmetal.util.algorithmobserver.impl.EvaluationObserver;
import org.uma.jmetal.util.algorithmobserver.impl.RealTimeChartObserver;
import org.uma.jmetal.util.terminationcondition.TerminationCondition;
import org.uma.jmetal.util.terminationcondition.impl.TerminationByComputingTime;
import org.uma.jmetal.util.terminationcondition.impl.TerminationByEvaluations;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Class for configuring and running the random search algorithm
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */

public class RandomSearchRunner extends AbstractAlgorithmRunner {
  /**
   * @param args Command line arguments.
   * @throws SecurityException
   * Invoking command:
  java org.uma.jmetal.runner.multiobjective.RandomSearchRunner problemName [referenceFront]
   */
  public static void main(String[] args) throws JMetalException, FileNotFoundException {
    Problem<DoubleSolution> problem;
    RandomSearch<DoubleSolution> algorithm;

    String referenceParetoFront = "" ;

    String problemName ;
    if (args.length == 1) {
      problemName = args[0];
    } else if (args.length == 2) {
      problemName = args[0] ;
      referenceParetoFront = args[1] ;
    } else {
      problemName = "org.uma.jmetal.problem.multiobjective.zdt.ZDT1";
      referenceParetoFront = "jmetal-problem/src/test/resources/pareto_fronts/ZDT1.pf" ;
    }

    problem = ProblemUtils.loadProblem(problemName);

    TerminationCondition terminationCondition =
            //new TerminationByEvaluations(100000) ;
            new TerminationByComputingTime(10000) ;

    algorithm = new RandomSearch<>(problem, terminationCondition) ;

    new RealTimeChartObserver<DoubleSolution>(algorithm, "Random Search", 80, referenceParetoFront) ;
    new EvaluationObserver(algorithm) ;

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
            .execute() ;

    List<DoubleSolution> population = algorithm.getResult() ;
    long computingTime = algorithmRunner.getComputingTime() ;

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
    JMetalLogger.logger.info("Evaluations: " + (int)algorithm.getEvaluations());

    printFinalSolutionSet(population);
    if (!referenceParetoFront.equals("")) {
      printQualityIndicators(population, referenceParetoFront) ;
    }
  }
}
