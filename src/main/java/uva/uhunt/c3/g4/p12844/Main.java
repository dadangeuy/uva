package uva.uhunt.c3.g4.p12844;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

/**
 * 12844 - Outwitting the Weighing Machine
 * Time limit: 1.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=4709
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        final int totalTestCases = in.nextInt();
        for (int i = 0; i < totalTestCases; i++) {
            final Input input = new Input();
            input.caseId = i + 1;
            input.coupleWeights = new int[10];
            for (int j = 0; j < 10; j++) {
                input.coupleWeights[j] = in.nextInt();
            }

            final Output output = process.process(input);
            out.format(
                "Case %d: %d %d %d %d %d\n",
                output.caseId, output.weights[0], output.weights[1], output.weights[2], output.weights[3], output.weights[4]
            );
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int caseId;
    public int[] coupleWeights;
}

class Output {
    public int caseId;
    public int[] weights;
}

class Process {
    public Output process(final Input input) {
        final int[] couples = input.coupleWeights;
        Arrays.sort(couples);

        final int abcde = Arrays.stream(couples).sum() / 4;

        for (int i = 0; i < couples.length; i++) {
            final int ab = couples[i];

            for (int j = i + 1; j < couples.length; j++) {
                final int bc = couples[j];

                for (int k = j + 1; k < couples.length; k++) {
                    final int cd = couples[k];

                    for (int l = k + 1; l < couples.length; l++) {
                        final int de = couples[l];

                        final int a = abcde - bc - de;
                        final int b = ab - a;
                        final int c = bc - b;
                        final int d = cd - c;
                        final int e = de - d;

                        final int[] personals = new int[]{a, b, c, d, e};
                        Arrays.sort(personals);

                        final boolean isValid = isValid(couples, personals);
                        if (isValid) {
                            final Output output = new Output();
                            output.caseId = input.caseId;
                            output.weights = personals;
                            return output;
                        }
                    }
                }
            }
        }

        throw new NullPointerException();
    }

    private boolean isValid(final int[] couples, final int[] personals) {
        final int[] couples2 = new int[10];
        for (int i = 0, k = 0; i < 5; i++) {
            for (int j = i + 1; j < 5; j++, k++) {
                couples2[k] = personals[i] + personals[j];
            }
        }
        Arrays.sort(couples2);

        return Arrays.equals(couples, couples2);
    }
}
