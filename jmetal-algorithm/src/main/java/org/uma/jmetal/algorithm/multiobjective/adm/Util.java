package org.uma.jmetal.algorithm.multiobjective.adm;

import java.util.ArrayList;
import java.util.List;

public class Util {

    public static List<Double> initializeList(int size){
        List<Double> list = new ArrayList<>(size);
        for (int i=0;i < size;i++){
            list.add(0.0d);
        }
        return list;
    }
}
