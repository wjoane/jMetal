package org.uma.jmetal.lab.mogram;

import java.util.List;

import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.component.termination.Termination;
import org.uma.jmetal.component.termination.impl.TerminationByEvaluations;
import org.uma.jmetal.operator.crossover.CrossoverOperator;
import org.uma.jmetal.operator.crossover.impl.SBXCrossover;
import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.operator.mutation.impl.PolynomialMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.ProblemUtils;

public class MoGramExample extends AbstractAlgorithmRunner {

    public static void main(String[] args) {
        Problem<DoubleSolution> problem;
        NSGAII<DoubleSolution> algorithm;
        CrossoverOperator<DoubleSolution> crossover;
        MutationOperator<DoubleSolution> mutation;

        String problemName = "org.uma.jmetal.problem.multiobjective.zdt.ZDT1";

        problem = ProblemUtils.<DoubleSolution>loadProblem(problemName);

        double crossoverProbability = 0.9;
        double crossoverDistributionIndex = 20.0;
        crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);

        double mutationProbability = 1.0 / problem.getNumberOfVariables();
        double mutationDistributionIndex = 20.0;
        mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

        int populationSize = 100;
        int offspringPopulationSize = populationSize;

        Termination termination = new TerminationByEvaluations(10000);

        algorithm = new NSGAII<>(problem, populationSize, offspringPopulationSize, crossover, mutation, termination);

        algorithm.run();

        List<DoubleSolution> population = algorithm.getResult();
        JMetalLogger.logger.info("Total execution time : " + algorithm.getTotalComputingTime() + "ms");
        JMetalLogger.logger.info("Number of evaluations: " + algorithm.getEvaluations());

        String exportCSVLocation = "./mogram.csv";
        MoGram<DoubleSolution> moGram = new EuclideanMoGram(population);
        moGram.exportSVG(exportCSVLocation);
    }

}
