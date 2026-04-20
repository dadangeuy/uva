package uva.common.template.generic;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
final class UndirectedGraph<V> {
    public final Map<V, Set<V>> edges = new HashMap<>();

    public void add(final V vertex1, final V vertex2) {
        addDirected(vertex1, vertex2);
        addDirected(vertex2, vertex1);
    }

    private void addDirected(final V fromVertex, final V intoVertex) {
        edges.computeIfAbsent(fromVertex, k -> new HashSet<>()).add(intoVertex);
    }

    public Set<V> get() {
        return edges.keySet();
    }

    public Set<V> get(final V fromVertex) {
        return edges.getOrDefault(fromVertex, Collections.emptySet());
    }
}
