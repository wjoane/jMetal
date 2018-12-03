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

public class RealTimeChartObserver implements MeasureListener<Map<String, Object>> {
  private ChartContainer chart;


  public RealTimeChartObserver(Measurable measurable, String legend, int delay) {
    this(measurable, legend, delay, "") ;
  }

  public RealTimeChartObserver(Measurable measurable, String legend, int delay, String referenceFrontName) {
    MeasureManager measureManager = measurable.getMeasureManager() ;
    BasicMeasure<Map<String, Object>> observedData =  (BasicMeasure<Map<String, Object>>)measureManager
            .<Map<String, Object>>getPushMeasure("ALGORITHM_DATA");

    observedData.register(this);

    chart = new ChartContainer(legend, delay) ;
    try {
      chart.setFrontChart(0, 1, referenceFrontName);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    chart.initChart();
  }

  public void setReferencePointList(List<List<Double>> referencePointList) {
    chart.setReferencePoint(referencePointList);
  }

  @Override
  public void measureGenerated(Map<String, Object> data) {
    int evaluations = (int)data.get("EVALUATIONS") ;
    List<? extends Solution<?>> population = (List<? extends Solution<?>>) data.get("POPULATION");
    if (this.chart != null) {
      this.chart.getFrontChart().setTitle("Evaluation: " + evaluations);
      this.chart.updateFrontCharts(population);
      this.chart.refreshCharts();
    }
  }
}
