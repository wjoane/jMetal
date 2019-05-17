package org.uma.jmetal.algorithm.multiobjective.adm;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import org.uma.jmetal.algorithm.InteractiveAlgorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.util.ReferencePoint;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.problem.impl.AbstractIntegerDoubleProblem;
import org.uma.jmetal.problem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.ObjectiveComparator;
import org.uma.jmetal.util.distance.impl.EuclideanDistanceBetweenSolutionsInObjectiveSpace;
import org.uma.jmetal.util.point.impl.IdealPoint;
import org.uma.jmetal.util.point.impl.NadirPoint;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;


/**
 * Class implementing the
 * Towards automatic testing of reference point based interactive methods described in:
 * Ojalehto, V., Podkopaev, D., & Miettinen, K. (2016, September).
 * Towards automatic testing of reference point based interactive methods.
 * In International Conference on Parallel Problem Solving from Nature (pp. 483-492). Springer, Cham.
 */
public class ArtificialDM<S extends Solution<?>> extends ArtificialDecisionMaker<S,List<S>> {

  protected List<Double> idealObjectiveVector = null;
  protected List<Double> nadirObjectiveVector = null;
  protected List<Double> rankingCoeficient = null;
  protected List<Double> asp = null;
  protected double tolerance;
  protected int numberReferencePoints;
  protected JMetalRandom random = null;
  protected double considerationProbability;
  protected  int numberOfObjectives;
  protected double varyingProbability;
  protected int evaluations;
  protected int maxEvaluations;
  protected List<List<Double>> allReferencePoints;
  protected List<Double> currentReferencePoint;
  protected List<Double> distances;
  private S solutionRun=null;
  public ArtificialDM(Problem<S> problem,
      InteractiveAlgorithm<S,List<S>> algorithm,double considerationProbability,double tolerance,int maxEvaluations
  ,List<Double> rankingCoeficient,int numberReferencePoints,List<Double> asp) {
    super(problem, algorithm);
    this.considerationProbability = considerationProbability;
    this.tolerance = tolerance;
    this.numberOfObjectives=problem.getNumberOfObjectives();
    this.random = JMetalRandom.getInstance();
    this.maxEvaluations = maxEvaluations;
    this.rankingCoeficient = rankingCoeficient;
    if(rankingCoeficient==null || rankingCoeficient.isEmpty()){
      initialiceRankingCoeficient();
    }
    this.numberReferencePoints = numberReferencePoints;
    this.allReferencePoints = new ArrayList<>();
    this.distances = new ArrayList<>();
    this.idealObjectiveVector = Util.initializeList(this.numberOfObjectives);
    this.nadirObjectiveVector = Util.initializeList(this.numberOfObjectives);

    this.asp = asp;
  }


  private  void  initialiceRankingCoeficient(){
    rankingCoeficient = new ArrayList<>();
    for (int i = 0; i < problem.getNumberOfObjectives() ; i++) {
      rankingCoeficient.add(1.0/problem.getNumberOfObjectives());
    }
  }

  private void updateObjectiveVector(List<S> solutionList){
   for (int j = 0; j < numberOfObjectives; j++) {
      Collections.sort(solutionList, new ObjectiveComparator<>(j));
      double objetiveMinn = solutionList.get(0).getObjective(j);
      double objetiveMaxn = solutionList.get(solutionList.size() - 1).getObjective(j);
      idealObjectiveVector.set(j,objetiveMinn);
      nadirObjectiveVector.set(j,objetiveMaxn);
    }
    if(problem instanceof AbstractDoubleProblem){
      AbstractDoubleProblem aux =(AbstractDoubleProblem)problem;
      for (int i = 0; i < numberOfObjectives ; i++) {
        idealObjectiveVector.set(i,aux.getLowerBound(i));
        nadirObjectiveVector.set(i,aux.getUpperBound(i));
      }
    }else if(problem instanceof AbstractIntegerProblem){
      AbstractIntegerProblem aux =(AbstractIntegerProblem)problem;
      for (int i = 0; i < numberOfObjectives ; i++) {
        idealObjectiveVector.set(i,aux.getLowerBound(i).doubleValue());
        nadirObjectiveVector.set(i,aux.getUpperBound(i).doubleValue());
      }
    }else if(problem instanceof AbstractIntegerDoubleProblem){
      AbstractIntegerDoubleProblem aux =(AbstractIntegerDoubleProblem)problem;
      for (int i = 0; i < numberOfObjectives ; i++) {
        idealObjectiveVector.set(i,aux.getLowerBound(i).doubleValue());
        nadirObjectiveVector.set(i,aux.getUpperBound(i).doubleValue());
      }
    }
    if(asp==null)
      asp = idealObjectiveVector;
  }

  @Override
  protected List<Double> generatePreferenceInformation() {

    idealObjectiveVector = Util.initializeList(this.numberOfObjectives);
    nadirObjectiveVector = Util.initializeList(this.numberOfObjectives);
    List<S> solutions = new ArrayList<>();
    S sol = problem.createSolution();
    problem.evaluate(sol);
    solutions.add(sol);
    updateObjectiveVector(solutions);
   // solutionRun = solutions.get(0);
    List<Double> referencePoints  = new ArrayList<>();
    for (int i=0;i < numberReferencePoints;i++){
      List<Double> referencePoint = Util.initializeList(this.numberOfObjectives);
      for (int j = 0; j < numberOfObjectives; j++) {
        double rand = random.nextDouble(0.0, 1.0);
        if (rand < considerationProbability * rankingCoeficient.get(i)) {
          referencePoint.set(j, asp.get(j));
        } else {
          referencePoint.set(j, nadirObjectiveVector.get(j));
        }
      }
      referencePoints.addAll(referencePoint);
      if(i==0)currentReferencePoint = referencePoint;
    }

    allReferencePoints.add(referencePoints);
    return referencePoints;
  }

  @Override
  protected boolean isStoppingConditionReached() {
    boolean stop = evaluations > maxEvaluations ;

    if(indexOfRelevantObjectiveFunctions!=null   ){
      stop = stop || indexOfRelevantObjectiveFunctions.size()==numberOfObjectives;
    }
    return stop;
  }


  @Override
  protected void initProgress() {
    evaluations =0;
    varyingProbability = considerationProbability;
  }

  @Override
  protected void updateProgress() {
    evaluations++;
  }

  @Override
  protected List<Integer> relevantObjectiveFunctions(List<S> front) {
    List<Integer> order = new ArrayList<>();
    List<Integer> indexRelevantObjectivesFunctions = new ArrayList<>();
    SortedMap<Double, List<Integer>> map = new TreeMap<>(Collections.reverseOrder());
    for (int i = 0; i < rankingCoeficient.size(); i++) {
      List<Integer> aux = map.getOrDefault(rankingCoeficient.get(i), new ArrayList<>());
      aux.add(i);
      map.putIfAbsent(rankingCoeficient.get(i),aux);
    }
    Set<Double> keys =map.keySet();
    for (Double key:keys) {
      order.addAll(map.get(key));
    }
    S solution = getSolution(front,currentReferencePoint);
    for (Integer i : order) {
      double rand = random.nextDouble(0.0, 1.0);
      if ((asp.get(i) - solution.getObjective(i)) < tolerance
          && rand < considerationProbability) {
        indexRelevantObjectivesFunctions.add(i);
      } else if (rand < varyingProbability) {
        indexRelevantObjectivesFunctions.add(i);
      }
      varyingProbability -= (varyingProbability / i) * indexRelevantObjectivesFunctions.size();
    }
    return indexRelevantObjectivesFunctions;
  }

  @Override
  protected List<Double> calculateReferencePoints(
      List<Integer> indexOfRelevantObjectiveFunctions, List<S> front,List<S> paretoOptimalSolutions) {
    List<Double> result = new ArrayList<>();
    List<S> temporal = new ArrayList<>(front);

    for(int numRefPoint=0;numRefPoint<numberReferencePoints;numRefPoint++){
      S solution = getSolution(temporal,currentReferencePoint);
      solutionRun = solution;
      temporal.remove(solution);
        List<Double> referencePoint = Util.initializeList(numberOfObjectives);
        for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
          if (indexOfRelevantObjectiveFunctions.contains(i)) {
            referencePoint.set(i,
                asp.get(i) - (asp.get(i) - solution.getObjective(i)) / 2);
          } else {
            //predict the i position of reference point
            referencePoint.set(i, prediction(i,front,solution));//paretoOptimalSolutions
          }
        }
        calculateDistance(solutionRun,asp);
        result.addAll(referencePoint);
        if(numRefPoint==0)currentReferencePoint = referencePoint;
    }

    allReferencePoints.add(result);

    return result;
  }

  private void calculateDistance(S solution, List<Double> referencePoint) {
    EuclideanDistanceBetweenSolutionsInObjectiveSpace euclidean = new EuclideanDistanceBetweenSolutionsInObjectiveSpace();

    double distance = euclidean
        .getDistance((DoubleSolution) solution, getSolutionFromRP(referencePoint));
    distances.add(distance);
  }
  private DoubleSolution getSolutionFromRP(List<Double> referencePoint){
    DoubleSolution result = (DoubleSolution)problem.createSolution();
    for (int i = 0; i < result.getNumberOfObjectives(); i++) {
      result.setObjective(i,referencePoint.get(i));
      result.setVariableValue(i,referencePoint.get(i));

    }
    return result;
  }

 private double prediction(int index,List<S> paretoOptimalSolutions,S solution) {
   DecisionTreeEstimator<S> dte = new DecisionTreeEstimator<S>(paretoOptimalSolutions);

   double data=dte.doPrediction(index,solution);
   return data;
 }
  @Override
  protected void updateParetoOptimal(List<S> front,List<S> paretoOptimalSolutions) {
    //paretoOptimalSolutions.addAll(front);
    paretoOptimalSolutions = new ArrayList<>(front);
  }

  @Override
  public List<List<Double>> getReferencePoints() {

    allReferencePoints.remove(allReferencePoints.size()-1);
   return allReferencePoints;
  }

  @Override
  public List<Double> getDistances() {

    return distances;
  }

  private S getSolution(List<S> front, List<Double> referencePoint) {
    S result = front.get(0);
    EuclideanDistanceBetweenSolutionsInObjectiveSpace euclidean = new EuclideanDistanceBetweenSolutionsInObjectiveSpace();
    SortedMap<Double, S> map = new TreeMap<>();
    DoubleSolution aux = getSolutionFromRP(referencePoint);
    for (S solution : front) {
      double distance = euclidean.getDistance(solution,aux);
      map.put(distance, solution);
    }
    result = map.get(map.firstKey());
    return result;
  }

  private S referencePointToSolution(List<Double> referencePoint){
   S result = problem.createSolution();
    for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
      result.setObjective(i,referencePoint.get(i));
    }
    return result;
  }

}
