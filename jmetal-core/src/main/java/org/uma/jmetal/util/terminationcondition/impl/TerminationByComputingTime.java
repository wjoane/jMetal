package org.uma.jmetal.util.terminationcondition.impl;

import java.util.Map;
import org.uma.jmetal.util.terminationcondition.TerminationCondition;

public class TerminationByComputingTime implements TerminationCondition {
  private long maxComputingTime ;

  public TerminationByComputingTime(int maxComputingTime) {
    this.maxComputingTime = maxComputingTime ;
  }

  @Override
  public boolean check(Map<String, Object> algorithmStatusData) {
    long currentComputingTime = (long) algorithmStatusData.get("COMPUTING_TIME") ;

    return (currentComputingTime >= maxComputingTime) ;
  }
}
