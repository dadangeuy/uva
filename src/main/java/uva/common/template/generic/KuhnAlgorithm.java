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

    public List<List<V>> findMaximumBipartiteMatching(final UndirectedGraph<V, E> graph) {
        return findMaximumBipartiteMatching(graph, graph.get());
    }

    public List<List<V>> findMaximumBipartiteMatching(final UndirectedGraph<V, E> graph, final Set<V> vertices) {
        matched.clear();
        for (final V vertex : vertices) {
            used.clear();
            depthFirstSearch(graph, vertex);
        }

        final List<List<V>> list = new LinkedList<>();
        for (final Map.Entry<V, V> entry : matched.entrySet()) {
            list.add(Arrays.asList(entry.getKey(), entry.getValue()));
        }

        return list;
    }

    private boolean depthFirstSearch(final UndirectedGraph<V, E> graph, final V vertex) {
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
