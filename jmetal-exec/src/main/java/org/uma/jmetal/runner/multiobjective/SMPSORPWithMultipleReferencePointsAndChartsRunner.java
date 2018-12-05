package org.uma.jmetal.runner.multiobjective;

import org.knowm.xchart.BitmapEncoder;
import org.uma.jmetal.algorithm.multiobjective.smpso.SMPSORP;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.algorithmobserver.impl.RealTimeChartObserver;
import org.uma.jmetal.util.archivewithreferencepoint.ArchiveWithReferencePoint;
import org.uma.jmetal.util.archivewithreferencepoint.impl.CrowdingDistanceArchiveWithReferencePoint;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.uma.jmetal.util.terminationcondition.TerminationCondition;
import org.uma.jmetal.util.terminationcondition.impl.TerminationByEvaluations;

public class SMPSORPWithMultipleReferencePointsAndChartsRunner {
  /**
   * Program to run the SMPSORP algorithm with three reference points and plotting a graph during the algorithm
   * execution. SMPSORP is described in "Extending the Speed-constrained Multi-Objective PSO (SMPSO) With Reference Point Based Preference
   * Articulation. Antonio J. Nebro, Juan J. Durillo, José García-Nieto, Cristóbal Barba-González,
   * Javier Del Ser, Carlos A. Coello Coello, Antonio Benítez-Hidalgo, José F. Aldana-Montes.
   * Parallel Problem Solving from Nature -- PPSN XV. Lecture Notes In Computer Science, Vol. 11101,
   * pp. 298-310. 2018".
   *
   * @author Antonio J. Nebro
   */
  public static void main(String[] args) throws JMetalException, IOException {
    DoubleProblem problem;
    SMPSORP algorithm;
    MutationOperator<DoubleSolution> mutation;
    String referenceParetoFront = "" ;

    String problemName ;
    if (args.length == 1) {
      problemName = args[0];
    } else if (args.length == 2) {
      problemName = args[0] ;
      referenceParetoFront = args[1] ;
    } else {
      problemName = "org.uma.jmetal.problem.multiobjective.zdt.ZDT4" ;
      referenceParetoFront = "jmetal-problem/src/test/resources/pareto_fronts/ZDT4.pf" ;
    }

    problem = (DoubleProblem) ProblemUtils.<DoubleSolution> loadProblem(problemName);

    List<List<Double>> referencePoints;
    referencePoints = new ArrayList<>();
    //referencePoints.add(Arrays.asList(0.6, 0.1)) ;
    referencePoints.add(Arrays.asList(0.2, 0.3)) ;
    referencePoints.add(Arrays.asList(0.8, 0.2)) ;

    double mutationProbability = 1.0 / problem.getNumberOfVariables() ;
    double mutationDistributionIndex = 20.0 ;
    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex) ;

    int swarmSize = 100 ;
    List<ArchiveWithReferencePoint<DoubleSolution>> archivesWithReferencePoints = new ArrayList<>();

    for (int i = 0 ; i < referencePoints.size(); i++) {
      archivesWithReferencePoints.add(
          new CrowdingDistanceArchiveWithReferencePoint<DoubleSolution>(
              swarmSize/referencePoints.size(), referencePoints.get(i))) ;
    }

    //TerminationCondition terminationCondition = new TerminationByComputingTime(1000);
    TerminationCondition terminationCondition = new TerminationByEvaluations(25000) ;
    //TerminationCondition terminationCondition = new TerminationByKeyboard();
    //TerminationCondition terminationCondition = new TerminationByQualityIndicator<DoubleSolution>
    //  (referenceParetoFront, 0.99) ;

    algorithm = new SMPSORP(problem,
            swarmSize,
            terminationCondition,
            archivesWithReferencePoints,
            referencePoints,
            mutation,
            0.1,
            0.0, 1.0,
            0.0, 1.0,
            2.5, 1.5,
            2.5, 1.5,
            0.1, 0.1,
            -1.0, -1.0,
            new SequentialSolutionListEvaluator<>() );


    RealTimeChartObserver observer = new RealTimeChartObserver(algorithm, "SMPSO/RP", 80, referenceParetoFront) ;
    observer.setReferencePointList(referencePoints);

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
            .execute() ;

    observer.getChart().saveChart("SMPSORP", BitmapEncoder.BitmapFormat.PNG);
    List<DoubleSolution> population = algorithm.getResult() ;
    long computingTime = algorithmRunner.getComputingTime() ;

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

    new SolutionListOutput(population)
            .setSeparator("\t")
            .setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
            .setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
            .print();

    for (int i = 0 ; i < archivesWithReferencePoints.size(); i++) {
      new SolutionListOutput(archivesWithReferencePoints.get(i).getSolutionList())
          .setSeparator("\t")
          .setVarFileOutputContext(new DefaultFileOutputContext("VAR" + i + ".tsv"))
          .setFunFileOutputContext(new DefaultFileOutputContext("FUN" + i + ".tsv"))
          .print();
    }

    System.exit(0);
  }
}
