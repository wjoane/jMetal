package org.uma.jmetal.util.chartcontainer;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontUtils;

/**
 * Class for configuring and displaying a XChart.
 *
 * @author Jorge Rodriguez Ordonez
 */

public class ChartContainer {
	private Map<String, XYChart> charts;
	private XYChart frontChart;
	private XYChart varChart;
	private SwingWrapper<XYChart> sw;
	private String name;
	private int delay;
	private int objective1;
	private int objective2;
	private int variable1;
	private int variable2;
	private Map<String, List<Integer>> iterations;
	private Map<String, List<Double>> indicatorValues;
	
	public ChartContainer(String name) {
		this(name, 0);
	}
	
	public ChartContainer(String name, int delay) {
		this.name = name;
		this.delay = delay;
		this.charts = new LinkedHashMap<String, XYChart>();
		this.iterations = new HashMap<String, List<Integer>>();
		this.indicatorValues = new HashMap<String, List<Double>>();
	}
	
	public void setFrontChart(int objective1, int objective2) throws FileNotFoundException {
		this.setFrontChart(objective1, objective2, null);
	}
	
	public void setFrontChart(int objective1, int objective2, String referenceFrontFileName) throws FileNotFoundException {
		this.objective1 = objective1;
		this.objective2 = objective2;
		this.frontChart = new XYChartBuilder().xAxisTitle("Objective " + this.objective1)
						.yAxisTitle("Objective " + this.objective2).build();
		this.frontChart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter).setMarkerSize(5);
		
		//if (referenceFrontFileName != null) {
		//	this.displayReferenceFront(referenceFrontFileName);
		//}
		
		double[] xData = new double[]{0};
		double[] yData = new double[]{0};
		XYSeries frontChartSeries = this.frontChart.addSeries(this.name, xData, yData);
		frontChartSeries.setMarkerColor(Color.blue);
		
		this.charts.put("Front", this.frontChart);
	}
	
	public void addFrontChart(int objective1, int objective2) {
		XYChart chart = new XYChartBuilder()
						.xAxisTitle("Objective " + objective1)
						.yAxisTitle("Objective " + objective2)
						.build();
		chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter).setMarkerSize(5);
		
		double[] xData = new double[]{0};
		double[] yData = new double[]{0};
		XYSeries frontChartSeries = chart.addSeries(this.name, xData, yData);
		frontChartSeries.setMarkerColor(Color.blue);
		
		this.charts.put("" + objective1 + "," + objective2, chart) ;
	}
	
	public void setReferencePoint(List<Double> referencePoint) {
		int referencePoint1 ;
		int referencePoint2 ;
		for (int i = 0; i < referencePoint.size(); i++) {
			for (int j = i +1 ; j < referencePoint.size(); j++) {
				String key = "" + i + "," + j ;
				System.out.println(key) ;
				XYSeries referencePointSeries = charts.get(key).addSeries("Reference Point [" + referencePoint.get(i) + ", " + referencePoint.get(j) + "]",
								new double[]{referencePoint.get(i)},
								new double[]{referencePoint.get(j)});
				referencePointSeries.setMarkerColor(Color.green);
			}
		}
		
		/*
		for (Map.Entry<String, XYChart> entry : this.charts.entrySet()) {
			XYSeries referencePointSeries = entry.getValue().addSeries("Reference Point [" + rp1 + ", " + rp2 + "]",
							new double[]{rp1},
							new double[]{rp2});
			referencePointSeries.setMarkerColor(Color.green);
		}
		*/
		
		/*
		double rp1 = referencePoint.get(this.objective1);
		double rp2 = referencePoint.get(this.objective2);
		XYSeries referencePointSeries = this.frontChart.addSeries("Reference Point [" + rp1 + ", " + rp2 + "]",
						new double[]{rp1},
						new double[]{rp2});
		referencePointSeries.setMarkerColor(Color.green);
		*/
	}
	
	public void setVarChart(int variable1, int variable2) {
		this.variable1 = variable1;
		this.variable2 = variable2;
		this.varChart = new XYChartBuilder().xAxisTitle("Variable " + this.variable1)
						.yAxisTitle("Variable " + this.variable2).build();
		this.varChart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter).setMarkerSize(5);
		
		double[] xData = new double[]{0};
		double[] yData = new double[]{0};
		
		XYSeries varChartSeries = this.varChart.addSeries(this.name, xData, yData);
		varChartSeries.setMarkerColor(Color.blue);
		
		this.charts.put("VAR", this.varChart);
	}
	
	public void initChart() {
		this.sw = new SwingWrapper<XYChart>(new ArrayList<XYChart>(this.charts.values()));
		this.sw.displayChartMatrix(this.name);
	}
	
	public void updateFrontCharts(List<DoubleSolution> solutionList) {
		/*
		if (this.frontChart != null) {
			this.frontChart.updateXYSeries(this.name,
							this.getSolutionsForObjective(solutionList, this.objective1),
							this.getSolutionsForObjective(solutionList, this.objective2),
							null);
		}
		
		if (this.varChart != null) {
			this.varChart.updateXYSeries(this.name,
							this.getVariableValues(solutionList, this.variable1),
							this.getVariableValues(solutionList, this.variable2),
							null);
		}
		*/
		
		for (Map.Entry<String, XYChart> entry : this.charts.entrySet()) {
			
			int objective1 = Integer.parseInt(entry.getKey().substring(0, 1)) ;
			int objective2 = Integer.parseInt(entry.getKey().substring(2, 3)) ;
			
			entry.getValue().updateXYSeries(this.name,
							this.getSolutionsForObjective(solutionList, objective1),
							this.getSolutionsForObjective(solutionList, objective2), null) ;
		}
	}
	
	public void refreshCharts() {
		this.refreshCharts(this.delay);
	}
	
	public void refreshCharts(int delay) {
		if (delay > 0) {
			try {
				TimeUnit.MILLISECONDS.sleep(delay);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.repaint();
	}
	
	public void addIndicatorChart(String indicator) {
		XYChart indicatorChart = new XYChartBuilder().xAxisTitle("n").yAxisTitle(indicator).build();
		indicatorChart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter).setMarkerSize(5);
		
		List<Integer> indicatorIterations = new ArrayList<Integer>();
		indicatorIterations.add(0);
		List<Double> indicatorValues = new ArrayList<Double>();
		indicatorValues.add(0.0);
		
		XYSeries indicatorSeries = indicatorChart.addSeries(this.name, indicatorIterations, indicatorValues);
		indicatorSeries.setMarkerColor(Color.blue);
		
		this.iterations.put(indicator, indicatorIterations);
		this.indicatorValues.put(indicator, indicatorValues);
		this.charts.put(indicator, indicatorChart);
	}
	
	public void removeIndicator(String indicator) {
		this.iterations.remove(indicator);
		this.indicatorValues.remove(indicator);
		this.charts.remove(indicator);
	}
	
	public void updateIndicatorChart(String indicator, Double value) {
		this.indicatorValues.get(indicator).add(value);
		this.iterations.get(indicator).add(this.indicatorValues.get(indicator).size());
		
		this.charts.get(indicator).updateXYSeries(this.name, this.iterations.get(indicator),
						this.indicatorValues.get(indicator), null);
	}
	
	public void repaint() {
		try {
			for (int i = 0; i < this.charts.values().size(); i++) {
				this.sw.repaintChart(i);
			}
		} catch (IndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void displayFront(String name, String fileName, int objective1, int objective2)
					throws FileNotFoundException {
		ArrayFront front = new ArrayFront(fileName);
		double[][] data = FrontUtils.convertFrontToArray(front);
		double[] xData = getObjectiveValues(data, objective1);
		double[] yData = getObjectiveValues(data, objective2);
		XYSeries referenceFront = this.frontChart.addSeries(name, xData, yData);
		referenceFront.setMarkerColor(Color.red);
	}
	
	private double[] getObjectiveValues(double[][] data, int obj) {
		double[] values = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			values[i] = data[i][obj];
		}
		return values;
	}
	
	private double[] getSolutionsForObjective(List<DoubleSolution> solutionList, int objective) {
		double[] result = new double[solutionList.size()];
		for (int i = 0; i < solutionList.size(); i++) {
			result[i] = solutionList.get(i).getObjective(objective);
		}
		return result;
	}
	
	private double[] getVariableValues(List<DoubleSolution> solutionList, int variable) {
		double[] result = new double[solutionList.size()];
		for (int i = 0; i < solutionList.size(); i++) {
			result[i] = solutionList.get(i).getVariableValue(variable);
		}
		return result;
	}
	
	public void saveChart(String fileName, BitmapFormat format) throws IOException {
		for (String chart : this.charts.keySet()) {
			BitmapEncoder.saveBitmap(this.charts.get(chart), fileName + "_" + chart, format);
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	public ChartContainer setName(String name) {
		this.name = name;
		return this;
	}
	
	public int getDelay() {
		return this.delay;
	}
	
	public ChartContainer setDelay(int delay) {
		this.delay = delay;
		return this;
	}
	
	public XYChart getFrontChart() {
		return this.frontChart;
	}
	
	public XYChart getVarChart() {
		return this.varChart;
	}
	
	public List<XYChart> getCharts() {
		List<XYChart> chartList = new ArrayList<>() ;
		
		for (Map.Entry<String, XYChart> entry : this.charts.entrySet()) {
			chartList.add(entry.getValue()) ;
		}
		return chartList ;
	}
	
	public XYChart getChart(String chartName) {
		return this.charts.get(chartName);
	}
}
