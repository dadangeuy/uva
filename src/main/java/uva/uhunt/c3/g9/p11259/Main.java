package uva.uhunt.c3.g9.p11259;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

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
            input.denominations = new int[4];
            for (int j = 0; j < 4; j++) {
                input.denominations[j] = in.nextInt();
            }
            input.totalQueries = in.nextInt();
            input.queries = new int[input.totalQueries][5];
            for (int j = 0; j < input.totalQueries; j++) {
                for (int k = 0; k < 5; k++) {
                    input.queries[j][k] = in.nextInt();
                }
            }

            final Output output = process.process(input);
            for (final long answer : output.answers) {
                out.println(answer);
            }
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int[] denominations;
    public int totalQueries;
    public int[][] queries;
}

class Output {
    public long[] answers;
}

class Process {
    private static final int MAX_VALUE = 100_000;

    public Output process(final Input input) {
        final Output output = new Output();

        final int[] denominations = input.denominations;
        final long[] totalWaysPerValue = findTotalWaysPerValue(denominations);

        output.answers = new long[input.totalQueries];
        for (int i = 0; i < input.totalQueries; i++) {
            final int[] query = input.queries[i];
            final int[] quantities = Arrays.copyOfRange(query, 0, 4);
            final int value = query[4];

            final long totalWays = findTotalWays(totalWaysPerValue, value, denominations, quantities);
            output.answers[i] = totalWays;
        }

        return output;
    }

    // Coin Change with Dynamic Programming
    private long[] findTotalWaysPerValue(final int[] denominations) {
        final long[] totalWaysPerValue = new long[MAX_VALUE + 1];
        totalWaysPerValue[0] = 1;

        for (final int denomination : denominations) {
            for (int value = 0; value <= MAX_VALUE - denomination; value++) {
                final int newValue = value + denomination;
                totalWaysPerValue[newValue] += totalWaysPerValue[value];
            }
        }

        return totalWaysPerValue;
    }

    private long findTotalWays(
        final long[] totalWaysPerValue,
        final int value,
        final int[] denominations,
        final int[] quantities
    ) {
        final long w = getTotalWays(totalWaysPerValue, value, denominations, quantities);

        final long w0 = getTotalWays(totalWaysPerValue, value, denominations, quantities, 0);
        final long w1 = getTotalWays(totalWaysPerValue, value, denominations, quantities, 1);
        final long w2 = getTotalWays(totalWaysPerValue, value, denominations, quantities, 2);
        final long w3 = getTotalWays(totalWaysPerValue, value, denominations, quantities, 3);

        final long w01 = getTotalWays(totalWaysPerValue, value, denominations, quantities, 0, 1);
        final long w02 = getTotalWays(totalWaysPerValue, value, denominations, quantities, 0, 2);
        final long w03 = getTotalWays(totalWaysPerValue, value, denominations, quantities, 0, 3);
        final long w12 = getTotalWays(totalWaysPerValue, value, denominations, quantities, 1, 2);
        final long w13 = getTotalWays(totalWaysPerValue, value, denominations, quantities, 1, 3);
        final long w23 = getTotalWays(totalWaysPerValue, value, denominations, quantities, 2, 3);

        final long w012 = getTotalWays(totalWaysPerValue, value, denominations, quantities, 0, 1, 2);
        final long w013 = getTotalWays(totalWaysPerValue, value, denominations, quantities, 0, 1, 3);
        final long w023 = getTotalWays(totalWaysPerValue, value, denominations, quantities, 0, 2, 3);
        final long w123 = getTotalWays(totalWaysPerValue, value, denominations, quantities, 1, 2, 3);

        final long w0123 = getTotalWays(totalWaysPerValue, value, denominations, quantities, 0, 1, 2, 3);

        final long totalWays = (
            w
            - w0 - w1 - w2 - w3
            + w01 + w02 + w03 + w12 + w13 + w23
            - w012 - w013 - w023 - w123
            + w0123
        );
        return totalWays;
    }

    private long getTotalWays(
        final long[] totalWaysPerValue,
        final int value,
        final int[] denominations,
        final int[] quantities,
        final int... excludeCoins
    ) {
        int excludeValue = value;
        for (final int coin : excludeCoins) {
            final int quantity = quantities[coin] + 1;
            final int denomination = denominations[coin];
            excludeValue -= quantity * denomination;
        }

        return getOrDefault(totalWaysPerValue, excludeValue, 0);
    }

    private long getOrDefault(
        final long[] array,
        final int index,
        final int defaultValue
    ) {
        final boolean valid = 0 <= index && index < array.length;
        return valid ? array[index] : defaultValue;
    }
}
