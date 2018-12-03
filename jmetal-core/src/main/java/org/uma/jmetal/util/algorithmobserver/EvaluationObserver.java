package org.uma.jmetal.util.algorithmobserver;

import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.Measure;
import org.uma.jmetal.measure.MeasureListener;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.BasicMeasure;
import org.uma.jmetal.solution.Solution;
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
public class EvaluationObserver implements MeasureListener<Map<String, Object>> {
  private Integer maxEvaluations ;
  private int evaluations ;

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
    this.maxEvaluations = maxEvaluations ;
    MeasureManager measureManager = measurable.getMeasureManager() ;

    BasicMeasure<Map<String, Object>> observedData =  (BasicMeasure<Map<String, Object>>)measureManager
            .<Map<String, Object>>getPushMeasure("ALGORITHM_DATA");

    observedData.register(this);
  }

  /**
   * This method gets the evaluation number from the dada map and prints it in the screen.
   * @param data Map of pairs (key, value)
   */
  @Override
  public void measureGenerated(Map<String, Object> data) {
    this.evaluations = (int)data.get("EVALUATIONS") ;
    if (maxEvaluations == null) {
      System.out.println("Evaluations: " + evaluations) ;
    } else {
      System.out.println("Evaluations: " + evaluations + " from " + maxEvaluations);
    }
  }

  public int getEvaluations() {
    return evaluations ;
  }
}
