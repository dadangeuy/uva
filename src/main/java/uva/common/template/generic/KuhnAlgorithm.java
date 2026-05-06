package uva.common.template.generic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
final class KuhnAlgorithm<V, E> {
    private final Map<V, V> matched = new HashMap<>();
    private final Set<V> used = new HashSet<>();

    public List<E> findMaximumBipartiteMatching(
        final UndirectedGraph<V, E> graph,
        final Set<V> leftVertices,
        final Set<V> rightVertices
    ) {
        matched.clear();
        for (final V vertex : leftVertices) {
            used.clear();
            depthFirstSearch(graph, vertex);
        }

        final List<E> list = new LinkedList<>();
        for (final Map.Entry<V, V> entry : matched.entrySet()) {
            final E edge = graph.get(entry.getKey(), entry.getValue()).get();
            list.add(edge);
        }

        return list;
    }

    private boolean depthFirstSearch(
        final UndirectedGraph<V, E> graph,
        final V vertex
    ) {
        if (used.contains(vertex)) {
            return false;
        }

        used.add(vertex);
        for (final V nextVertex : graph.get(vertex)) {
            final V matchedNextVertex = matched.get(nextVertex);
            final boolean matches = matchedNextVertex == null || depthFirstSearch(graph, matchedNextVertex);
            if (matches) {
                matched.put(nextVertex, vertex);
                return true;
            }
        }
        return false;
    }
}
