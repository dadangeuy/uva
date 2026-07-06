package uva.uhunt.c6.g5.p10405;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * 10405 - Longest Common Subsequence
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=1346
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        while (in.hasNextLine()) {
            final Input input = new Input();
            input.string1 = in.nextLine();
            input.string2 = in.nextLine();

            final Output output = process.process(input);
            out.println(output.longestCommonSubsequence);
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public String string1;
    public String string2;
}

class Output {
    public int longestCommonSubsequence;
}

class Process {
    public Output process(final Input input) {
        final Output output = new Output();

        final int[][] dp = new int[input.string1.length()][input.string2.length()];

        for (int i = 0; i < input.string1.length(); i++) {
            for (int j = 0; j < input.string2.length(); j++) {

                final char letter1 = input.string1.charAt(i);
                final char letter2 = input.string2.charAt(j);
                final boolean match = letter1 == letter2;

                final int candidate1 = get(dp, i - 1, j, 0);
                final int candidate2 = get(dp, i, j - 1, 0);
                final int candidate3 = get(dp, i - 1, j - 1, 0) + (match ? 1 : 0);

                dp[i][j] = Math.max(candidate1, Math.max(candidate2, candidate3));
            }
        }

        final int length1 = input.string1.length() - 1;
        final int length2 = input.string2.length() - 1;
        output.longestCommonSubsequence = get(dp, length1, length2, 0);
        return output;
    }

    private int get(final int[][] array, final int i, final int j, final int defaultValue) {
        final boolean valid1 = 0 <= i && i < array.length;
        final boolean valid2 = valid1 && 0 <= j && j < array[i].length;
        return valid2 ? array[i][j] : defaultValue;
    }
}
