package uva.uhunt.c6.g5.p11475;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * 11475 - Extend to Palindrome
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2470
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        while (in.hasNext()) {
            final Input input = new Input();
            input.text = in.next();
            final Output output = process.process(input);
            out.println(output.palindrome);
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public String text;
}

class Output {
    public String palindrome;
}

class Process {
    public Output process(final Input input) {
        final Output output = new Output();

        final String text = input.text;
        final int textLength = text.length();
        for (int palindromeLength = textLength; palindromeLength <= 2 * textLength; palindromeLength++) {
            final boolean isPalindrome = isPalindrome(text, textLength, palindromeLength);
            if (isPalindrome) {
                final String palindrome = buildPalindrome(text, textLength, palindromeLength);
                output.palindrome = palindrome;
                break;
            }
        }

        return output;
    }

    private boolean isPalindrome(final String text, final int textLength, final int palindromeLength) {
        final int skipLength = palindromeLength - textLength;
        for (int left = skipLength, right = textLength - 1; left < right; left++, right--) {
            final boolean isEquals = text.charAt(left) == text.charAt(right);
            if (!isEquals) return false;
        }
        return true;
    }

    private String buildPalindrome(final String text, final int textLength, final int palindromeLength) {
        final int skipLength = palindromeLength - textLength;

        final StringBuilder palindrome = new StringBuilder(palindromeLength);
        for (int i = 0; i < skipLength; i++) palindrome.append(text.charAt(i));
        for (int i = skipLength; i < textLength; i++) palindrome.append(text.charAt(i));
        for (int i = skipLength - 1; i >= 0; i--) palindrome.append(text.charAt(i));

        return palindrome.toString();
    }
}
