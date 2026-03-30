package uva.common.template.generic;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public class DirectedGraph<V> {
    private final Map<V, Set<V>> edges;

    public DirectedGraph(final List<V> vertices) {
        edges = new HashMap<>(vertices.size());
        for (final V vertex : vertices) {
            edges.putIfAbsent(vertex, new HashSet<>());
        }
    }

    public void add(final V from, final V into) {
        edges.computeIfAbsent(from, k -> new HashSet<>()).add(into);
    }

    public Set<V> get() {
        return edges.keySet();
    }

    public Set<V> get(final V from) {
        return edges.getOrDefault(from, Collections.emptySet());
    }
}