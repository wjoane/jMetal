package org.uma.jmetal.problem.singleobjective;


import org.apache.commons.text.similarity.LevenshteinDistance;
import org.uma.jmetal.problem.impl.AbstractIntegerPermutationProblem;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.util.JMetalException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a single-objective permutation problem: find an ordered permutation
 */
@SuppressWarnings("serial")
public class OrderedPermutation extends AbstractIntegerPermutationProblem {
  private int permutationLength ;
  private String permutationString ;
  private LevenshteinDistance levenshteinDistance ;

  /**
   * Creates a new TSP problem instance
   */
  public OrderedPermutation(int permutationLength) {
    this.permutationLength = permutationLength ;
    setNumberOfVariables(permutationLength);
    setNumberOfObjectives(1);
    setName("Ordered Permutation");

    List<Integer> optimumList = new ArrayList<>() ;
    for (int i = 0 ; i < permutationLength; i++) {
      optimumList.add(i) ;
    }

    permutationString = optimumList.toString() ;
    System.out.println(permutationString) ;

    levenshteinDistance = new LevenshteinDistance() ;
  }

  /** Evaluate() method */
  public void evaluate(PermutationSolution<Integer> solution){
    String str = solution.getVariables().toString() ;

    solution.setObjective(0, levenshteinDistance.apply(permutationString, str));
  }

  @Override public int getPermutationLength() {
    return permutationLength ;
  }
}
