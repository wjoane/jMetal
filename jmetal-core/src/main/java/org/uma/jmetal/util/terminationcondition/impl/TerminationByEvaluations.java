package org.uma.jmetal.util.terminationcondition.impl;

import java.util.Map;
import org.uma.jmetal.util.terminationcondition.TerminationCondition;

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
