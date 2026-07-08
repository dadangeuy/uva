package uva.uhunt.c3.g3.p10763;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * 10763 - Foreign Exchange
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=1704
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        while (in.hasNextInt()) {
            final Input input = new Input();
            input.totalCandidates = in.nextInt();
            input.exchanges = new int[input.totalCandidates][2];
            for (int i = 0; i < input.totalCandidates; i++) {
                input.exchanges[i][0] = in.nextInt();
                input.exchanges[i][1] = in.nextInt();
            }
            if (input.isEOF()) break;

            final Output output = process.process(input);
            if (output.isValidExchanges) {
                out.println("YES");
            } else {
                out.println("NO");
            }
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int totalCandidates;
    public int[][] exchanges;

    public boolean isEOF() {
        return totalCandidates == 0;
    }
}

class Output {
    public boolean isValidExchanges;
}

class Process {
    private static final Output VALID_OUTPUT = buildOutput(true);
    private static final Output INVALID_OUTPUT = buildOutput(false);

    private static Output buildOutput(final boolean isValidExchanges) {
        final Output output = new Output();
        output.isValidExchanges = isValidExchanges;
        return output;
    }

    public Output process(final Input input) {
        final Counts totalCandidatesPerExchanges = new Counts();
        for (final int[] exchange : input.exchanges) {
            final int original = exchange[0], target = exchange[1];
            totalCandidatesPerExchanges.increment(original, target);
        }

        for (final int original : totalCandidatesPerExchanges.get()) {
            for (final int target : totalCandidatesPerExchanges.get(original)) {
                final int totalCandidates1 = totalCandidatesPerExchanges.get(original, target);
                final int totalCandidates2 = totalCandidatesPerExchanges.get(target, original);

                final boolean isValid = totalCandidates1 == totalCandidates2;
                if (!isValid) return INVALID_OUTPUT;
            }
        }

        return VALID_OUTPUT;
    }
}

class Counts {
    public Map<Integer, Map<Integer, Integer>> counts = new HashMap<>();

    public void increment(final int key1, final int key2) {
        counts
                .computeIfAbsent(key1, k -> new HashMap<>())
                .compute(key2, (k, v) -> 1 + (v == null ? 0 : v));
    }

    public Set<Integer> get() {
        return counts.keySet();
    }

    public Set<Integer> get(final int key1) {
        return counts
                .getOrDefault(key1, Collections.emptyMap())
                .keySet();
    }

    public int get(final int key1, final int key2) {
        return counts
                .getOrDefault(key1, Collections.emptyMap())
                .getOrDefault(key2, 0);
    }
}
