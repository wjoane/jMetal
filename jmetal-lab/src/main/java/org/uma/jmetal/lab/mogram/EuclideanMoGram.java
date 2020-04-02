package org.uma.jmetal.lab.mogram;

import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.distance.impl.EuclideanDistanceBetweenSolutionsInSolutionSpace;

public class EuclideanMoGram extends MoGram<DoubleSolution> {

    public EuclideanMoGram(List<DoubleSolution> solutions) {
        super(solutions);
        distance = new EuclideanDistanceBetweenSolutionsInSolutionSpace<DoubleSolution>();
    }

    @Override
    protected Graph<DoubleSolution, DefaultWeightedEdge> buildTree() {
        Graph<DoubleSolution, DefaultWeightedEdge> graph = new SimpleWeightedGraph<DoubleSolution, DefaultWeightedEdge>(
                DefaultWeightedEdge.class);

        solutions.forEach(graph::addVertex);

        for (DoubleSolution a : solutions) {
            for (DoubleSolution b : solutions) {
                DefaultWeightedEdge edge = graph.addEdge(a, b);
                graph.setEdgeWeight(edge, distance.getDistance(a, b));
            }
        }

        SpanningTreeAlgorithm<DefaultWeightedEdge> spanningTreeAlgorithm = new KruskalMinimumSpanningTree<DoubleSolution, DefaultWeightedEdge>(
                graph);

        Set<DefaultWeightedEdge> spanningTreeEdges = spanningTreeAlgorithm.getSpanningTree().getEdges();
        Set<DefaultWeightedEdge> allEdges = graph.edgeSet();

        for (DefaultWeightedEdge edge : allEdges) {
            if (!spanningTreeEdges.contains(edge)) {
                graph.removeEdge(edge);
            }

        }

        return graph;
    }
}