package org.uma.jmetal.util.terminationcondition;

import java.util.Map;

@FunctionalInterface
/**
 * This interface represents classes that isMet the termination condition of an algorithm.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public interface TerminationCondition {
  boolean isMet(Map<String, Object> algorithmStatusData) ;
}
