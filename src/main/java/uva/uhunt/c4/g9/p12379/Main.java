package uva.uhunt.c4.g9.p12379;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

/**
 * 12379 - Central Post Office
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=3801
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        final int totalCases = in.nextInt();
        for (int i = 0; i < totalCases; i++) {
            final Input input = new Input();
            input.totalCities = in.nextInt();
            input.adjacentCities = new int[input.totalCities + 1][];
            for (int city = 1; city <= input.totalCities; city++) {
                final int totalAdjacentCities = in.nextInt();
                input.adjacentCities[city] = new int[totalAdjacentCities];
                for (int j = 0; j < totalAdjacentCities; j++) {
                    input.adjacentCities[city][j] = in.nextInt();
                }
            }

            final Output output = process.process(input);
            out.println(output.dispatchingTime);
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int totalCities;
    public int[][] adjacentCities;
}

class Output {
    public int dispatchingTime;
}

class Process {
    public Output process(final Input input) {
        final UndirectedGraph<Integer> graph = createGraph(input.totalCities, input.adjacentCities);
        final Path farthestPath = findFarthestPathInMST(graph);
        final int dispatchingTime = findDispatchingTime(input.totalCities, farthestPath);

        final Output output = new Output();
        output.dispatchingTime = dispatchingTime;
        return output;
    }

    private UndirectedGraph<Integer> createGraph(final int totalCities, final int[][] adjacentCities) {
        final UndirectedGraph<Integer> graph = new UndirectedGraph<>();
        for (int city = 1; city <= totalCities; city++) {
            for (int adjacentCity : adjacentCities[city]) {
                graph.add(city, adjacentCity);
            }
        }
        return graph;
    }

    private Path findFarthestPathInMST(final UndirectedGraph<Integer> graph) {
        final Path farthestPath1 = findFarthestPath(graph, 1);
        final Path farthestPath2 = findFarthestPath(graph, farthestPath1.vertex);

        return farthestPath2;
    }

    private Path findFarthestPath(final UndirectedGraph<Integer> graph, final int originVertex) {
        final Queue<Path> pathq = new LinkedList<>();
        final Set<Integer> visited = new HashSet<>();

        final Path initial = new Path(originVertex);
        pathq.add(initial);
        visited.add(initial.vertex);

        Path last = initial;

        while (!pathq.isEmpty()) {
            final Path current = pathq.remove();
            last = current;

            for (final int nextVertex : graph.get(current.vertex)) {
                if (!visited.contains(nextVertex)) {
                    final Path next = current.next(nextVertex);
                    pathq.add(next);
                    visited.add(next.vertex);
                }
            }
        }

        return last;
    }

    private int findDispatchingTime(final int totalVertices, final Path farthestPath) {
        final int totalEdges = totalVertices - 1;
        final int totalFarthestEdges = farthestPath.distance;
        return totalEdges * 2 - totalFarthestEdges;
    }
}

final class UndirectedGraph<V> {
    public final Map<V, Set<V>> edges = new HashMap<>();

    public void add(final V vertex1, final V vertex2) {
        addDirected(vertex1, vertex2);
        addDirected(vertex2, vertex1);
    }

    private void addDirected(final V fromVertex, final V intoVertex) {
        edges.computeIfAbsent(fromVertex, k -> new HashSet<>()).add(intoVertex);
    }

    public Set<V> get() {
        return edges.keySet();
    }

    public Set<V> get(final V fromVertex) {
        return edges.getOrDefault(fromVertex, Collections.emptySet());
    }
}

class Path {
    public final Path previous;
    public final int vertex;
    public final int distance;

    public Path(final int vertex) {
        this(null, vertex, 0);
    }

    private Path(final Path previous, final int vertex, final int distance) {
        this.previous = previous;
        this.vertex = vertex;
        this.distance = distance;
    }

    public Path next(final int vertex) {
        return new Path(this, vertex, distance + 1);
    }
}
