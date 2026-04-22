package uva.common.template.generic;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("unused")
final class TarjanAlgorithm<V, E> {
    private final UndirectedGraph<V, E> graph;
    private final Map<V, Integer> discoveries;
    private final Map<V, Integer> lows;
    private final Map<V, V> parents;
    private int time;
    private final List<E> bridges;

    public TarjanAlgorithm(final UndirectedGraph<V, E> graph) {
        this.graph = graph;
        this.discoveries = new HashMap<>();
        this.lows = new HashMap<>();
        this.parents = new HashMap<>();
        this.time = 0;
        this.bridges = new LinkedList<>();
    }

    public List<E> findBridges() {
        for (final V vertex : graph.get()) {
            if (!discoveries.containsKey(vertex)) {
                depthFirstSearch(vertex);
            }
        }
        return bridges;
    }

    private void depthFirstSearch(final V vertex) {
        ++time;
        discoveries.put(vertex, time);
        lows.put(vertex, time);

        for (final V nextVertex : graph.get(vertex)) {
            final boolean isDiscovered = discoveries.containsKey(nextVertex);
            final boolean isParent = Objects.equals(nextVertex, parents.get(vertex));

            if (!isDiscovered) {
                parents.put(nextVertex, vertex);
                depthFirstSearch(nextVertex);

                final int low = Math.min(lows.get(vertex), lows.get(nextVertex));
                lows.put(vertex, low);

                if (lows.get(nextVertex) > discoveries.get(vertex))  {
                    final E edge = graph
                        .get(vertex, nextVertex)
                        .orElseThrow(NullPointerException::new);
                    bridges.add(edge);
                }
            } else if (!isParent) {
                final int low = Math.min(lows.get(vertex), discoveries.get(nextVertex));
                lows.put(vertex, low);
            }
        }
    }
}
