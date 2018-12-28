package org.uma.jmetal.operator.impl.mutation;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.uma.jmetal.problem.BinaryProblem;
import org.uma.jmetal.problem.impl.AbstractBinaryProblem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.impl.DefaultBinarySolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;
import org.uma.jmetal.util.pseudorandom.impl.AuditableRandomGenerator;

import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BitFlipMutationTest {
  private static final double EPSILON = 0.00000000000001 ;
  private static final int NUMBER_OF_BITS_OF_MOCKED_BINARY_PROBLEM = 4 ;

  @Test
  public void shouldConstructorAssignTheCorrectProbabilityValue() {
    double mutationProbability = 0.1 ;
    BitFlipMutation mutation = new BitFlipMutation(mutationProbability) ;
    assertEquals(mutationProbability, (Double) ReflectionTestUtils
        .getField(mutation, "mutationProbability"), EPSILON) ;
  }

  @Test (expected = JMetalException.class)
  public void shouldConstructorFailWhenPassedANegativeProbabilityValue() {
    double mutationProbability = -0.1 ;
    new BitFlipMutation(mutationProbability) ;
  }

  @Test
  public void shouldGetMutationProbabilityReturnTheRightValue() {
    double mutationProbability = 0.1 ;
    BitFlipMutation mutation = new BitFlipMutation(mutationProbability) ;
    assertEquals(mutationProbability, mutation.getMutationProbability(), EPSILON) ;
  }

  @Test (expected = JMetalException.class)
  public void shouldExecuteWithNullParameterThrowAnException() {
    BitFlipMutation mutation = new BitFlipMutation(0.1) ;

    mutation.execute(null) ;
  }

  @Test
  public void shouldMutateASingleVariableSolutionReturnTheSameSolutionIfNoBitsAreMutated() {
    @SuppressWarnings("unchecked")
	RandomGenerator<Double> randomGenerator = mock(RandomGenerator.class) ;
    double mutationProbability = 0.01;

    Mockito.when(randomGenerator.getRandomValue()).thenReturn(0.02, 0.02, 0.02, 0.02) ;

    BitFlipMutation mutation = new BitFlipMutation(mutationProbability) ;
    int[] bitsPerVariable = new int[]{NUMBER_OF_BITS_OF_MOCKED_BINARY_PROBLEM} ;
    BinarySolution solution = new DefaultBinarySolution(bitsPerVariable.length, 2, bitsPerVariable) ;
    BinarySolution oldSolution = (BinarySolution)solution.copy() ;

    ReflectionTestUtils.setField(mutation, "randomGenerator", randomGenerator);

    mutation.execute(solution) ;

    assertEquals(oldSolution, solution) ;
    verify(randomGenerator, times(4)).getRandomValue();
  }

  @Test
  public void shouldMutateASingleVariableSolutionWhenASingleBitIsMutated() {
    @SuppressWarnings("unchecked")
	RandomGenerator<Double> randomGenerator = mock(RandomGenerator.class) ;
    double mutationProbability = 0.01;

    Mockito.when(randomGenerator.getRandomValue()).thenReturn(0.02, 0.0, 0.02, 0.02) ;

    BitFlipMutation mutation = new BitFlipMutation(mutationProbability) ;
    int[] bitsPerVariable = new int[]{NUMBER_OF_BITS_OF_MOCKED_BINARY_PROBLEM} ;
    BinarySolution solution = new DefaultBinarySolution(bitsPerVariable.length, 2, bitsPerVariable) ;
    BinarySolution oldSolution = (BinarySolution)solution.copy() ;

    ReflectionTestUtils.setField(mutation, "randomGenerator", randomGenerator);

    mutation.execute(solution) ;

    assertNotEquals(oldSolution.getVariableValue(0).get(1), solution.getVariableValue(0).get(1)) ;
    verify(randomGenerator, times(4)).getRandomValue();
  }

  @Test
  public void shouldMutateATwoVariableSolutionReturnTheSameSolutionIfNoBitsAreMutated() {
    @SuppressWarnings("unchecked")
	RandomGenerator<Double> randomGenerator = mock(RandomGenerator.class) ;
    double mutationProbability = 0.01;

    Mockito.when(randomGenerator.getRandomValue()).thenReturn(0.02, 0.02, 0.02, 0.02, 0.2, 0.2, 0.2, 0.2) ;

    BitFlipMutation mutation = new BitFlipMutation(mutationProbability) ;
    int[] bitsPerVariable = new int[]{NUMBER_OF_BITS_OF_MOCKED_BINARY_PROBLEM, NUMBER_OF_BITS_OF_MOCKED_BINARY_PROBLEM} ;
    BinarySolution solution = new DefaultBinarySolution(bitsPerVariable.length, 2, bitsPerVariable) ;
    BinarySolution oldSolution = (BinarySolution)solution.copy() ;

    ReflectionTestUtils.setField(mutation, "randomGenerator", randomGenerator);

    mutation.execute(solution) ;

    assertEquals(oldSolution, solution) ;
    verify(randomGenerator, times(8)).getRandomValue();
  }

  @Test
  public void shouldMutateATwoVariableSolutionWhenTwoBitsAreMutated() {
    @SuppressWarnings("unchecked")
	RandomGenerator<Double> randomGenerator = mock(RandomGenerator.class) ;
    double mutationProbability = 0.01;

    Mockito.when(randomGenerator.getRandomValue()).thenReturn(0.01, 0.02, 0.02, 0.02, 0.02, 0.02, 0.01, 0.02) ;

    BitFlipMutation mutation = new BitFlipMutation(mutationProbability) ;
    int[] bitsPerVariable = new int[]{NUMBER_OF_BITS_OF_MOCKED_BINARY_PROBLEM, NUMBER_OF_BITS_OF_MOCKED_BINARY_PROBLEM} ;
    BinarySolution solution = new DefaultBinarySolution(bitsPerVariable.length, 2, bitsPerVariable) ;
    BinarySolution oldSolution = (BinarySolution)solution.copy() ;

    ReflectionTestUtils.setField(mutation, "randomGenerator", randomGenerator);

    mutation.execute(solution) ;

    assertNotEquals(oldSolution.getVariableValue(0).get(0), solution.getVariableValue(0).get(0)) ;
    assertNotEquals(oldSolution.getVariableValue(1).get(2), solution.getVariableValue(1).get(2)) ;
    verify(randomGenerator, times(8)).getRandomValue();
 }

  @Test
	public void shouldJMetalRandomGeneratorNotBeUsedWhenCustomRandomGeneratorProvided() {
		// Configuration
		double mutationProbability = 0.1;

    int[] bitsPerVariable = new int[]{NUMBER_OF_BITS_OF_MOCKED_BINARY_PROBLEM} ;
    BinarySolution solution = new DefaultBinarySolution(bitsPerVariable.length, 2, bitsPerVariable) ;

		// Check configuration leads to use default generator by default
		final int[] defaultUses = { 0 };
		JMetalRandom defaultGenerator = JMetalRandom.getInstance();
		AuditableRandomGenerator auditor = new AuditableRandomGenerator(defaultGenerator.getRandomGenerator());
		defaultGenerator.setRandomGenerator(auditor);
		auditor.addListener((a) -> defaultUses[0]++);

		new BitFlipMutation(mutationProbability).execute(solution);
		assertTrue("No use of the default generator", defaultUses[0] > 0);

		// Test same configuration uses custom generator instead
		defaultUses[0] = 0;
		final int[] customUses = { 0 };
		new BitFlipMutation(mutationProbability, () -> {
			customUses[0]++;
			return new Random().nextDouble();
		}).execute(solution);
		assertTrue("Default random generator used", defaultUses[0] == 0);
		assertTrue("No use of the custom generator", customUses[0] > 0);
	}
}
