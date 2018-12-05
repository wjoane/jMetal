package org.uma.jmetal.runner.multiobjective;

import java.io.IOException;
import org.knowm.xchart.BitmapEncoder;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.*;
import org.uma.jmetal.util.algorithmobserver.impl.EvaluationObserver;
import org.uma.jmetal.util.algorithmobserver.impl.HypervolumeIndicatorChartObserver;
import org.uma.jmetal.util.algorithmobserver.impl.RealTimeChartObserver;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.terminationcondition.TerminationCondition;
import org.uma.jmetal.util.terminationcondition.impl.TerminationByEvaluations;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Class to configure and run the NSGA-II algorithm
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class NSGAIIRunner extends AbstractAlgorithmRunner {
  /**
   * @param args Command line arguments.
   * @throws JMetalException
   * @throws FileNotFoundException Invoking command:
   *                               java org.uma.jmetal.runner.multiobjective.NSGAIIRunner problemName [referenceFront]
   */
  public static void main(String[] args) throws JMetalException, IOException {
    Problem<DoubleSolution> problem;
    NSGAII<DoubleSolution> algorithm;
    CrossoverOperator<DoubleSolution> crossover;
    MutationOperator<DoubleSolution> mutation;
    SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;
    String referenceParetoFront = "";

    String problemName;
    if (args.length == 1) {
      problemName = args[0];
    } else if (args.length == 2) {
      problemName = args[0];
      referenceParetoFront = args[1];
    } else {
      problemName = "org.uma.jmetal.problem.multiobjective.zdt.ZDT1";
      referenceParetoFront = "jmetal-problem/src/test/resources/pareto_fronts/ZDT1.pf";
    }

    problem = (DoubleProblem) ProblemUtils.<DoubleSolution>loadProblem(problemName);

    double crossoverProbability = 0.9;
    double crossoverDistributionIndex = 20.0;
    crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);

    double mutationProbability = 1.0 / problem.getNumberOfVariables();
    double mutationDistributionIndex = 20.0;
    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

    selection = new BinaryTournamentSelection<DoubleSolution>(
            new RankingAndCrowdingDistanceComparator<DoubleSolution>());

    //TerminationCondition terminationCondition = new TerminationByComputingTime(1000);
    TerminationCondition terminationCondition = new TerminationByEvaluations(25000) ;
    //TerminationCondition terminationCondition = new TerminationByKeyboard();
    //TerminationCondition terminationCondition = new TerminationByQualityIndicator<DoubleSolution>
    //  (referenceParetoFront, 0.99) ;

    int populationSize = 100 ;
    algorithm = new NSGAIIBuilder<DoubleSolution>(problem, populationSize, terminationCondition, crossover, mutation)
            .setSelectionOperator(selection)
            .build();

    RealTimeChartObserver<DoubleSolution> realTimeChartObserver =
        new RealTimeChartObserver<DoubleSolution>(algorithm, "NSGA-II", 80, referenceParetoFront) ;
    new EvaluationObserver(algorithm) ;
    new HypervolumeIndicatorChartObserver(algorithm, "Hypervolume", 80, referenceParetoFront) ;

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
            .execute();

    List<DoubleSolution> population = algorithm.getResult();
    long computingTime = algorithmRunner.getComputingTime();

    realTimeChartObserver.getChart().saveChart("NSGAII." + problemName, BitmapEncoder.BitmapFormat.PNG);

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

    printFinalSolutionSet(population);
    if (!referenceParetoFront.equals("")) {
      printQualityIndicators(population, referenceParetoFront);
    }
    System.exit(0) ;
  }
}
