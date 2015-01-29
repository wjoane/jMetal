//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package org.uma.jmetal.util.comparator;

import org.uma.jmetal.solution.Solution;

import java.util.Comparator;

/**
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 * @version 1.0
 *
 * This class implements a comparator based on a given objective
 */
public class ObjectiveComparator implements Comparator<Solution> {
  public enum Ordering {ASCENDING, DESCENDING} ;
  private int objectiveId;

  private Ordering order;

  /**
   * Constructor.
   *
   * @param objectiveId The index of the objective to compare
   */
  public ObjectiveComparator(int objectiveId) {
    this.objectiveId = objectiveId;
    order = Ordering.ASCENDING;
  }

  /**
   * Comparator.
   * @param objectiveId The index of the objective to compare
   * @param order Ascending or descending order
   */
  public ObjectiveComparator(int objectiveId, Ordering order) {
    this.objectiveId = objectiveId;
    this.order = order ;
  }

  /**
   * Compares two solutions.
   *
   * @param solution1 The first solution
   * @param solution2 The second solution
   * @return -1, or 0, or 1 if solution1 is less than, equal, or greater than solution2,
   * respectively, according to the established order
   */
  @Override
  public int compare(Solution solution1, Solution solution2) {
    int result ;
    if (solution1 == null) {
      if (solution2 == null) {
        result = 0;
      } else {
        result =  1;
      }
    } else if (solution2 == null) {
      result =  -1;
    } else {
      Double objective1 = solution1.getObjective(this.objectiveId);
      Double objective2 = solution2.getObjective(this.objectiveId);
      if (order == Ordering.ASCENDING) {

        //      if (objective1 < objective2) {
        //        return -1;
        //      } else if (objective1 > objective2) {
        //        return 1;
        //      } else {
        //        return 0;
        //      }
        //return compareOrder(objective1, objective2) ;
        result = Double.compare(objective1, objective2);
      } else {
        //      if (objective1 < objective2) {
        //        return 1;
        //      } else if (objective1 > objective2) {
        //        return -1;
        //      } else {
        //        return 0;
        //      }
        //return compareOrder(objective2, objective1) ;
        result = Double.compare(objective2, objective1);
      }
    }
    return result ;
  }

  private int compareOrder(double value1, double value2) {
    if (value1 < value1) {
      return -1;
    } else if (value1 > value1) {
      return 1;
    } else {
      return 0;
    }
  }
}
