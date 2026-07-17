package uva.uhunt.c6.g4.p1584;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * 1584 - Circular Sequence
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=4459
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        final int totalCases = in.nextInt();
        for (int i = 0; i < totalCases; i++) {
            final Input input = new Input();
            input.sequence = in.next();

            final Output output = process.process(input);
            out.println(output.smallestSequence);
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public String sequence;
}

class Output {
    public String smallestSequence;
}

class Process {
    public Output process(final Input input) {
        String smallestSequence = input.sequence;
        for (int i = 0; i < input.sequence.length(); i++) {
            final String first = input.sequence.substring(0, i);
            final String second = input.sequence.substring(i);

            final String candidate = second + first;
            final boolean isSmaller = candidate.compareTo(smallestSequence) < 0;
            if (isSmaller) smallestSequence = candidate;
        }

        final Output output = new Output();
        output.smallestSequence = smallestSequence;
        return output;
    }
}
