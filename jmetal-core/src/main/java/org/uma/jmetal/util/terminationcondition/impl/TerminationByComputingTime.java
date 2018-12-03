package org.uma.jmetal.util.terminationcondition.impl;

import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.terminationcondition.TerminationCondition;

import java.util.Map;

/**
 * Class that allows to check the termination condition when the computing time of an algorithm
 * gets higher than a given threshold.
 */
public class TerminationByComputingTime implements TerminationCondition {
  private long maxComputingTime ;

  public TerminationByComputingTime(int maxComputingTime) {
    this.maxComputingTime = maxComputingTime ;
  }

  @Override
  public boolean check(Map<String, Object> algorithmStatusData) {
    long currentComputingTime = (long) algorithmStatusData.get("COMPUTING_TIME") ;

    boolean result = currentComputingTime >= maxComputingTime ;
    if (result) {
      JMetalLogger.logger.info("Evaluations: " + (int)algorithmStatusData.get("EVALUATIONS"));
    }

    return result ;
  }
}
