package uva.uhunt.c3.g1.p12841;

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
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

/**
 * 12841 - In Puzzleland (III)
 * Time limit: 1.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=4706
 */
public class Main {
    public static void main(final String... args) throws IOException {
        final BufferedIO io = new BufferedIO(System.in, System.out);
        final Process process = new Process();

        final int totalCases = Integer.parseInt(io.readLine());
        for (int i = 0; i < totalCases; i++) {
            final Input input = new Input();
            input.caseId = i + 1;
            final String[] line1 = io.readLine(" ");
            input.totalVertices = Integer.parseInt(line1[0]);
            input.totalEdges = Integer.parseInt(line1[1]);
            final String[] line2 = io.readLine(" ");
            input.vertices = new char[input.totalVertices];
            for (int j = 0; j < input.totalVertices; j++) {
                input.vertices[j] = line2[j].charAt(0);
            }
            input.edges = new char[input.totalEdges][2];
            for (int j = 0; j < input.totalEdges; j++) {
                final String[] line3 = io.readLine(" ");
                input.edges[j][0] = line3[0].charAt(0);
                input.edges[j][1] = line3[1].charAt(0);
            }

            final Output output = process.process(input);
            if (output.isPossible) {
                io.write("Case %d: %s\n", output.caseId, new String(output.path));
            } else {
                io.write("Case %d: impossible\n", output.caseId);
            }
        }

        io.close();
    }
}

class Input {
    public int caseId;
    public int totalVertices;
    public int totalEdges;
    public char[] vertices;
    public char[][] edges;
}

class Output {
    public int caseId;
    public boolean isPossible;
    public char[] path;
}

class Process {
    public Output process(final Input input) {
        final Output output = new Output();
        output.caseId = input.caseId;

        final VertexTranslator translator = new VertexTranslator(input.vertices);
        final int[] vertices = translator.translate(input.vertices);
        final int[][] edges = translator.translate(input.edges);
        final boolean[][] graph = createGraph(vertices.length, edges);

        final int origin = vertices[0];
        final int destination = vertices[vertices.length - 1];
        final DynamicProgrammingAlgorithm algorithm = new DynamicProgrammingAlgorithm(graph, input.totalVertices);
        final Optional<int[]> fullPath = algorithm.findHamiltonianPath(origin, destination);

        output.isPossible = fullPath.isPresent();
        output.path = output.isPossible ? translator.translate(fullPath.get()) : null;
        return output;
    }

    private boolean[][] createGraph(final int totalVertices, final int[][] edges) {
        final boolean[][] graph = new boolean[totalVertices][totalVertices];
        for (int[] edge : edges) {
            graph[edge[0]][edge[1]] = true;
            graph[edge[1]][edge[0]] = true;
        }
        return graph;
    }
}

class DynamicProgrammingAlgorithm {
    private static final int[] EMPTY_PATH = new int[0];
    private final boolean[][] graph;
    private final int totalVertices;
    private final int fullMask;
    private final int[][][] memo;

    public DynamicProgrammingAlgorithm(
        final boolean[][] graph,
        final int totalVertices
    ) {
        this.graph = graph;
        this.totalVertices = totalVertices;
        this.fullMask = fullMask(totalVertices);
        this.memo = new int[fullMask + 1][totalVertices][];
    }

    public Optional<int[]> findHamiltonianPath(
        final int origin,
        final int destination
    ) {
        final LinkedList<Integer> path = new LinkedList<>(Collections.singletonList(origin));
        final int[] fullPath = findHamiltonianPath(mask(origin), origin, destination, path);
        return fullPath == EMPTY_PATH ? Optional.empty() : Optional.of(fullPath);
    }

    private int[] findHamiltonianPath(
        final int mask,
        final int origin,
        final int destination,
        final LinkedList<Integer> path
    ) {
        if (memo[mask][origin] != null) {
            return memo[mask][origin];
        }

        if (mask == fullMask) {
            if (origin == destination) {
                final int[] completePath = path.stream().mapToInt(Integer::intValue).toArray();
                return memo[mask][origin] = completePath;
            } else {
                return memo[mask][origin] = EMPTY_PATH;
            }
        }

        int[] completePath = EMPTY_PATH;
        for (int next = 0; next < totalVertices; next++) {
            if (contains(mask, next)) continue;
            if (!graph[origin][next]) continue;

            final int nextMask = mask | mask(next);
            path.addLast(next);
            completePath = findHamiltonianPath(nextMask, next, destination, path);
            path.removeLast();

            if (completePath != EMPTY_PATH) break;
        }

        return memo[mask][origin] = completePath;
    }

    private boolean contains(final int mask, final int item) {
        return ((mask >> item) & 1) > 0;
    }

    private int mask(final int item) {
        return 1 << item;
    }

    private int fullMask(final int totalItems) {
        return (1 << totalItems) - 1;
    }
}

final class VertexTranslator {
    private final Map<Character, Integer> translations1 = new HashMap<>();
    private final Map<Integer, Character> translations2 = new HashMap<>();

    public VertexTranslator(final char[] letters) {
        final char[] sorted = letters.clone();
        Arrays.sort(sorted);
        for (int i = 0; i < sorted.length; i++) {
            add(sorted[i], i);
        }
    }

    private void add(final char letter, final int number) {
        translations1.put(letter, number);
        translations2.put(number, letter);
    }

    public int translate(final char letter) {
        return translations1.get(letter);
    }

    public char translate(final int number) {
        return translations2.get(number);
    }

    public int[] translate(final char[] letters) {
        final int[] translated = new int[letters.length];
        for (int i = 0; i < letters.length; i++) {
            translated[i] = translate(letters[i]);
        }
        return translated;
    }

    public char[] translate(final int[] numbers) {
        final char[] translated = new char[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            translated[i] = translate(numbers[i]);
        }
        return translated;
    }

    public int[][] translate(final char[][] letters) {
        final int[][] translated = new int[letters.length][];
        for (int i = 0; i < letters.length; i++) {
            translated[i] = new int[letters[i].length];
            for (int j = 0; j < letters[i].length; j++) {
                translated[i][j] = translate(letters[i][j]);
            }
        }
        return translated;
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
