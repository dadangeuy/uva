package uva.uhunt.c3.g6.p1176;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * 1176 - A Benevolent Josephus
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=3617
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        while (in.hasNextInt()) {
            final Input input = new Input();
            input.totalPlayers = in.nextInt();

            final Output output = process.process(input);
            out.println(output.totalPayments);
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int totalPlayers;
}

class Output {
    public int totalPayments;
}

class Process {
    private static final int INTERVAL = 2;
    private static final int MAX_TOTAL_PEOPLES = 32767;
    private static final int[] SURVIVORS = findSurvivors(MAX_TOTAL_PEOPLES, INTERVAL);

    private static int[] findSurvivors(final int maxTotalPeoples, final int interval) {
        final int[] survivors = new int[maxTotalPeoples + 1];

        survivors[0] = -1;
        survivors[1] = 0;

        for (int totalPeoples = 2; totalPeoples <= maxTotalPeoples; totalPeoples++) {
            survivors[totalPeoples] = (survivors[totalPeoples - 1] + interval) % totalPeoples;
        }

        return survivors;
    }

    public Output process(final Input input) {
        final Output output = new Output();

        int totalPayments = 0;
        int totalPlayers = input.totalPlayers;

        while (true) {
            final int survivor = SURVIVORS[totalPlayers] + 1;
            final boolean canRemove = survivor < totalPlayers;

            if (canRemove) {
                final int payment = (totalPlayers - survivor);
                totalPayments += payment;
                totalPlayers = survivor;
            } else {
                final int payment = totalPlayers * 2;
                totalPayments += payment;
                totalPlayers = survivor;

                output.totalPayments = totalPayments;
                return output;
            }
        }
    }
}
