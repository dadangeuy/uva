package uva.uhunt.c3.g5.p12955;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

/**
 * 12955 - Factorial
 * Time limit: 1.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=4834
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        while (in.hasNextInt()) {
            final Input input = new Input();
            input.factorialSum = in.nextInt();

            final Output output = process.process(input);
            out.println(output.factorialSumQuantity);
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int factorialSum;
}

class Output {
    public int factorialSumQuantity;
}

class Process {
    private final int[] factorialList;
    private final int[] factorialSumList;

    public Process() {
        this.factorialList = getFactorialList(10);
        this.factorialSumList = getFactorialSumList(100_000);
    }

    public Output process(final Input input) {
        final Output output = new Output();
        output.factorialSumQuantity = factorialSumList[input.factorialSum];
        return output;
    }

    private int[] getFactorialList(final int max) {
        final int[] factorials = new int[max + 1];
        factorials[0] = factorials[1] = 1;
        for (int i = 2; i <= max; i++) {
            factorials[i] = i * factorials[i - 1];
        }

        return factorials;
    }

    private int[] getFactorialSumList(final int max) {
        final int[] coinChange = new int[max + 1];
        Arrays.fill(coinChange, Integer.MAX_VALUE);
        coinChange[0] = 0;

        boolean hasUpdate = true;
        while (hasUpdate) {
            hasUpdate = false;

            for (final int factorial : factorialList) {
                for (int factorialSum = coinChange.length - 1; factorialSum >= 0; factorialSum--) {
                    final int count = coinChange[factorialSum];
                    if (count == Integer.MAX_VALUE) continue;

                    final int newFactorialSum = factorialSum + factorial;
                    final int newCount = count + 1;
                    if (newFactorialSum > max) continue;
                    if (newCount >= coinChange[newFactorialSum]) continue;

                    hasUpdate = true;
                    coinChange[newFactorialSum] = newCount;
                }
            }
        }

        return coinChange;
    }
}
