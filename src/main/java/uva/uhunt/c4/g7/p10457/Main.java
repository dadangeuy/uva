package uva.uhunt.c4.g7.p10457;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * 10457 - Magic Car
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1398
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        while (in.hasNextInt()) {
            final Input input = new Input();
            input.totalJunctions = in.nextInt();
            input.totalRoads = in.nextInt();
            input.roads = new int[input.totalRoads][3];
            for (int i = 0; i < input.totalRoads; i++) {
                input.roads[i][0] = in.nextInt();
                input.roads[i][1] = in.nextInt();
                input.roads[i][2] = in.nextInt();
            }
            input.startEnergy = in.nextInt();
            input.stopEnergy = in.nextInt();
            input.totalQueries = in.nextInt();
            input.queries = new int[input.totalQueries][2];
            for (int i = 0; i < input.totalQueries; i++) {
                input.queries[i][0] = in.nextInt();
                input.queries[i][1] = in.nextInt();
            }

            final Output output = process.process(input);
            for (final int answer : output.answer) {
                out.println(answer);
            }
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int totalJunctions;
    public int totalRoads;
    public int[][] roads;
    public int startEnergy;
    public int stopEnergy;
    public int totalQueries;
    public int[][] queries;
}

class Output {
    public int[] answer;
}

class Process {
    private static final Comparator<int[]> ORDER_EDGES_BY_WEIGHT = Comparator.comparingInt(e -> e[2]);

    public Output process(final Input input) {
        final Output output = new Output();
        output.answer = new int[input.totalQueries];

        final int[][] edges = input.roads;
        Arrays.sort(edges, ORDER_EDGES_BY_WEIGHT);

        for (int i = 0; i < input.totalQueries; i++) {
            final int[] query = input.queries[i];
            final int origin = query[0], destination = query[1];

            int minTotalEnergies = Integer.MAX_VALUE;
            for (int minEdgeIdx = 0; minEdgeIdx < edges.length; minEdgeIdx++) {
                final int[][] minimumSpanningTree = createMinimumSpanningTree(
                    input,
                    edges,
                    minEdgeIdx, edges.length,
                    origin, destination
                );
                if (minimumSpanningTree == null) break;

                final int[] minEdge = minimumSpanningTree[0], maxEdge = minimumSpanningTree[minimumSpanningTree.length - 1];
                final int minWeight = minEdge[2], maxWeight = maxEdge[2];
                final int totalEnergies = maxWeight - minWeight + input.startEnergy + input.stopEnergy;
                minTotalEnergies = Math.min(minTotalEnergies, totalEnergies);
            }

            output.answer[i] = minTotalEnergies;
        }

        return output;
    }

    private int[][] createMinimumSpanningTree(
        final Input input,
        final int[][] edges,
        final int fromEdgeIdx,
        final int untilEdgeIdx,
        final int origin,
        final int destination
    ) {
        final DisjointSet connectedComponents = new DisjointSet(input.totalJunctions);
        final LinkedList<int[]> minimumSpanningTree = new LinkedList<>();

        for (int i = fromEdgeIdx; i < untilEdgeIdx; i++) {
            final int[] edge = edges[i];
            final int vertex1 = edge[0], vertex2 = edge[1];
            final int component1 = connectedComponents.find(vertex1), component2 = connectedComponents.find(vertex2);

            final boolean connected = component1 == component2;
            if (!connected) {
                minimumSpanningTree.addLast(edge);
                connectedComponents.union(vertex1, vertex2);
            }

            final int originComponent = connectedComponents.find(origin), destinationComponent = connectedComponents.find(destination);
            final boolean completed = originComponent == destinationComponent;
            if (completed) {
                return minimumSpanningTree.toArray(new int[0][0]);
            }
        }

        return null;
    }
}

final class DisjointSet {
    private final int[] parents;

    public DisjointSet(final int maxVertex) {
        parents = new int[maxVertex + 1];
        for (int vertex = 0; vertex <= maxVertex; vertex++) parents[vertex] = vertex;
    }

    public int find(final int child) {
        final int parent = parents[child];
        if (parent == child) {
            return parent;
        } else {
            final int grandparent = find(parent);
            parents[child] = grandparent;
            return grandparent;
        }
    }

    public void union(final int child1, final int child2) {
        final int parent1 = find(child1);
        final int parent2 = find(child2);
        parents[parent2] = parent1;
    }
}
