package org.uma.jmetal.util.terminationcondition;

import java.util.Map;

@FunctionalInterface
/**
 * This interface represents classes that check the termination condition of an algorithm.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public interface TerminationCondition {
  boolean check(Map<String, Object> algorithmStatusData) ;
}
