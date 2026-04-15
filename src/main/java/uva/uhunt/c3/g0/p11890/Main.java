package uva.uhunt.c3.g0.p11890;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

/**
 * 11890 - Calculus Simplified
 * Time limit: 2.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2990
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        final int totalTests = in.nextInt();
        for (int i = 0; i < totalTests; i++) {
            final Input input = new Input();
            input.expression = in.next();
            input.totalNumbers = in.nextInt();
            input.numbers = new int[input.totalNumbers];
            for (int j = 0; j < input.totalNumbers; j++) {
                input.numbers[j] = in.nextInt();
            }

            final Output output = process.process(input);
            out.println(output.maximumValue);
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public String expression;
    public int totalNumbers;
    public int[] numbers;
}

class Output {
    public int maximumValue;
}

class Process {
    public Output process(final Input input) {
        final Output output = new Output();
        output.maximumValue = 0;

        final Deque<Integer> numbers = new LinkedList<>();
        final Stack<Character> letters = new Stack<>();
        boolean negative = false;

        Arrays.sort(input.numbers);
        for (final int number : input.numbers) {
            numbers.addLast(number);
        }

        for (final char letter : input.expression.toCharArray()) {
            switch (letter) {
                case '(':
                case '+':
                    letters.add(letter);
                    break;

                case ')':
                    letters.pop();

                    if (letters.empty()) {
                    } else if (letters.peek() == '+') {
                        letters.pop();
                    } else if (letters.peek() == '-') {
                        letters.pop();
                        negative = !negative;
                    }
                    break;

                case 'x':
                    if (negative) output.maximumValue -= numbers.removeFirst();
                    else output.maximumValue += numbers.removeLast();

                    if (letters.empty()) {
                    } else if (letters.peek() == '+') {
                        letters.pop();
                    } else if (letters.peek() == '-') {
                        letters.pop();
                        negative = !negative;
                    }
                    break;

                case '-':
                    letters.add(letter);
                    negative = !negative;
                    break;
            }
        }

        return output;
    }
}
