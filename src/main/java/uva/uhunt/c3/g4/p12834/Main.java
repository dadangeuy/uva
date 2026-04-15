package uva.uhunt.c3.g4.p12834;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

/**
 * 12834 - Extreme Terror
 * Time limit: 1.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=4699
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        final int totalTestCases = in.nextInt();
        for (int caseId = 1; caseId <= totalTestCases; caseId++) {
            final Input input = new Input();
            input.caseId = caseId;
            input.totalBusinesses = in.nextInt();
            input.totalDeniableBusinesses = in.nextInt();
            input.kamalMoneys = new long[input.totalBusinesses];
            input.shamsuMoneys = new long[input.totalBusinesses];
            for (int i = 0; i < input.totalBusinesses; i++) {
                input.kamalMoneys[i] = in.nextLong();
            }
            for (int i = 0; i < input.totalBusinesses; i++) {
                input.shamsuMoneys[i] = in.nextLong();
            }

            final Output output = process.process(input);
            if (output.isProfitable) {
                out.format("Case %d: %d\n", output.caseId, output.totalProfits);
            } else {
                out.format("Case %d: No Profit\n", output.caseId);
            }
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int caseId;
    public int totalBusinesses;
    public int totalDeniableBusinesses;
    public long[] kamalMoneys;
    public long[] shamsuMoneys;
}

class Output {
    public int caseId;
    public boolean isProfitable;
    public long totalProfits;
}

class Process {
    public Output process(final Input input) {
        final long[] profits = new long[input.totalBusinesses];
        for (int i = 0; i < input.totalBusinesses; i++) {
            final long profit = input.shamsuMoneys[i] - input.kamalMoneys[i];
            profits[i] = profit;
        }
        Arrays.sort(profits);

        long totalProfits = 0;
        int totalDeniedBusinesses = 0;
        for (final long profit : profits) {
            final boolean loss = profit <= 0;
            final boolean deniable = totalDeniedBusinesses < input.totalDeniableBusinesses;
            if (loss && deniable) {
                totalDeniedBusinesses++;
                continue;
            }
            totalProfits += profit;
        }

        final Output output = new Output();
        output.caseId = input.caseId;
        output.totalProfits = totalProfits;
        output.isProfitable = totalProfits > 0;
        return output;
    }
}
