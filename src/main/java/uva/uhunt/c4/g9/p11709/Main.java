package uva.uhunt.c4.g9.p11709;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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

        final KosarajuAlgorithm<String> kosarajuAlgorithm = new KosarajuAlgorithm<>(vertices, edges);
        final List<List<String>> stronglyConnectedComponents = kosarajuAlgorithm.getStronglyConnectedComponents();

        output.totalGroups = stronglyConnectedComponents.size();

        return output;
    }
}

class KosarajuAlgorithm<V> {
    private final DirectedGraph<V> graph;
    private final DirectedGraph<V> transposeGraph;
    private final List<List<V>> stronglyConnectedComponents;

    public KosarajuAlgorithm(final List<V> vertices, final List<List<V>> edges) {
        this.graph = createGraph(vertices, edges);
        this.transposeGraph = createGraph(vertices, transpose(edges));
        this.stronglyConnectedComponents = findStronglyConnectedComponents();
    }

    private List<List<V>> transpose(final List<List<V>> edges) {
        return edges.stream()
            .map(edge -> Arrays.asList(edge.get(1), edge.get(0)))
            .collect(Collectors.toList());
    }

    private DirectedGraph<V> createGraph(final List<V> vertices, List<List<V>> edges) {
        final DirectedGraph<V> graph = new DirectedGraph<>(vertices);
        for (final List<V> edge : edges) {
            graph.add(edge.get(0), edge.get(1));
        }
        return graph;
    }

    private List<List<V>> findStronglyConnectedComponents() {
        final Set<V> visited = new HashSet<>();
        final LinkedList<V> sequence = new LinkedList<>();

        for (final V vertex : graph.get()) {
            if (visited.contains(vertex)) continue;
            dfs(graph, vertex, visited, sequence);
        }

        final List<List<V>> listSCC = new LinkedList<>();
        visited.clear();
        for (final Iterator<V> it = sequence.descendingIterator(); it.hasNext(); ) {
            final V vertex = it.next();
            if (visited.contains(vertex)) continue;

            final LinkedList<V> scc = new LinkedList<>();
            dfs(transposeGraph, vertex, visited, scc);
            listSCC.add(new ArrayList<>(scc));
        }

        return new ArrayList<>(listSCC);
    }

    private void dfs(
        final DirectedGraph<V> graph,
        final V vertex,
        final Set<V> visited,
        final LinkedList<V> sequence
    ) {
        if (visited.contains(vertex)) return;

        visited.add(vertex);
        graph.get(vertex).forEach(nextVertex -> dfs(graph, nextVertex, visited, sequence));
        sequence.addLast(vertex);
    }

    public List<List<V>> getStronglyConnectedComponents() {
        return stronglyConnectedComponents;
    }
}

class DirectedGraph<V> {
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
