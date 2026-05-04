package uva.uhunt.c4.g0.p11770;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 11770 - Lighting Away
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2870
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        final int totalCases = in.nextInt();
        for (int i = 0; i < totalCases; i++) {
            final Input input = new Input();
            input.caseId = i + 1;
            input.totalLights = in.nextInt();
            input.totalReactions = in.nextInt();
            input.reactions = new int[input.totalReactions][2];
            for (int j = 0; j < input.totalReactions; j++) {
                input.reactions[j][0] = in.nextInt();
                input.reactions[j][1] = in.nextInt();
            }

            final Output output = process.process(input);
            out.format("Case %d: %d\n", output.caseId, output.totalManualLights);
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int caseId;
    public int totalLights;
    public int totalReactions;
    public int[][] reactions;
}

class Output {
    public int caseId;
    public int totalManualLights;
}

class Process {
    public Output process(final Input input) {
        final DirectedGraph<Integer, int[]> graph = createGraph(input);
        final List<Integer> vertices = createVertices(input);

        final KosarajuAlgorithm<Integer, int[]> kosaraju = new KosarajuAlgorithm<>(graph, vertices);
        final List<List<Integer>> stronglyConnectedComponents = kosaraju.findStronglyConnectedComponents();

        final DirectedGraph<Integer, int[]> sccGraph = createSccGraph(input, stronglyConnectedComponents);
        final List<Integer> sccVertices = createSccVertices(stronglyConnectedComponents);

        final Map<Integer, Integer> inDegrees = countInDegrees(sccGraph, sccVertices);
        final List<Integer> zeroVertices = sccVertices.stream()
            .filter(vertex -> inDegrees.getOrDefault(vertex, 0) == 0)
            .collect(Collectors.toList());

        final Output output = new Output();
        output.caseId = input.caseId;
        output.totalManualLights = zeroVertices.size();
        return output;
    }

    private DirectedGraph<Integer, int[]> createGraph(final Input input) {
        final DirectedGraph<Integer, int[]> graph = new DirectedGraph<>();
        for (final int[] reaction : input.reactions) {
            final int light1 = reaction[0], light2 = reaction[1];
            graph.add(light1, light2, reaction);
        }
        return graph;
    }

    private List<Integer> createVertices(final Input input) {
        return IntStream.rangeClosed(1, input.totalLights)
            .boxed()
            .collect(Collectors.toList());
    }

    private DirectedGraph<Integer, int[]> createSccGraph(
        final Input input,
        final List<List<Integer>> stronglyConnectedComponents
    ) {
        final int[] condensedVertices = new int[input.totalLights + 1];
        final Iterator<List<Integer>> it = stronglyConnectedComponents.iterator();
        for (int condensedVertex = 1; it.hasNext(); condensedVertex++) {
            final List<Integer> stronglyConnectedComponent = it.next();
            for (final int vertex : stronglyConnectedComponent) {
                condensedVertices[vertex] = condensedVertex;
            }
        }

        final DirectedGraph<Integer, int[]> graph = new DirectedGraph<>();
        for (final int[] reaction : input.reactions) {
            final int vertex1 = condensedVertices[reaction[0]];
            final int vertex2 = condensedVertices[reaction[1]];
            graph.add(vertex1, vertex2, reaction);
        }
        return graph;
    }

    private List<Integer> createSccVertices(
        final List<List<Integer>> stronglyConnectedComponents
    ) {
        return IntStream.rangeClosed(1, stronglyConnectedComponents.size())
            .boxed()
            .collect(Collectors.toList());
    }

    private Map<Integer, Integer> countInDegrees(
        final DirectedGraph<Integer, int[]> graph,
        final List<Integer> vertices
    ) {
        final Map<Integer, Integer> inDegrees = new HashMap<>();
        for (final int vertex : vertices) {
            for (final int nextVertex : graph.get(vertex)) {
                if (vertex == nextVertex) continue;
                inDegrees.put(nextVertex, inDegrees.getOrDefault(nextVertex, 0) + 1);
            }
        }
        return inDegrees;
    }
}

final class KosarajuAlgorithm<V, E> {
    private final DirectedGraph<V, E> graph;
    private final DirectedGraph<V, E> transposeGraph;
    private final List<V> vertices;

    public KosarajuAlgorithm(final DirectedGraph<V, E> graph, final List<V> vertices) {
        this.graph = graph;
        this.transposeGraph = transposeGraph(graph);
        this.vertices = vertices;
    }

    private DirectedGraph<V, E> transposeGraph(final DirectedGraph<V, E> graph) {
        final DirectedGraph<V, E> transpose = new DirectedGraph<>();
        for (final V fromVertex : graph.get()) {
            for (final V intoVertex : graph.get(fromVertex)) {
                final E edge = graph.get(fromVertex, intoVertex).orElseThrow(NullPointerException::new);
                transpose.add(intoVertex, fromVertex, edge);
            }
        }

        return transpose;
    }

    public List<List<V>> findStronglyConnectedComponents() {
        final Set<V> visited = new HashSet<>();
        final LinkedList<V> sequence = new LinkedList<>();

        for (final V vertex : vertices) {
            if (visited.contains(vertex)) continue;

            depthFirstSearch(graph, vertex, visited, sequence);
        }

        final List<List<V>> stronglyConnectedComponents = new LinkedList<>();
        visited.clear();
        for (final Iterator<V> it = sequence.descendingIterator(); it.hasNext(); ) {
            final V vertex = it.next();
            if (visited.contains(vertex)) continue;

            final LinkedList<V> stronglyConnectedComponent = new LinkedList<>();
            depthFirstSearch(transposeGraph, vertex, visited, stronglyConnectedComponent);
            stronglyConnectedComponents.add(stronglyConnectedComponent);
        }

        return stronglyConnectedComponents;
    }

    private void depthFirstSearch(
        final DirectedGraph<V, E> graph,
        final V vertex,
        final Set<V> visited,
        final LinkedList<V> sequence
    ) {
        if (visited.contains(vertex)) return;

        visited.add(vertex);
        for (final V nextVertex : graph.get(vertex)) {
            depthFirstSearch(graph, nextVertex, visited, sequence);
        }
        sequence.addLast(vertex);
    }
}

final class DirectedGraph<V, E> {
    private final Map<V, Map<V, E>> edges = new HashMap<>();

    public void add(final V fromVertex, final V intoVertex, final E edge) {
        edges
            .computeIfAbsent(fromVertex, k -> new HashMap<>())
            .put(intoVertex, edge);
    }

    public Set<V> get() {
        return edges.keySet();
    }

    public Set<V> get(final V vertex) {
        return edges
            .getOrDefault(vertex, Collections.emptyMap())
            .keySet();
    }

    public Optional<E> get(final V vertex1, final V vertex2) {
        return Optional.ofNullable(
            edges
                .getOrDefault(vertex1, Collections.emptyMap())
                .get(vertex2)
        );
    }
}
