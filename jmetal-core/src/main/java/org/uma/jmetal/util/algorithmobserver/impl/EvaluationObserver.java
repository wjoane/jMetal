package org.uma.jmetal.util.algorithmobserver.impl;

import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.Measure;
import org.uma.jmetal.measure.MeasureListener;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.BasicMeasure;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.algorithmobserver.AlgorithmObserver;
import org.uma.jmetal.util.chartcontainer.ChartContainer;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * This observer prints the current evaluation number of an algorithm. It requires a pair
 * (EVALUATIONS, int) in the map used in the measureGenerated() method.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class EvaluationObserver extends AlgorithmObserver {
  private Integer maxEvaluations, evaluations ;

  public EvaluationObserver(Measurable measurable) {
    this(measurable, null);
  }

  /**
   * Constructor
   * @param measurable Measurable algorithm
   * @param maxEvaluations Optional field that, if present, is used to be printed with the evaluation
   * value.
   */
  public EvaluationObserver(Measurable measurable, Integer maxEvaluations) {
    super(measurable) ;
    this.maxEvaluations = maxEvaluations ;
  }

  /**
   * This method gets the evaluation number from the dada map and prints it in the screen.
   * @param data Map of pairs (key, value)
   */
  @Override
  public void measureGenerated(Map<String, Object> data) {

    evaluations = (Integer)data.get("EVALUATIONS") ;

    if (evaluations!=null) {
      if (maxEvaluations == null) {
        System.out.println("Evaluations: " + evaluations);
      } else {
        System.out.println("Evaluations: " + evaluations + " from " + maxEvaluations);
      }
    } else {
      JMetalLogger.logger.info(getClass().getName()+
                                  ": The algorithm has not registered yet any info related to the EVALUATIONS key");
    }
  }

  public int getEvaluations() {
    return evaluations;
  }
}
