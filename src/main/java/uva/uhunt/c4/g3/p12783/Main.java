package uva.uhunt.c4.g3.p12783;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

/**
 * 12783 - Weak Links
 * Time limit: 2.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=4648
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        while (true) {
            final Input input = new Input();
            input.totalPolices = in.nextInt();
            input.totalCommunications = in.nextInt();
            if (input.isEOF()) break;
            input.communications = new int[input.totalCommunications][2];
            for (int i = 0; i < input.totalCommunications; i++) {
                input.communications[i][0] = in.nextInt();
                input.communications[i][1] = in.nextInt();
            }

            final Output output = process.process(input);
            out.print(output.totalWeakLinks);
            for (final int[] weakLink : output.weakLinks) {
                for (final int police : weakLink) {
                    out.print(' ');
                    out.print(police);
                }
            }
            out.println();
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int totalPolices;
    public int totalCommunications;
    public int[][] communications;

    public boolean isEOF() {
        return totalPolices == 0 && totalCommunications == 0;
    }
}

class Output {
    public int totalWeakLinks;
    public int[][] weakLinks;
}

class Process {
    private static final Comparator<int[]> ORDER_EDGES_BY_VERTICES = Comparator
        .comparingInt((int[] e) -> e[0])
        .thenComparingInt((int[] e) -> e[1]);

    public Output process(final Input input) {
        final UndirectedGraph<Integer, int[]> graph = createGraph(input);
        final List<int[]> bridges = findBridges(graph);

        final Output output = new Output();
        output.weakLinks = convertToWeakLinks(bridges);
        output.totalWeakLinks = output.weakLinks.length;
        return output;
    }

    private UndirectedGraph<Integer, int[]> createGraph(final Input input) {
        final UndirectedGraph<Integer, int[]> graph = new UndirectedGraph<>();
        for (final int[] communication : input.communications) {
            final int minVertex = Math.min(communication[0], communication[1]);
            final int maxVertex = Math.max(communication[0], communication[1]);
            final int[] edge = new int[]{minVertex, maxVertex};
            graph.add(minVertex, maxVertex, edge);
        }
        return graph;
    }

    private List<int[]> findBridges(final UndirectedGraph<Integer, int[]> graph) {
        final TarjanAlgorithm<Integer, int[]> tarjanAlgorithm = new TarjanAlgorithm<>(graph);
        return tarjanAlgorithm.findBridges();
    }

    private int[][] convertToWeakLinks(final List<int[]> bridges) {
        return bridges.stream()
            .sorted(ORDER_EDGES_BY_VERTICES)
            .toArray(int[][]::new);
    }
}

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
