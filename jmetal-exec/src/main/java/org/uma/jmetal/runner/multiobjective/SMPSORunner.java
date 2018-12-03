package org.uma.jmetal.runner.multiobjective;

import org.knowm.xchart.BitmapEncoder;
import org.uma.jmetal.algorithm.multiobjective.smpso.SMPSO;
import org.uma.jmetal.algorithm.multiobjective.smpso.SMPSOBuilder;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.algorithmobserver.EvaluationObserver;
import org.uma.jmetal.util.algorithmobserver.HypervolumeIndicatorChartObserver;
import org.uma.jmetal.util.algorithmobserver.RealTimeChartObserver;
import org.uma.jmetal.util.archive.BoundedArchive;
import org.uma.jmetal.util.archive.impl.CrowdingDistanceArchive;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.terminationcondition.TerminationCondition;
import org.uma.jmetal.util.terminationcondition.impl.TerminationByEvaluations;

import java.util.List;

/**
 * Class for configuring and running the SMPSO algorithm
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class SMPSORunner extends AbstractAlgorithmRunner {
  /**
   * @param args Command line arguments. The first (optional) argument specifies
   *             the problem to solve.
   * @throws org.uma.jmetal.util.JMetalException
   * @throws java.io.IOException
   * @throws SecurityException
   * Invoking command:
  java org.uma.jmetal.runner.multiobjective.SMPSORunner problemName [referenceFront]
   */
  public static void main(String[] args) throws Exception {
    DoubleProblem problem;
    SMPSO algorithm;
    MutationOperator<DoubleSolution> mutation;

    String referenceParetoFront = "" ;

    String problemName ;
    if (args.length == 1) {
      problemName = args[0];
    } else if (args.length == 2) {
      problemName = args[0] ;
      referenceParetoFront = args[1] ;
    } else {
      problemName = "org.uma.jmetal.problem.multiobjective.zdt.ZDT4";
      referenceParetoFront = "jmetal-problem/src/test/resources/pareto_fronts/ZDT1.pf" ;
    }

    problem = (DoubleProblem) ProblemUtils.<DoubleSolution>loadProblem(problemName);

    BoundedArchive<DoubleSolution> archive = new CrowdingDistanceArchive<DoubleSolution>(100) ;

    double mutationProbability = 1.0 / problem.getNumberOfVariables() ;
    double mutationDistributionIndex = 20.0 ;
    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex) ;

    //TerminationCondition terminationCondition = new TerminationByComputingTime(1000);
    TerminationCondition terminationCondition = new TerminationByEvaluations(25000) ;
    //TerminationCondition terminationCondition = new TerminationByKeyboard();
    //TerminationCondition terminationCondition = new TerminationByQualityIndicator<DoubleSolution>
    //  (referenceParetoFront, 0.99) ;

    algorithm = new SMPSOBuilder(problem,
            terminationCondition,
            archive)
        .setMutation(mutation)
        .setSwarmSize(100)
        .setSolutionListEvaluator(new SequentialSolutionListEvaluator<DoubleSolution>())
        .build();

    RealTimeChartObserver<DoubleSolution> realTimeChartObserver =
        new RealTimeChartObserver<DoubleSolution>(algorithm, "SMPSO", 80, referenceParetoFront) ;
    new EvaluationObserver(algorithm) ;
    new HypervolumeIndicatorChartObserver(algorithm, "Hypervolume", 80, referenceParetoFront) ;

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
        .execute();

    List<DoubleSolution> population = algorithm.getResult();
    long computingTime = algorithmRunner.getComputingTime();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

    realTimeChartObserver.getChart().saveChart("NSGAII." + problemName, BitmapEncoder.BitmapFormat.PNG);

    printFinalSolutionSet(population);
    if (!referenceParetoFront.equals("")) {
      printQualityIndicators(population, referenceParetoFront) ;
    }

    System.exit(0);
  }
}
