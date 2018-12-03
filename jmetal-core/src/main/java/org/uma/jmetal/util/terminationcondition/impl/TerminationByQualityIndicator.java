package org.uma.jmetal.util.terminationcondition.impl;

import org.uma.jmetal.qualityindicator.impl.Hypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.PointSolution;
import org.uma.jmetal.util.terminationcondition.TerminationCondition;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class TerminationByQualityIndicator<S extends Solution<?>> implements TerminationCondition {
  private Hypervolume<PointSolution> hypervolume;
  private double qualityDegree ;
  private double referenceFrontHypervolume ;

  public TerminationByQualityIndicator(String referenceParetoFront, double qualityDegree) throws FileNotFoundException {
    Front referenceFront = new ArrayFront(referenceParetoFront);

    hypervolume = new PISAHypervolume<PointSolution>(referenceFront) ;
    referenceFrontHypervolume = ((PISAHypervolume<PointSolution>) hypervolume).evaluate(FrontUtils
        .convertFrontToSolutionList(referenceFront)) ;
    this.qualityDegree = qualityDegree ;

  }

  @Override
  public boolean check(Map<String, Object> algorithmStatusData) {
    List<S> solutionList = (List<S>)algorithmStatusData.get("POPULATION") ;
    ArrayFront arrayFront = new ArrayFront(SolutionListUtils.getNondominatedSolutions(solutionList)) ;

    double hypervolumeValue = hypervolume.evaluate(FrontUtils.convertFrontToSolutionList(arrayFront));

    boolean result = qualityDegree * referenceFrontHypervolume < hypervolumeValue ;
    if (result) {
      JMetalLogger.logger.info("Evaluations: " + (int)algorithmStatusData.get("EVALUATIONS"));
    }
    return result ;
  }
}
