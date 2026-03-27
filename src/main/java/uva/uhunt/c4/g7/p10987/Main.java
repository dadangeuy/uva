package uva.uhunt.c4.g7.p10987;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * 10987 - Antifloyd
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=1928
 */
public class Main {
    public static void main(String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        final int totalTestCases = in.nextInt();
        for (int i = 0; i < totalTestCases; i++) {
            final Input input = new Input();
            input.caseId = i + 1;
            input.totalComputers = in.nextInt();

            final LinkedList<int[]> measurements = new LinkedList<>();
            for (int j = 0; j < input.totalComputers - 1; j++) {
                for (int k = 0; k <= j; k++) {
                    final int computer1 = j + 2, computer2 = k + 1, latency = in.nextInt();
                    final int[] measurement = new int[]{computer1, computer2, latency};
                    measurements.addLast(measurement);
                }
            }
            input.measurements = measurements.toArray(new int[0][0]);

            final Output output = process.process(input);
            out.format("Case #%d:\n", output.caseId);
            if (output.isPossible) {
                out.println(output.totalCables);
                for (final int[] cable : output.cables) {
                    out.format("%d %d %d\n", cable[0], cable[1], cable[2]);
                }
            } else {
                out.println("Need better measurements.");
            }
            out.println();
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int caseId;
    public int totalComputers;
    public int[][] measurements;
}

class Output {
    public int caseId;
    public boolean isPossible;
    public int totalCables;
    public int[][] cables;
}

class Process {
    private static final Comparator<int[]> ORDER_BY_VERTICES = Comparator
        .comparingInt((int[] m) -> m[0])
        .thenComparingInt((int[] m) -> m[1]);

    public Output process(final Input input) {
        final int[][] edges = input.measurements;
        for (final int[] edge : edges) {
            final int max = Math.max(edge[0], edge[1]), min = Math.min(edge[0], edge[1]);
            edge[0] = min;
            edge[1] = max;
        }
        Arrays.sort(edges, ORDER_BY_VERTICES);

        final AllPairsShortestPaths apsp = floydWarshall(input.totalComputers, input.measurements);
        final boolean isMinimumMeasurements = isMinimumMeasurements(input.measurements, apsp);
        final int[][] directCables = filterDirectCables(input.measurements, apsp);

        final Output output = new Output();
        output.caseId = input.caseId;
        output.isPossible = isMinimumMeasurements;
        output.totalCables = directCables.length;
        output.cables = directCables;

        return output;
    }

    private AllPairsShortestPaths floydWarshall(final int totalVertices, final int[][] edges) {
        final int[][] distances = new int[totalVertices + 1][totalVertices + 1];
        final boolean[][] hasAlternatives = new boolean[totalVertices + 1][totalVertices + 1];

        for (int vertex = 1; vertex <= totalVertices; vertex++) {
            distances[vertex][vertex] = 0;
            hasAlternatives[vertex][vertex] = false;
        }

        for (final int[] edge : edges) {
            final int vertex1 = edge[0], vertex2 = edge[1], weight = edge[2];
            distances[vertex1][vertex2] = distances[vertex2][vertex1] = weight;
            hasAlternatives[vertex1][vertex2] = hasAlternatives[vertex2][vertex1] = false;
        }

        for (int alternative = 1; alternative <= totalVertices; alternative++) {
            for (int origin = 1; origin <= totalVertices; origin++) {
                if (alternative == origin) continue;
                for (int destination = 1; destination <= totalVertices; destination++) {
                    if (alternative == destination) continue;
                    if (origin == destination) continue;

                    final int distance = distances[origin][destination];
                    final int alternativeDistance = distances[origin][alternative] + distances[alternative][destination];

                    distances[origin][destination] = Math.min(distance, alternativeDistance);
                    hasAlternatives[origin][destination] |= alternativeDistance <= distance;
                }
            }
        }

        return new AllPairsShortestPaths(distances, hasAlternatives);
    }

    private boolean isMinimumMeasurements(final int[][] measurements, final AllPairsShortestPaths apsp) {
        for (int[] measurement : measurements) {
            final int computer1 = measurement[0], computer2 = measurement[1], latency = measurement[2];
            final int minimumLatency = apsp.distances[computer1][computer2];

            final boolean isMinimum = latency == minimumLatency;
            if (!isMinimum) return false;
        }
        return true;
    }

    private int[][] filterDirectCables(final int[][] measurements, final AllPairsShortestPaths apsp) {
        return Arrays.stream(measurements)
            .filter(measurement -> !apsp.hasAlternatives[measurement[0]][measurement[1]])
            .toArray(int[][]::new);
    }
}

class AllPairsShortestPaths {
    public final int[][] distances;
    public final boolean[][] hasAlternatives;

    public AllPairsShortestPaths(final int[][] distances, final boolean[][] hasAlternatives) {
        this.distances = distances;
        this.hasAlternatives = hasAlternatives;
    }
}
