package org.uma.jmetal.problem.multiobjective.fda;

import org.uma.jmetal.problem.BoundedProblem;
import org.uma.jmetal.problem.DynamicProblem;
import org.uma.jmetal.problem.doubleproblem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.observable.Observable;
import org.uma.jmetal.util.observable.impl.DefaultObservable;

import java.io.Serializable;

/** Cristóbal Barba <cbarba@lcc.uma.es> */
public abstract class FDA extends AbstractDoubleProblem
    implements DynamicProblem<DoubleSolution, Integer>, BoundedProblem<Double, DoubleSolution> {
  protected double time;
  private boolean changeStatus = false;
  protected Observable<Integer> observable;

  private int tauT = 5;
  private int nT = 10;

  public FDA(Observable<Integer> observable) {
    this.observable = observable;
    observable.register(this);
  }

  public FDA() {
    this(new DefaultObservable<>("FDA observable"));
  }

  @Override
  public void update(Observable<Integer> observable, Integer counter) {
    time = (1.0d / (double) nT) * Math.floor(counter / (double) tauT);
    JMetalLogger.logger.info("Received counter: " + counter + ". Time: " + time);
    ;

    setChanged();
  }

  @Override
  public boolean hasChanged() {
    return changeStatus;
  }

  @Override
  public void setChanged() {
    changeStatus = true;
  }

  @Override
  public void clearChanged() {
    changeStatus = false;
  }
}