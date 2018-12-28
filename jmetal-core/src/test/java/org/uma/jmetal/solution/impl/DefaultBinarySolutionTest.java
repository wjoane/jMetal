package org.uma.jmetal.solution.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.uma.jmetal.problem.BinaryProblem;
import org.uma.jmetal.problem.impl.AbstractBinaryProblem;
import org.uma.jmetal.solution.BinarySolution;

import static org.junit.Assert.assertEquals;

public class DefaultBinarySolutionTest {
  private static final int NUMBER_OF_BITS_OF_MOCKED_BINARY_PROBLEM = 5 ;


  @Test public void shouldTheSumOfGetNumberOfBitsBeEqualToTheSumOfBitsPerVariable() {
    int[] bitsPerVariable = new int[]{NUMBER_OF_BITS_OF_MOCKED_BINARY_PROBLEM, NUMBER_OF_BITS_OF_MOCKED_BINARY_PROBLEM} ;
    DefaultBinarySolution solution = new DefaultBinarySolution(bitsPerVariable.length, 2, bitsPerVariable) ;

    assertEquals(bitsPerVariable.length * NUMBER_OF_BITS_OF_MOCKED_BINARY_PROBLEM, solution.getTotalNumberOfBits());
  }

  @Test public void shouldGetNumberOfBitsBeEqualToTheNumberOfOfBitsPerVariable() {
    int[] bitsPerVariable = new int[]{NUMBER_OF_BITS_OF_MOCKED_BINARY_PROBLEM, NUMBER_OF_BITS_OF_MOCKED_BINARY_PROBLEM} ;
    DefaultBinarySolution solution = new DefaultBinarySolution(bitsPerVariable.length, 2, bitsPerVariable) ;

    for (int i = 0; i < solution.getNumberOfVariables(); i++) {
      assertEquals(solution.getVariableValue(i).getBinarySetLength(), solution.getNumberOfBits(i));
    }
  }

  @Test public void shouldCopyReturnAnIdenticalVariable() {
    int[] bitsPerVariable = new int[]{NUMBER_OF_BITS_OF_MOCKED_BINARY_PROBLEM, NUMBER_OF_BITS_OF_MOCKED_BINARY_PROBLEM} ;
    DefaultBinarySolution solution = new DefaultBinarySolution(bitsPerVariable.length, 2, bitsPerVariable) ;
    BinarySolution newSolution = solution.copy();

    assertEquals(solution, newSolution);
  }

  @Test public void shouldTheHashCodeOfTwoIdenticalSolutionsBeTheSame() {
    int[] bitsPerVariable = new int[]{NUMBER_OF_BITS_OF_MOCKED_BINARY_PROBLEM} ;

    BinarySolution solutionA = new DefaultBinarySolution(bitsPerVariable.length, 2, bitsPerVariable) ;
    BinarySolution solutionB = new DefaultBinarySolution(bitsPerVariable.length, 2, bitsPerVariable) ;

    solutionA.getVariableValue(0).set(0) ;
    solutionA.getVariableValue(0).clear(1) ;
    solutionA.getVariableValue(0).set(2) ;
    solutionA.getVariableValue(0).clear(3) ;
    solutionA.getVariableValue(0).set(4) ;

    solutionB.getVariableValue(0).set(0) ;
    solutionB.getVariableValue(0).clear(1) ;
    solutionB.getVariableValue(0).set(2) ;
    solutionB.getVariableValue(0).clear(3) ;
    solutionB.getVariableValue(0).set(4) ;

    assertEquals(solutionA.hashCode(), solutionB.hashCode());
  }

  @Test public void shouldGetVariableValueStringReturnARightStringRepresentation() throws Exception {
    int[] bitsPerVariable = new int[]{NUMBER_OF_BITS_OF_MOCKED_BINARY_PROBLEM} ;
    BinarySolution solution = new DefaultBinarySolution(bitsPerVariable.length, 2, bitsPerVariable) ;
    solution.getVariableValue(0).set(0, NUMBER_OF_BITS_OF_MOCKED_BINARY_PROBLEM) ;

    assertEquals("11111", solution.getVariableValueString(0)) ;

    solution.getVariableValue(0).clear(2) ;
    assertEquals("11011", solution.getVariableValueString(0)) ;
  }
}
