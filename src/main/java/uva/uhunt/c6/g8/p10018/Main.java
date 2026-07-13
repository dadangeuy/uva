package uva.uhunt.c6.g8.p10018;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
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
    public Output process(final Input input) {
        long totalIterations = 0, number = input.number;
        while (!isPalindrome(number)) {
            totalIterations++;
            number += reverse(number);
        }

        final Output output = new Output();
        output.totalIterations = totalIterations;
        output.palindrome = number;
        return output;
    }

    private boolean isPalindrome(final long number) {
        return number == reverse(number);
    }

    private long reverse(final long number) {
        final String text = Long.toString(number);
        final String reverseText = reverse(text);
        return Long.parseLong(reverseText);
    }

    private String reverse(final String text) {
        return new StringBuilder(text).reverse().toString();
    }
}
