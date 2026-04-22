package uva.common.template.generic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
final class KosarajuAlgorithm<V, E> {
    private final DirectedGraph<V, E> graph;
    private final DirectedGraph<V, E> transposeGraph;
    private final List<V> vertices;

    public KosarajuAlgorithm(final DirectedGraph<V, E> graph, final List<V> vertices) {
        this.graph = graph;
        this.transposeGraph = transposeGraph(graph);
        this.vertices = vertices;
    }

    private DirectedGraph<V, E> transposeGraph(final DirectedGraph<V, E> graph) {
        final DirectedGraph<V, E> transpose = new DirectedGraph<>();
        for (final V fromVertex : graph.get()) {
            for (final V intoVertex : graph.get(fromVertex)) {
                final E edge = graph.get(fromVertex, intoVertex).orElseThrow(NullPointerException::new);
                transpose.add(intoVertex, fromVertex, edge);
            }
        }

        return transpose;
    }

    public List<List<V>> findStronglyConnectedComponents() {
        final Set<V> visited = new HashSet<>();
        final LinkedList<V> sequence = new LinkedList<>();

        for (final V vertex : vertices) {
            if (visited.contains(vertex)) continue;

            depthFirstSearch(graph, vertex, visited, sequence);
        }

        final List<List<V>> stronglyConnectedComponents = new LinkedList<>();
        visited.clear();
        for (final Iterator<V> it = sequence.descendingIterator(); it.hasNext(); ) {
            final V vertex = it.next();
            if (visited.contains(vertex)) continue;

            final LinkedList<V> stronglyConnectedComponent = new LinkedList<>();
            depthFirstSearch(transposeGraph, vertex, visited, stronglyConnectedComponent);
            stronglyConnectedComponents.add(stronglyConnectedComponent);
        }

        return stronglyConnectedComponents;
    }

    private void depthFirstSearch(
        final DirectedGraph<V, E> graph,
        final V vertex,
        final Set<V> visited,
        final LinkedList<V> sequence
    ) {
        if (visited.contains(vertex)) return;

        visited.add(vertex);
        for (final V nextVertex : graph.get(vertex)) {
            depthFirstSearch(graph, nextVertex, visited, sequence);
        }
        sequence.addLast(vertex);
    }
}
