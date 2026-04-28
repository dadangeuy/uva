package uva.uhunt.c3.g1.p13141;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * 13141 - Growing Trees
 * Time limit: 1.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=5052
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        while (in.hasNextInt()) {
            final Input input = new Input();
            input.level = in.nextInt();
            if (input.isEOF()) break;

            final Output output = process.process(input);
            out.println(output.totalLeaves);
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int level;

    public boolean isEOF() {
        return level == 0;
    }
}

class Output {
    public long totalLeaves;
}

class Process {
    private final long[][] dp = new long[85][2];

    public Output process(final Input input) {
        final Output output = new Output();
        output.totalLeaves = getTotalLeaves(input.level - 1, false);
        return output;
    }

    private long getTotalLeaves(final int remainingLevel, final boolean mustBranch) {
        if (remainingLevel == 0) {
            return 1;
        }

        final boolean unknown = dp[remainingLevel][mustBranch ? 1 : 0] == 0;
        if (unknown) {
            if (mustBranch) {
                final long total1 = getTotalLeaves(remainingLevel - 1, true);
                final long total2 = getTotalLeaves(remainingLevel - 1, false);
                final long total = total1 + total2;
                dp[remainingLevel][1] = total;
            } else {
                final long total = getTotalLeaves(remainingLevel - 1, true);
                dp[remainingLevel][0] = total;
            }
        }

        return dp[remainingLevel][mustBranch ? 1 : 0];
    }
}
