package org.uma.jmetal.runner.multiobjective;

import org.uma.jmetal.algorithm.multiobjective.moead.alternative.MOEADAlt;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.aggregativefunction.AggregativeFunction;
import org.uma.jmetal.util.aggregativefunction.impl.Tschebyscheff;
import org.uma.jmetal.util.algorithmobserver.impl.EvaluationObserver;
import org.uma.jmetal.util.algorithmobserver.impl.HypervolumeIndicatorChartObserver;
import org.uma.jmetal.util.algorithmobserver.impl.RealTimeChartObserver;
import org.uma.jmetal.util.terminationcondition.TerminationCondition;
import org.uma.jmetal.util.terminationcondition.impl.TerminationByEvaluations;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Class for configuring and running the MOEA/D algorithm (version {@link MOEADAlt}
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class MOEADAltRunner extends AbstractAlgorithmRunner {

  /**
   * @param args Command line arguments.
   * @throws SecurityException Invoking command: java org.uma.jmetal.runner.multiobjective.MOEADRunner
   * problemName [referenceFront]
   */
  public static void main(String[] args) throws FileNotFoundException {
    DoubleProblem problem;
    MOEADAlt algorithm;

    String problemName;
    String referenceParetoFront = "";
    if (args.length == 1) {
      problemName = args[0];
    } else if (args.length == 2) {
      problemName = args[0];
      referenceParetoFront = args[1];
    } else {
      problemName = "org.uma.jmetal.problem.multiobjective.lz09.LZ09F2";
      referenceParetoFront = "jmetal-problem/src/test/resources/pareto_fronts/LZ09_F2.pf";
    }

    problem = (DoubleProblem) ProblemUtils.<DoubleSolution>loadProblem(problemName);

    int populationSize = 300;
    int neighborSize = 20;
    double neighborhoodSelectionProbability = 0.9;
    int maximumNumberOfReplacedSolutions = 2;
    AggregativeFunction aggregativeFunction = new Tschebyscheff() ;

    //TerminationCondition terminationCondition = new TerminationByComputingTime(1000);
    TerminationCondition terminationCondition = new TerminationByEvaluations(175000) ;
    //TerminationCondition terminationCondition = new TerminationByKeyboard();
    //TerminationCondition terminationCondition = new TerminationByQualityIndicator<DoubleSolution>
      //  ("jmetal-problem/src/test/resources/pareto_fronts/LZ09_F2.pf", 0.99) ;

    algorithm = new MOEADAlt(problem,
        populationSize,
        neighborhoodSelectionProbability,
        maximumNumberOfReplacedSolutions,
        neighborSize,
        aggregativeFunction,
        terminationCondition,
        new DifferentialEvolutionCrossover(1.0, 0.5, "rand/1/bin"),
        new PolynomialMutation(1.0 / problem.getNumberOfVariables(), 20.0));

    new RealTimeChartObserver(algorithm, "MOEA/D", 1,"jmetal-problem/src/test/resources/pareto_fronts/LZ09_F2.pf") ;
    new EvaluationObserver(algorithm) ;
    new HypervolumeIndicatorChartObserver(algorithm, "Hypervolume", 80, referenceParetoFront) ;

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
        .execute();

    List<DoubleSolution> population = algorithm.getResult();
    long computingTime = algorithmRunner.getComputingTime();

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

    printFinalSolutionSet(population);
    if (!referenceParetoFront.equals("")) {
      printQualityIndicators(population, referenceParetoFront);
    }
  }
}
