package uva.uhunt.c4.g8.p12768;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

/**
 * 12768 - Inspired Procrastination
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=4621
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        while (true) {
            final Input input = new Input();
            input.totalPoints = in.nextInt();
            input.totalDecisions = in.nextInt();
            if (input.isEOF()) break;

            input.decisions = new int[input.totalDecisions][3];
            for (int i = 0; i < input.totalDecisions; i++) {
                input.decisions[i][0] = in.nextInt();
                input.decisions[i][1] = in.nextInt();
                input.decisions[i][2] = in.nextInt();
            }

            final Output output = process.process(input);
            if (output.isUnlimited) out.println("Unlimited!");
            else out.println(output.maximumTotalFactors);
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int totalPoints;
    public int totalDecisions;
    public int[][] decisions;

    public boolean isEOF() {
        return totalPoints == 0 && totalDecisions == 0;
    }
}

class Output {
    public boolean isUnlimited;
    public int maximumTotalFactors;
}

class Process {
    public Output process(final Input input) {
        final int[][] negativeEdges = getNegativeEdges(input);
        final int[] distances = getDistancesWithBellmanFord(negativeEdges, input.totalPoints, 1);
        final boolean hasNegativeCycles = hasNegativeCycles(negativeEdges, distances);
        final int minimumDistance = getMinimumDistance(distances);

        final Output output = new Output();
        output.isUnlimited = hasNegativeCycles;
        output.maximumTotalFactors = -minimumDistance;

        return output;
    }

    private int[][] getNegativeEdges(final Input input) {
        final int[][] edges = new int[input.totalDecisions][3];
        for (int i = 0; i < input.totalDecisions; i++) {
            edges[i][0] = input.decisions[i][0];
            edges[i][1] = input.decisions[i][1];
            edges[i][2] = -input.decisions[i][2];
        }
        return edges;
    }

    private int[] getDistancesWithBellmanFord(final int[][] edges, final int maxVertex, final int source) {
        final int[] distances = new int[maxVertex + 1];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[source] = 0;

        for (int i = 0; i < maxVertex - 1; i++) {
            for (final int[] edge : edges) {
                final int from = edge[0], into = edge[1], weight = edge[2];
                if (distances[from] < Integer.MAX_VALUE) {
                    distances[into] = Math.min(distances[into], distances[from] + weight);
                }
            }
        }

        return distances;
    }

    private boolean hasNegativeCycles(final int[][] edges, final int[] distances) {
        for (final int[] edge : edges) {
            final int from = edge[0], into = edge[1], weight = edge[2];
            if (distances[from] < Integer.MAX_VALUE && distances[from] + weight < distances[into]) {
                return true;
            }
        }

        return false;
    }

    private int getMinimumDistance(final int[] distances) {
        return Arrays.stream(distances).min().orElse(0);
    }
}
