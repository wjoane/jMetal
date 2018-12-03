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

/**
 * Class that allows to check the termination condition based on the value of the Hypervolume
 * quality indicator. Concretely, given the Hypervolume value of a reference front (HVrf),
 * the stopping condition is true when the hypervolume of the received front (HV) is higher than a
 * quality degree value (D) according to the expression "D*HV > HVrf".
 *
 *  @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class TerminationByQualityIndicator<S extends Solution<?>> implements TerminationCondition {
  private Hypervolume<PointSolution> hypervolume;
  private double qualityDegree ;
  private double referenceFrontHypervolume ;
  private int evaluations ;

  public TerminationByQualityIndicator(String referenceParetoFront, double qualityDegree) throws FileNotFoundException {
    Front referenceFront = new ArrayFront(referenceParetoFront);

    hypervolume = new PISAHypervolume<PointSolution>(referenceFront) ;
    referenceFrontHypervolume = ((PISAHypervolume<PointSolution>) hypervolume).evaluate(FrontUtils
        .convertFrontToSolutionList(referenceFront)) ;
    this.qualityDegree = qualityDegree ;
    evaluations = 0 ;
  }

  @Override
  public boolean check(Map<String, Object> algorithmStatusData) {
    List<S> solutionList = (List<S>)algorithmStatusData.get("POPULATION") ;
    ArrayFront arrayFront = new ArrayFront(SolutionListUtils.getNondominatedSolutions(solutionList)) ;

    double hypervolumeValue = hypervolume.evaluate(FrontUtils.convertFrontToSolutionList(arrayFront));

    boolean result = qualityDegree * referenceFrontHypervolume < hypervolumeValue ;

    if (result) {
      this.evaluations = (int)algorithmStatusData.get("EVALUATIONS") ;
      JMetalLogger.logger.info("Evaluations: " + evaluations);
    }
    return result ;
  }

  public int getEvaluations() {
    return evaluations ;
  }
}
