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

public class EvaluationObserver implements MeasureListener<Map<String, Object>> {
  private Integer maxEvaluations ;
  private int evaluations ;

  public EvaluationObserver(Measurable measurable) {
    this(measurable, null);
  }

  public EvaluationObserver(Measurable measurable, Integer maxEvaluations) {
    this.maxEvaluations = maxEvaluations ;
    MeasureManager measureManager = measurable.getMeasureManager() ;

    measureManager = measurable.getMeasureManager() ;
    BasicMeasure<Map<String, Object>> observedData =  (BasicMeasure<Map<String, Object>>)measureManager
            .<Map<String, Object>>getPushMeasure("ALGORITHM_DATA");

    observedData.register(this);
  }

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
