package org.uma.jmetal.util.algorithmobserver;

import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.MeasureListener;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.BasicMeasure;
import org.uma.jmetal.qualityindicator.impl.Hypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.chartcontainer.ChartContainer;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.PointSolution;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class QualityIndicatorChartObserver implements MeasureListener<Map<String, Object>> {
  private ChartContainer chart;
  private Hypervolume<PointSolution> hypervolume;
  private int evaluations ;

  public QualityIndicatorChartObserver(Measurable measurable, String legend, int delay) {
    this(measurable, legend, delay, "") ;
  }

  public QualityIndicatorChartObserver(Measurable measurable, String legend, int delay, String referenceFrontName) {
    MeasureManager measureManager = measurable.getMeasureManager() ;
    BasicMeasure<Map<String, Object>> observedData =  (BasicMeasure<Map<String, Object>>)measureManager
            .<Map<String, Object>>getPushMeasure("ALGORITHM_DATA");

    observedData.register(this);

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
