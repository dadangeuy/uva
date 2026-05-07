package uva.uhunt.c4.g5.p12875;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * 12875 - Concert Tour
 * Time limit: 1.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=4740
 */
public class Main {
    public static void main(final String... args) throws IOException {
        final BufferedIO io = new BufferedIO(System.in, System.out);
        final Process process = new Process();

        final int totalCases = Integer.parseInt(io.readLine());
        for (int i = 0; i < totalCases; i++) {
            final Input input = new Input();
            final String[] l1 = io.readLine(" ");
            input.totalStores = Integer.parseInt(l1[0]);
            input.totalConcerts = Integer.parseInt(l1[1]);
            input.profitPerStoreAndConcert = new int[input.totalStores][input.totalConcerts];
            for (int j = 0; j < input.totalStores; j++) {
                final String[] l2 = io.readLine(" ");
                for (int k = 0; k < input.totalConcerts; k++) {
                    input.profitPerStoreAndConcert[j][k] = Integer.parseInt(l2[k]);
                }
            }
            input.costBetweenStores = new int[input.totalStores][input.totalStores];
            for (int j = 0; j < input.totalStores; j++) {
                final String[] l3 = io.readLine(" ");
                for (int k = 0; k < input.totalStores; k++) {
                    input.costBetweenStores[j][k] = Integer.parseInt(l3[k]);
                }
            }

            final Output output = process.process(input);
            io.write("%d\n", output.totalNetProfits);
        }

        io.close();
    }
}

class Input {
    public int totalStores;
    public int totalConcerts;
    public int[][] profitPerStoreAndConcert;
    public int[][] costBetweenStores;
}

class Output {
    public int totalNetProfits;
}

class Process {
    public Output process(final Input input) {
        final int[][] dp = new int[input.totalConcerts][input.totalStores];
        fill(dp, Integer.MIN_VALUE);

        final int firstConcert = 0;
        for (int concert = 0; concert < input.totalConcerts; concert++) {
            final boolean isFirstConcert = concert == firstConcert;
            for (int prevStore = 0; prevStore < input.totalStores; prevStore++) {
                for (int nextStore = 0; nextStore < input.totalStores; nextStore++) {
                    final int profit = input.profitPerStoreAndConcert[nextStore][concert];
                    final int cost = isFirstConcert? 0 : input.costBetweenStores[prevStore][nextStore];
                    final int netProfit = profit - cost;

                    final int oldTotalNetProfits = dp[concert][nextStore];
                    final int newTotalNetProfits = (isFirstConcert? 0 : dp [concert - 1][prevStore]) + netProfit;
                    final int maxTotalNetProfits = Math.max(oldTotalNetProfits, newTotalNetProfits);

                    dp[concert][nextStore] = maxTotalNetProfits;
                }
            }
        }

        final int lastConcert = input.totalConcerts - 1;
        int maxTotalNetProfits = Integer.MIN_VALUE;
        for (int store = 0; store < input.totalStores; store++) {
            final int totalNetProfits = dp[lastConcert][store];
            maxTotalNetProfits = Math.max(maxTotalNetProfits, totalNetProfits);
        }

        final Output output = new Output();
        output.totalNetProfits = maxTotalNetProfits;
        return output;
    }

    private void fill(final int[][] array, final int value) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                array[i][j] = value;
            }
        }
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
