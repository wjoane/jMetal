package org.uma.jmetal.algorithm.multiobjective.adm;

import org.uma.jmetal.solution.Solution;
import smile.classification.DecisionTree;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.Table;

import java.util.List;

public class DecisionTreeSmile<S extends Solution<?>> {
  private List<S> solutionList = null;
  private Table table = null;
  private int numObjectives;
  private DecisionTree decisionTree;

  public DecisionTreeSmile(List<S> solutionList) {
    this.solutionList = solutionList;
    this.numObjectives = solutionList.get(0).getNumberOfObjectives();
    createTable();

  }

  private void createTable() {
    DoubleColumn[] columns = new DoubleColumn[solutionList.size()];
    double[][] columnas = new double[numObjectives][solutionList.size()];
    int i = 0;
    for (S aux : solutionList) {
      for (int j = 0; j < aux.getNumberOfObjectives(); j++) {
        columnas[i][j] = aux.getObjective(j);
      }

      i++;
    }
    for (int c = 0; c < columnas.length; c++) {
      DoubleColumn column = DoubleColumn.create("objective" + c, columnas[c]);
      columns[c] = column;
    }
    table = Table.create("Tree", columns);
  }

  public double doPrediction(int index, S testSolution) {
    double result = -1;
    try {

    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return result;
  }
}
