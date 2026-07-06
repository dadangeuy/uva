package uva.uhunt.c6.g2.p11022;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * 11022 - String Factoring
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=1963
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        while (in.hasNext()) {
            final Input input = new Input();
            input.string = in.next();
            if (input.isEOF()) break;

            final Output output = process.process(input);
            out.println(output.weight);
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public String string;

    public boolean isEOF() {
        return "*".equals(string);
    }
}

class Output {
    public int weight;
}

class Process {
    private final Map<String, Integer> weightPerString = new HashMap<>();

    public Output process(final Input input) {
        final Output output = new Output();
        output.weight = getWeight(input.string);
        return output;
    }

    private int getWeight(final String string) {
        if (weightPerString.containsKey(string)) {
            return weightPerString.get(string);
        }

        int minWeight = string.length();
        for (int i = 0; i < string.length(); i++) {
            for (int length = 1; length < string.length() - i; length++) {
                final int start1 = i, end1 = i + length;
                final String factor1 = string.substring(start1, end1);

                for (int j = i + length; j < string.length(); j += length) {
                    final int start2 = j, end2 = Math.min(j + length, string.length());
                    final String factor2 = string.substring(start2, end2);

                    final boolean repeatable = factor1.equals(factor2);
                    if (!repeatable) break;

                    final String prefix = string.substring(0, start1);
                    final String suffix = string.substring(end2, string.length());

                    final int weight = getWeight(prefix) + getWeight(factor1) + getWeight(suffix);
                    minWeight = Math.min(minWeight, weight);
                }
            }
        }

        weightPerString.put(string, minWeight);
        return minWeight;
    }
}
