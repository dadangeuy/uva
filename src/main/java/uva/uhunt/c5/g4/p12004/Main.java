package uva.uhunt.c5.g4.p12004;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * 12004 - Bubble Sort
 * Time limit: 1.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=3155
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        final int totalCases = in.nextInt();
        for (int caseId = 1; caseId <= totalCases; caseId++) {
            final Input input = new Input();
            input.n = in.nextLong();

            final Output output = process.process(input);
            if (output.q == 1) {
                out.format("Case %d: %d\n", caseId, output.p);
            } else {
                out.format("Case %d: %d/%d\n", caseId, output.p, output.q);
            }
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public long n;
}

class Output {
    public long p;
    public long q;
}

class Process {
    public Output process(final Input input) {
        final long n = input.n;

        // base formula for p/q: https://oeis.org/A001809, https://oeis.org/A000142
        long p = n * (n - 1);
        long q = 4;

        for (long gcd = gcd(p, q); gcd > 1; gcd = gcd(p, q)) {
            p = p / gcd;
            q = q / gcd;
        }

        final Output output = new Output();
        output.p = p;
        output.q = q;
        return output;
    }

    public long gcd(final long a, final long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}
