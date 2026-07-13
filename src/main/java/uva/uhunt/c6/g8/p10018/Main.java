package uva.uhunt.c6.g8.p10018;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * 10018 - Reverse and Add
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=959
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        final int totalCases = in.nextInt();
        for (int i = 0; i < totalCases; i++) {
            final Input input = new Input();
            input.number = in.nextLong();

            final Output output = process.process(input);
            out.format("%d %d\n", output.totalIterations, output.palindrome);
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public long number;
}

class Output {
    public long totalIterations;
    public long palindrome;
}

class Process {
    private final Map<Long, Output> outputPerNumber = new HashMap<>();

    public Output process(final Input input) {
        final Output output = getOutput(input.number);

        return output;
    }

    private Output getOutput(final long number) {
        if (outputPerNumber.containsKey(number)) {
            return outputPerNumber.get(number);
        }

        if (isPalindrome(number)) {
            final Output output = new Output();
            output.totalIterations = 0;
            output.palindrome = number;
            return output;
        }

        final long reverseNumber = reverse(number);
        final long sumNumber = number + reverseNumber;

        final Output sumOutput = getOutput(sumNumber);
        final Output output = new Output();
        output.totalIterations = sumOutput.totalIterations + 1;
        output.palindrome = sumOutput.palindrome;
        return output;
    }

    private boolean isPalindrome(final long number) {
        final String text = Long.toString(number);
        for (int i = 0, j = text.length() - 1; i < j; i++, j--) {
            final boolean valid = text.charAt(i) == text.charAt(j);
            if (!valid) return false;
        }
        return true;
    }

    private long reverse(final long number) {
        final String text = Long.toString(number);
        final String reverseText = new StringBuilder(text).reverse().toString();
        return Long.parseLong(reverseText);
    }
}
