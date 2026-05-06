package uva.uhunt.c4.g8.p12668;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 12668 - Attacking rooks
 * Time limit: 20.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=4406
 */
public class Main {
    public static void main(final String... args) throws IOException {
        final BufferedIO io = new BufferedIO(System.in, System.out);
        final Process process = new Process();

        while (true) {
            final String boardSizeLine = io.readLine();
            final boolean isEOF = boardSizeLine == null;
            if (isEOF) break;

            final Input input = new Input();
            input.boardSize = Integer.parseInt(boardSizeLine);
            input.board = new char[input.boardSize][];
            for (int i = 0; i < input.boardSize; i++) {
                input.board[i] = io.readLine().toCharArray();
            }

            final Output output = process.process(input);
            io.write("%d\n", output.totalRooks);
        }

        io.close();
    }
}

class Input {
    public int boardSize;
    public char[][] board;
}

class Output {
    public int totalRooks;
}

class Process {
    private static final char EMPTY = '.';
    private static final char PAWN = 'X';
    private static final KuhnAlgorithm<String, Line[]> KUHN_ALGORITHM = new KuhnAlgorithm<>();

    public Output process(final Input input) {
        final List<Line> horizontalLines = findHorizontalLines(input.boardSize, input.board);
        final List<Line> verticalLines = findVerticalLines(input.boardSize, input.board);

        final UndirectedGraph<String, Line[]> graph = buildGraph(horizontalLines, verticalLines);
        final Set<String> horizontalVertices = convertToVertices(horizontalLines);
        final Set<String> verticalVertices = convertToVertices(verticalLines);

        final List<Line[]> matching = KUHN_ALGORITHM.findMaximumBipartiteMatching(
            graph,
            horizontalVertices,
            verticalVertices
        );

        final Output output = new Output();
        output.totalRooks = matching.size();
        return output;
    }

    private List<Line> findHorizontalLines(final int boardSize, final char[][] board) {
        final LinkedList<Line> lines = new LinkedList<>();

        for (int row = 0; row < boardSize; row++) {
            int minCol = 0, maxCol = 0;
            for (int col = 0; col <= boardSize; col++) {
                if (col == boardSize || board[row][col] == PAWN) {
                    maxCol = col - 1;
                    if (maxCol >= minCol) {
                        final Line line = new Line(row, minCol, row, maxCol);
                        lines.addLast(line);
                    }
                    minCol = col + 1;
                }
            }
        }

        return lines;
    }

    private List<Line> findVerticalLines(final int boardSize, final char[][] board) {
        final LinkedList<Line> lines = new LinkedList<>();

        for (int col = 0; col < boardSize; col++) {
            int minRow = 0, maxRow = 0;
            for (int row = 0; row <= boardSize; row++) {
                if (row == boardSize || board[row][col] == PAWN) {
                    maxRow = row - 1;
                    if (maxRow >= minRow) {
                        final Line line = new Line(minRow, col, maxRow, col);
                        lines.addLast(line);
                    }
                    minRow = row + 1;
                }
            }
        }

        return lines;
    }

    private boolean intersect(final Line horizontal, final Line vertical) {
        final int vx1 = vertical.p1.x;
        final int vx2 = vertical.p2.x;
        final int hx = horizontal.p1.x;
        final boolean intersect1 = vx1 <= hx && hx <= vx2;

        final int hy1 = horizontal.p1.y;
        final int hy2 = horizontal.p2.y;
        final int vy = vertical.p1.y;
        final boolean intersect2 = hy1 <= vy && vy <= hy2;

        return intersect1 && intersect2;
    }

    private UndirectedGraph<String, Line[]> buildGraph(final List<Line> horizontalLines, final List<Line> verticalLines) {
        final UndirectedGraph<String, Line[]> graph = new UndirectedGraph<>();

        for (final Line horizontalLine : horizontalLines) {
            for (final Line verticalLine : verticalLines) {
                if (intersect(horizontalLine, verticalLine)) {
                    final String horizontalVertex = horizontalLine.toString();
                    final String verticalVertex = verticalLine.toString();
                    final Line[] edge = new Line[]{horizontalLine, verticalLine};

                    graph.add(horizontalVertex, verticalVertex, edge);
                }
            }
        }

        return graph;
    }

    private Set<String> convertToVertices(final List<Line> lines) {
        return lines.stream().map(Line::toString).collect(Collectors.toSet());
    }
}

final class KuhnAlgorithm<V, E> {
    private final Map<V, V> matched = new HashMap<>();
    private final Set<V> used = new HashSet<>();

    public List<E> findMaximumBipartiteMatching(
        final UndirectedGraph<V, E> graph,
        final Set<V> leftVertices,
        final Set<V> rightVertices
    ) {
        matched.clear();
        for (final V vertex : leftVertices) {
            used.clear();
            depthFirstSearch(graph, vertex);
        }

        final List<E> list = new LinkedList<>();
        for (final Map.Entry<V, V> entry : matched.entrySet()) {
            final E edge = graph.get(entry.getKey(), entry.getValue()).get();
            list.add(edge);
        }

        return list;
    }

    private boolean depthFirstSearch(
        final UndirectedGraph<V, E> graph,
        final V vertex
    ) {
        if (used.contains(vertex)) {
            return false;
        }

        used.add(vertex);
        for (final V nextVertex : graph.get(vertex)) {
            final V matchedNextVertex = matched.get(nextVertex);
            final boolean matches = matchedNextVertex == null || depthFirstSearch(graph, matchedNextVertex);
            if (matches) {
                matched.put(nextVertex, vertex);
                return true;
            }
        }
        return false;
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

class Line {
    public final Point p1;
    public final Point p2;

    public Line(final int x1, final int y1, final int x2, final int y2) {
        this(new Point(x1, y1), new Point(x2, y2));
    }

    public Line(final Point p1, final Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public String toString() {
        return String.format("(%s,%s)", p1.toString(), p2.toString());
    }
}

class Point {
    public final int x;
    public final int y;

    public Point(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", x, y);
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
