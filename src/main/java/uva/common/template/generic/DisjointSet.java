package uva.common.template.generic;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
final class DisjointSet<V> {
    private final Map<V, V> parentMap = new HashMap<>();

    public V find(final V child) {
        final V parent = parentMap.getOrDefault(child, child);
        if (parent == child) {
            return parent;
        } else {
            final V grandparent = find(parent);
            parentMap.put(child, grandparent);
            return grandparent;
        }
    }

    public void union(final V child1, final V child2) {
        final V parent1 = find(child1);
        final V parent2 = find(child2);
        parentMap.put(parent2, parent1);
    }
}
