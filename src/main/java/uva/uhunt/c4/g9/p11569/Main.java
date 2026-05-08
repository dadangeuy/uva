package uva.uhunt.c4.g9.p11569;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Set;
import java.util.TreeSet;

/**
 * 11569 - Lovely Hint
 * Time limit: 1.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2616
 */
public class Main {
    public static void main(final String... args) throws IOException {
        final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
        final Process process = new Process();

        final int totalSentences = Integer.parseInt(in.readLine());
        for (int i = 0; i < totalSentences; i++) {
            final Input input = new Input();
            input.sentence = in.readLine();

            final Output output = process.process(input);
            out.write(String.format("%d %d\n", output.length, output.total));
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public String sentence;
}

class Output {
    public int length;
    public int total;
}

class Process {
    public Output process(final Input input) {
        final int[] values = getValues(input.sentence);

        final int[] lengths = new int[27];
        final int[] totals = new int[27];

        for (int i = values.length - 1; i >= 0; i--) {
            final int value1 = values[i];
            lengths[value1] = 1;
            totals[value1] = 1;

            for (int j = i + 1; j < values.length; j++) {
                final int value2 = values[j];
                final boolean valid = value1 * 5 <= value2 * 4;

                if (valid) {
                    final int oldLength = lengths[value1];
                    final int newLength = 1 + lengths[value2];

                    final boolean longer = newLength > oldLength;
                    final boolean equals = newLength == oldLength;
                    if (longer) {
                        lengths[value1] = newLength;
                        totals[value1] = totals[value2];
                    } else if (equals) {
                        totals[value1] += totals[value2];
                    }
                }
            }
        }

        int maxLength = 0;
        int maxTotals = 0;

        for (int value = 1; value <= 26; value++) {
            final int length = lengths[value];
            final int total = totals[value];

            final boolean longer = length > maxLength;
            final boolean equals = length == maxLength;
            if (longer) {
                maxLength = length;
                maxTotals = total;
            } else if (equals) {
                maxTotals += total;
            }
        }

        final Output output = new Output();
        output.length = maxLength;
        output.total = maxTotals;
        return output;
    }

    private int[] getValues(final String sentence) {
        final Set<Integer> values = new TreeSet<>();
        for (final char letter : sentence.toCharArray()) {
            final boolean valid = 'A' <= letter && letter <= 'Z';
            if (valid) {
                final int value = letter - 'A' + 1;
                values.add(value);
            }
        }

        return values.stream()
            .mapToInt(Integer::intValue)
            .toArray();
    }
}
