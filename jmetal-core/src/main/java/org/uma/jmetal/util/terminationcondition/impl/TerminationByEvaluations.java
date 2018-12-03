package org.uma.jmetal.util.terminationcondition.impl;

import org.uma.jmetal.util.terminationcondition.TerminationCondition;

import java.util.Map;

/**
 * Class that allows to check the termination condition based on a maximum number of indicated
 * evaluations.
 *
 *  @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class TerminationByEvaluations implements TerminationCondition {
  private int maximumNumberOfEvaluations ;

  public TerminationByEvaluations(int maximumNumberOfEvaluations) {
    this.maximumNumberOfEvaluations = maximumNumberOfEvaluations ;
  }

  @Override
  public boolean check(Map<String, Object> algorithmStatusData) {
    int currentNumberOfEvaluations = (int) algorithmStatusData.get("EVALUATIONS") ;

    return (currentNumberOfEvaluations >= maximumNumberOfEvaluations) ;
  }
}
