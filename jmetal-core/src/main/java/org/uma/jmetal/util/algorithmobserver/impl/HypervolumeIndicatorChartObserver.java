package org.uma.jmetal.util.algorithmobserver.impl;

import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.MeasureListener;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.BasicMeasure;
import org.uma.jmetal.qualityindicator.impl.Hypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.algorithmobserver.AlgorithmObserver;
import org.uma.jmetal.util.chartcontainer.ChartContainer;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.PointSolution;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * This observer draws a chart with the values of the hypervolume indicator during the execution
 * of an algorithm. It requires a two pairs in the map used in the measureGenerated() method:
 * - (EVALUATIONS, int)
 * - (POPULATION, List<Solution>)
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class HypervolumeIndicatorChartObserver extends AlgorithmObserver {
  private ChartContainer chart;
  private Hypervolume<PointSolution> hypervolume;
  private int evaluations ;

  /**
   * Constructor
   * @param measurable Measurable algorithm
   * @param legend Legend to be included in the chart
   * @param delay Display delay
   */
  public HypervolumeIndicatorChartObserver(Measurable measurable, String legend, int delay) {
    this(measurable, legend, delay, "") ;
  }

  /**
   * Constructor.
   * @param measurable Measurable algorithm
   * @param legend Legend to be included in the chart
   * @param delay Display delay
   * @param referenceFrontName File name containing a reference front, needed to compute the hypervolume
   */
  public HypervolumeIndicatorChartObserver(Measurable measurable, String legend, int delay, String referenceFrontName) {
    super(measurable) ;

    Front referenceFront = null;
    try {
      referenceFront = new ArrayFront(referenceFrontName);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    hypervolume = new PISAHypervolume<PointSolution>(referenceFront) ;

    chart = new ChartContainer(legend, delay) ;
    chart.addIndicatorChart("Hypervolume");
    chart.initChart();
  }

  /**
   * This method computes the hypervolume of the solution list (population) and prints it in the screen.
   * @param data Map of pairs (key, value)
   */
  @Override
  public void measureGenerated(Map<String, Object> data) {
    this.evaluations = (int)data.get("EVALUATIONS") ;
    List<? extends Solution<?>> population = (List<? extends Solution<?>>) data.get("POPULATION");

    ArrayFront arrayFront = new ArrayFront(SolutionListUtils.getNondominatedSolutions(population)) ;

    double hypervolumeValue = hypervolume.evaluate(FrontUtils.convertFrontToSolutionList(arrayFront)) ;

    if (this.chart != null) {
      this.chart.getChart("Hypervolume").setTitle("Evaluations: " + evaluations);
      this.chart.updateIndicatorChart("Hypervolume", hypervolumeValue);
      this.chart.refreshCharts();
    }
  }

  public int getEvaluations() {
    return evaluations ;
  }
}
