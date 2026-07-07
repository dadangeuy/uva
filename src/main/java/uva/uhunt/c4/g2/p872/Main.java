package uva.uhunt.c4.g2.p872;

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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 872 - Ordering
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=813
 */
public class Main {
    public static void main(final String... args) throws IOException {
        final BufferedIO io = new BufferedIO(System.in, System.out);
        final Process process = new Process();

        final int totalCases = Integer.parseInt(io.readLine());
        for (int i = 0; i < totalCases; i++) {
            final Input input = new Input();
            input.variables = Arrays.asList(io.readLine(" "));
            input.constraints = Arrays.asList(io.readLine(" "));

            final Output output = process.process(input);
            if (i > 0) io.write("\n");
            if (output.orderings.isEmpty()) {
                io.write("NO\n");
            } else {
                for (final List<String> ordering : output.orderings) {
                    io.write("%s\n", String.join(" ", ordering));
                }
            }
        }

        io.close();
    }
}

class Input {
    public List<String> variables;
    public List<String> constraints;
}

class Output {
    public List<List<String>> orderings;
}

class Process {
    public Output process(final Input input) {
        final Output output = new Output();

        final Graph graph = new Graph();
        final Counts counts = new Counts();
        for (final String constraint : input.constraints) {
            final String[] variables = constraint.split("<");
            graph.add(variables[0], variables[1]);
            counts.increment(variables[1]);
        }

        output.orderings = permutationTopologicalSort(input.variables, graph, counts);

        return output;
    }

    private List<List<String>> permutationTopologicalSort(
            final List<String> variables,
            final Graph graph,
            final Counts counts
    ) {
        final Set<String> frees = new TreeSet<>();
        for (final String variable : variables) {
            if (counts.isZero(variable)) frees.add(variable);
        }

        final List<List<String>> orderings = new LinkedList<>();
        permutationTopologicalSort(
                variables,
                graph,
                counts,
                frees,
                new LinkedList<>(),
                orderings
        );

        return orderings;
    }

    private void permutationTopologicalSort(
            final List<String> variables,
            final Graph graph,
            final Counts counts,
            final Set<String> frees,
            final LinkedList<String> ordering,
            final List<List<String>> orderings
    ) {
        if (variables.size() == ordering.size()) {
            orderings.add(new ArrayList<>(ordering));
            return;
        }

        final List<String> listFrees = new ArrayList<>(frees);
        for (final String variable : listFrees) {
            // process variable
            frees.remove(variable);
            ordering.addLast(variable);
            final List<String> listNextVariables = new ArrayList<>(graph.get(variable));
            for (final String nextVariable : listNextVariables) {
                counts.decrement(nextVariable);
                if (counts.isZero(nextVariable)) frees.add(nextVariable);
            }

            // dfs ordering
            permutationTopologicalSort(
                    variables,
                    graph,
                    counts,
                    frees,
                    ordering,
                    orderings
            );

            // rollback variable
            for (final String nextVariable : listNextVariables) {
                if (counts.isZero(nextVariable)) frees.remove(nextVariable);
                counts.increment(nextVariable);
            }
            ordering.removeLast();
            frees.add(variable);
        }
    }
}

class Graph {
    private final Map<String, Set<String>> graph = new HashMap<>();

    public void add(final String key, final String value) {
        graph.computeIfAbsent(key, k -> new HashSet<>()).add(value);
    }

    public Set<String> get(final String key) {
        return graph.getOrDefault(key, Collections.emptySet());
    }
}

class Counts {
    private final Map<String, Integer> counts = new HashMap<>();

    public void increment(final String key) {
        counts.put(key, get(key) + 1);
    }

    public void decrement(final String key) {
        counts.put(key, get(key) - 1);
    }

    public int get(final String key) {
        return counts.getOrDefault(key, 0);
    }

    public boolean isZero(final String key) {
        return get(key) == 0;
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
