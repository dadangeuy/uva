package uva.uhunt.c4.g9.p11709;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 11709 - Trust groups
 * Time limit: 5.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2756
 */
public class Main {
    public static void main(final String... args) throws IOException {
        final BufferedIO io = new BufferedIO(System.in, System.out);
        final Process process = new Process();

        while (true) {
            final Input input = new Input();

            final String[] l1 = io.readLine(" ");
            input.totalPeoples = Integer.parseInt(l1[0]);
            input.totalTrusts = Integer.parseInt(l1[1]);
            if (input.isEOF()) break;

            input.peoples = new String[input.totalPeoples];
            for (int i = 0; i < input.totalPeoples; i++) {
                input.peoples[i] = io.readLine();
            }

            input.trusts = new String[input.totalTrusts][2];
            for (int i = 0; i < input.totalTrusts; i++) {
                input.trusts[i][0] = io.readLine();
                input.trusts[i][1] = io.readLine();
            }

            final Output output = process.process(input);
            io.write("%d\n", output.totalGroups);
        }

        io.close();
    }
}

class Input {
    public int totalPeoples;
    public int totalTrusts;
    public String[] peoples;
    public String[][] trusts;

    public boolean isEOF() {
        return totalPeoples == 0 && totalTrusts == 0;
    }
}

class Output {
    public int totalGroups;
}

class Process {
    public Output process(final Input input) {
        final Output output = new Output();

        final List<String> vertices = Arrays.stream(input.peoples).collect(Collectors.toList());
        final List<List<String>> edges = Arrays.stream(input.trusts).map(Arrays::asList).collect(Collectors.toList());

        final DirectedGraph<String, List<String>> graph = new DirectedGraph<>();
        for (final List<String> edge : edges) {
            final String fromVertex = edge.get(0);
            final String intoVertex = edge.get(1);
            graph.add(fromVertex, intoVertex, edge);
        }

        final KosarajuAlgorithm<String, List<String>> kosarajuAlgorithm = new KosarajuAlgorithm<>(graph, vertices);
        final List<List<String>> stronglyConnectedComponents = kosarajuAlgorithm.findStronglyConnectedComponents();

        output.totalGroups = stronglyConnectedComponents.size();

        return output;
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

final class BufferedIO {
    private final BufferedReader in;
    private final BufferedWriter out;

    public BufferedIO(final InputStream in, final OutputStream out) {
        this.in = new BufferedReader(new InputStreamReader(in));
        this.out = new BufferedWriter(new OutputStreamWriter(out));
    }

    public String[] readLine(final String separator) throws IOException {
        final String line = readLine();
        return line == null ? null : line.split(separator);
    }

    public String readLine() throws IOException {
        String line = in.readLine();
        while (line != null && line.isEmpty()) line = in.readLine();
        return line;
    }

    public void write(final String format, Object... args) throws IOException {
        final String string = String.format(format, args);
        write(string);
    }

    public void write(final String string) throws IOException {
        out.write(string);
    }

    public void close() throws IOException {
        in.close();
        out.flush();
        out.close();
    }
}
