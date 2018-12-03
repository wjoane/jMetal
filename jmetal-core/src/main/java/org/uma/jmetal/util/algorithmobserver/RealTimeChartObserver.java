package org.uma.jmetal.util.algorithmobserver;

import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.MeasureListener;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.BasicMeasure;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.chartcontainer.ChartContainer;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * This observer prints a chart in real time showing the current Pareto front approximation produced
 * by an algorithm. It requires a two pairs in the map used in the measureGenerated() method:
 *  * - (EVALUATIONS, int)
 *  * - (POPULATION, List<Solution>)
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class RealTimeChartObserver<S extends Solution<?>> implements MeasureListener<Map<String, Object>> {
  private ChartContainer<S> chart;
  private int evaluations ;

  /**
   * Constructor
   * @param measurable Measurable algorithm
   * @param legend Legend to be included in the chart
   * @param delay Display delay
   */
  public RealTimeChartObserver(Measurable measurable, String legend, int delay) {
    this(measurable, legend, delay, "") ;
  }

  /**
   *
   * @param measurable Measurable algorithm
   * @param legend Legend to be included in the chart
   * @param delay Display delay
   * @param referenceFrontName File name containing a reference front
   */
  public RealTimeChartObserver(Measurable measurable, String legend, int delay, String referenceFrontName) {
    MeasureManager measureManager = measurable.getMeasureManager() ;
    BasicMeasure<Map<String, Object>> observedData =  (BasicMeasure<Map<String, Object>>)measureManager
            .<Map<String, Object>>getPushMeasure("ALGORITHM_DATA");

    observedData.register(this);

    chart = new ChartContainer<S>(legend, delay) ;
    try {
      chart.setFrontChart(0, 1, referenceFrontName);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    chart.initChart();
  }

  /**
   * This method is used to set a list of reference points; it is used by reference-point based
   * algorithms.
   * @param referencePointList
   */
  public void setReferencePointList(List<List<Double>> referencePointList) {
    chart.setReferencePoint(referencePointList);
  }

  /**
   * This methods displays a front (population)
   * @param data Map of pairs (key, value)
   */
  @Override
  public void measureGenerated(Map<String, Object> data) {
    this.evaluations = (int)data.get("EVALUATIONS") ;
    List<S> population = (List<S>) data.get("POPULATION");
    if (this.chart != null) {
      this.chart.getFrontChart().setTitle("Evaluation: " + evaluations);
      this.chart.updateFrontCharts(population);
      this.chart.refreshCharts();
    }
  }

  public ChartContainer getChart() {
    return chart ;
  }

  public int getEvaluations() {
    return evaluations ;
  }
}
