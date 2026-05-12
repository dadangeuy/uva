package uva.uhunt.c3.g0.p11490;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * 11490 - Just Another Problem
 * Time limit: 4.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2485
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        for (int caseId = 1; ; caseId++) {
            final Input input = new Input();
            input.caseId = caseId;
            input.totalSoldiers = in.nextLong();
            if (input.isEOF()) break;

            final Output output = process.process(input);
            if (output.isPossible) {
                for (final long totalMissingSoldiers : output.listTotalMissingSoldiers) {
                    out.format("Possible Missing Soldiers = %d\n", totalMissingSoldiers);
                }
            } else {
                out.println("No Solution Possible");
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
    public long totalSoldiers;

    public boolean isEOF() {
        return totalSoldiers == 0;
    }
}

class Output {
    public int caseId;
    public boolean isPossible;
    public long[] listTotalMissingSoldiers;
}

class Process {
    private static final long MODULO = 100000007;

    public Output process(final Input input) {
        final LinkedList<Long> innerLengths = new LinkedList<>();
        final long minOuterLength = 1;
        final long maxOuterLength = Math.round(Math.ceil(Math.sqrt(input.totalSoldiers)));

        for (long outerLength = minOuterLength; outerLength <= maxOuterLength; outerLength++) {
            final long innerLength = getInnerLength(input.totalSoldiers, outerLength);
            final long totalSoldiers = getTotalSoldiers(innerLength, outerLength);

            final boolean valid = innerLength > 0 && totalSoldiers == input.totalSoldiers;
            if (valid) innerLengths.addLast(innerLength);
        }

        final long[] listTotalMissingSoldiers = innerLengths.stream()
            .mapToLong(Long::longValue)
            .map(innerLength -> getTotalMissingSoldiers(innerLength, MODULO))
            .toArray();

        final Output output = new Output();
        output.caseId = input.caseId;
        output.isPossible = !innerLengths.isEmpty();
        output.listTotalMissingSoldiers = listTotalMissingSoldiers;
        return output;
    }

    private long getTotalSoldiers(final long innerLength, final long outerLength) {
        return (7 * innerLength * outerLength) + (6 * outerLength * outerLength);
    }

    private long getInnerLength(final long totalSoldiers, final long outerLength) {
        return (totalSoldiers - (6 * outerLength * outerLength)) / (7 * outerLength);
    }

    private long getTotalMissingSoldiers(final long innerLength, final long mod) {
        final long length = innerLength % mod;
        final long length2 = (length * length) % mod;
        return (2 * length2) % mod;
    }
}
