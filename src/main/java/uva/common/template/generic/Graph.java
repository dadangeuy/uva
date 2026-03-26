package uva.common.template.generic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("unused")
final class Graph<V, E> {
    public final Map<V, Map<V, E>> edges = new HashMap<>();

    public void addBi(final V vertex1, final V vertex2, final E edge) {
        addUni(vertex1, vertex2, edge);
        addUni(vertex2, vertex1, edge);
    }

    public void addUni(final V fromVertex, final V intoVertex, final E edge) {
        edges.computeIfAbsent(fromVertex, k -> new HashMap<>()).put(intoVertex, edge);
    }

    public Set<V> get() {
        return edges.keySet();
    }

    public Set<V> get(final V fromVertex) {
        return edges.getOrDefault(fromVertex, Collections.emptyMap()).keySet();
    }

    public Optional<E> get(final V fromVertex, final V intoVertex) {
        return Optional.ofNullable(edges.getOrDefault(fromVertex, Collections.emptyMap()).get(intoVertex));
    }
}