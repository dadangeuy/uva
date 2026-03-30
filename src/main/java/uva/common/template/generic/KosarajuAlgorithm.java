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
class KosarajuAlgorithm<V> {
    private final DirectedGraph<V> graph;
    private final DirectedGraph<V> transposeGraph;
    private final List<List<V>> stronglyConnectedComponents;

    public KosarajuAlgorithm(final List<V> vertices, final List<List<V>> edges) {
        this.graph = createGraph(vertices, edges);
        this.transposeGraph = createGraph(vertices, transpose(edges));
        this.stronglyConnectedComponents = findStronglyConnectedComponents();
    }

    private List<List<V>> transpose(final List<List<V>> edges) {
        return edges.stream()
            .map(edge -> Arrays.asList(edge.get(1), edge.get(0)))
            .collect(Collectors.toList());
    }

    private DirectedGraph<V> createGraph(final List<V> vertices, List<List<V>> edges) {
        final DirectedGraph<V> graph = new DirectedGraph<>(vertices);
        for (final List<V> edge : edges) {
            graph.add(edge.get(0), edge.get(1));
        }
        return graph;
    }

    private List<List<V>> findStronglyConnectedComponents() {
        final Set<V> visited = new HashSet<>();
        final LinkedList<V> sequence = new LinkedList<>();

        for (final V vertex : graph.get()) {
            if (visited.contains(vertex)) continue;
            dfs(graph, vertex, visited, sequence);
        }

        final List<List<V>> listSCC = new LinkedList<>();
        visited.clear();
        for (final Iterator<V> it = sequence.descendingIterator(); it.hasNext(); ) {
            final V vertex = it.next();
            if (visited.contains(vertex)) continue;

            final LinkedList<V> scc = new LinkedList<>();
            dfs(transposeGraph, vertex, visited, scc);
            listSCC.add(new ArrayList<>(scc));
        }

        return new ArrayList<>(listSCC);
    }

    private void dfs(
        final DirectedGraph<V> graph,
        final V vertex,
        final Set<V> visited,
        final LinkedList<V> sequence
    ) {
        if (visited.contains(vertex)) return;

        visited.add(vertex);
        graph.get(vertex).forEach(nextVertex -> dfs(graph, nextVertex, visited, sequence));
        sequence.addLast(vertex);
    }

    public List<List<V>> getStronglyConnectedComponents() {
        return stronglyConnectedComponents;
    }
}
