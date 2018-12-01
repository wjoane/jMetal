package org.uma.jmetal.util.terminationcondition;

import java.util.Map;

@FunctionalInterface
public interface TerminationCondition {
  boolean check(Map<String, Object> algorithmStatusData) ;
}
