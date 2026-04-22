package uva.common.template.generic;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("unused")
final class UndirectedGraph<V, E> {
    public final Map<V, Map<V, E>> edges = new HashMap<>();

    public void add(final V vertex1, final V vertex2, final E edge) {
        addDirected(vertex1, vertex2, edge);
        addDirected(vertex2, vertex1, edge);
    }

    private void addDirected(final V fromVertex, final V intoVertex, final E edge) {
        edges.computeIfAbsent(fromVertex, k -> new HashMap<>()).put(intoVertex, edge);
    }

    public Set<V> get() {
        return edges.keySet();
    }

    public Set<V> get(final V vertex) {
        return edges.getOrDefault(vertex, Collections.emptyMap()).keySet();
    }

    public Optional<E> get(final V vertex1, final V vertex2) {
        return Optional.ofNullable(edges.getOrDefault(vertex1, Collections.emptyMap()).get(vertex2));
    }
}
