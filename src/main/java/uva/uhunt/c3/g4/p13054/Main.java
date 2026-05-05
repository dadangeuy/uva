package uva.uhunt.c3.g4.p13054;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * 13054 - Hippo Circus
 * Time limit: 1.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=4952
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        final int totalCases = in.nextInt();
        for (int i = 0; i < totalCases; i++) {
            final Input input = new Input();
            input.caseId = i + 1;
            input.totalHippos = in.nextInt();
            input.doorHeight = in.nextInt();
            input.aloneTime = in.nextInt();
            input.carryTime = in.nextInt();
            input.hippoHeights = new int[input.totalHippos];
            for (int j = 0; j < input.totalHippos; j++) {
                input.hippoHeights[j] = in.nextInt();
            }

            final Output output = process.process(input);
            out.format("Case %d: %d\n", output.caseId, output.minimumTime);
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int caseId;
    public int totalHippos;
    public int doorHeight;
    public int aloneTime;
    public int carryTime;
    public int[] hippoHeights;
}

class Output {
    public int caseId;
    public int minimumTime;
}

class Process {
    public Output process(final Input input) {
        final Output output = new Output();
        output.caseId = input.caseId;

        final boolean isFasterAlone = (input.aloneTime * 2) <= input.carryTime;
        if (isFasterAlone) {
            output.minimumTime = input.aloneTime * input.totalHippos;
            return output;
        }

        final int[] hippoHeights = input.hippoHeights;
        Arrays.sort(hippoHeights);
        final Deque<Integer> deque = new LinkedList<>();
        for (final int height : hippoHeights) deque.addLast(height);

        int totalTimes = 0;
        while (!deque.isEmpty()) {
            final int shortest = deque.removeFirst();
            if (deque.isEmpty()) {
                totalTimes += input.aloneTime;
            } else {
                final int tallest = deque.removeLast();
                final boolean canFit = (shortest + tallest) < input.doorHeight;
                if (canFit) {
                    totalTimes += input.carryTime;
                } else {
                    totalTimes += input.aloneTime;
                    deque.addFirst(shortest);
                }
            }
        }

        output.minimumTime = totalTimes;
        return output;
    }
}
