package uva.uhunt.c3.g9.p11259;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * 11259 - Coin Changing Again
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2226
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        final int totalTestCases = in.nextInt();
        for (int i = 0; i < totalTestCases; i++) {
            final Input input = new Input();
            input.coins = new int[4];
            for (int j = 0; j < 4; j++) {
                input.coins[j] = in.nextInt();
            }
            input.totalQueries = in.nextInt();
            input.queries = new int[input.totalQueries][5];
            for (int j = 0; j < input.totalQueries; j++) {
                for (int k = 0; k < 5; k++) {
                    input.queries[j][k] = in.nextInt();
                }
            }

            final Output output = process.process(input);
            for (final int answer : output.answers) {
                out.println(answer);
            }
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int[] coins;
    public int totalQueries;
    public int[][] queries;
}

class Output {
    public int[] answers;
}

class Process {
    private static final int MAX_COUNT = 100_000;

    public Output process(final Input input) {
        final Output output = new Output();
        output.answers = new int[input.totalQueries];

        for (int i = 0; i < input.totalQueries; i++) {
            final int[] query = input.queries[i];
            final int[] counts = Arrays.copyOfRange(query, 0, 4);
            final int value = query[4];

            final int totalWays = findTotalWays(input.coins, counts, value);
            output.answers[i] = totalWays;
        }

        return output;
    }

    private int findTotalWays(final int[] coins, final int[] counts, final int value) {
        final TreeMap<Integer, Integer> totalWaysPerValue = new TreeMap<>();
        totalWaysPerValue.put(0, 1);

        for (int i = 0; i < coins.length; i++) {
            final int coin = coins[i];
            final int count = counts[i];

            for (Integer k = totalWaysPerValue.lastKey(); k != null; k = totalWaysPerValue.lowerKey(k)) {
                for (int j = count; j >= 1; j--) {
                    final int oldValue = k;
                    final int newValue = k + (coin * j);

                    final int oldTotalWays = totalWaysPerValue.getOrDefault(oldValue, 0);
                    final int newTotalWays = totalWaysPerValue.getOrDefault(newValue, 0) + oldTotalWays;

                    if (newValue <= value) {
                        totalWaysPerValue.put(newValue, newTotalWays);
                    }
                }
            }
        }

        return totalWaysPerValue.getOrDefault(value, 0);
    }
}
