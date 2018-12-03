package org.uma.jmetal.util.terminationcondition.impl;

import org.uma.jmetal.util.terminationcondition.TerminationCondition;

import java.util.Map;

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
