package org.uma.jmetal.util.algorithmobserver;

import java.util.Map;
import org.uma.jmetal.measure.Measurable;
import org.uma.jmetal.measure.MeasureListener;
import org.uma.jmetal.measure.MeasureManager;
import org.uma.jmetal.measure.impl.BasicMeasure;

/**
 * Abstract representing algorithm observer objects which registers in algorithms implementing the
 * {@link Measurable} interface (observable entities) and receive a map of pairs (string, object).
 * Each pair represents information returned by the algorihtm at the end of each iteration, such
 * as the current evaluation number, the current population, etc.
 *
 */
public abstract class AlgorithmObserver implements MeasureListener<Map<String, Object>> {
   public AlgorithmObserver(Measurable measurable) {
     MeasureManager measureManager = measurable.getMeasureManager() ;

     BasicMeasure<Map<String, Object>> observedData =  (BasicMeasure<Map<String, Object>>)measureManager
         .<Map<String, Object>>getPushMeasure("ALGORITHM_DATA");

     observedData.register(this);
   }
}
