package org.uma.jmetal.auto.replacement.impl;

import org.uma.jmetal.auto.replacement.Replacement;
import org.uma.jmetal.auto.selection.impl.RankingAndDensityEstimatorMatingPoolSelection;
import org.uma.jmetal.auto.util.densityestimator.DensityEstimator;
import org.uma.jmetal.auto.util.ranking.Ranking;
import org.uma.jmetal.solution.Solution;

import java.util.ArrayList;
import java.util.List;

public class RankingAndDensityEstimatorReplacement<S extends Solution<?>> implements Replacement<S> {
  public Ranking<S> ranking ;
  public DensityEstimator<S> densityEstimator ;

  public RankingAndDensityEstimatorReplacement(Ranking<S> ranking, DensityEstimator<S> densityEstimator) {
    this.ranking = ranking ;
    this.densityEstimator = densityEstimator ;
  }

  public List<S> replace(List<S> currentList, List<S> offspringList) {
    List<S> jointPopulation = new ArrayList<>();
    jointPopulation.addAll(currentList);
    jointPopulation.addAll(offspringList);

    RankingAndDensityEstimatorMatingPoolSelection<S> selection ;
    selection = new RankingAndDensityEstimatorMatingPoolSelection<S>(currentList.size(), ranking, densityEstimator);

    return selection.select(jointPopulation) ;
  }
}