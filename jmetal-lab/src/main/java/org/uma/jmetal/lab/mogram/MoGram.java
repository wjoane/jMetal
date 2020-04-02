package org.uma.jmetal.lab.mogram;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.uma.jmetal.util.distance.Distance;

public abstract class MoGram<S> {
    protected List<S> solutions;
    protected Distance<S, S> distance;

    public MoGram(List<S> solutions) {
        super();
        this.solutions = solutions;
    }

    abstract protected Graph<S, DefaultWeightedEdge> buildTree();

    public void exportSVG(String filePath) {
        Graph<S, DefaultWeightedEdge> graph = buildTree();

        // TODO Show Graph using JGraphX and color Nodes using the objective values
    }
}
