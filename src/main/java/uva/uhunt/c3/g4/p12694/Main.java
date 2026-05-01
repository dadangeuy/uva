package uva.uhunt.c3.g4.p12694;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * 12694 - Meeting Room Arrangement
 * Time limit: 1.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=4432
 */
public class Main {
    private static final int[] LAST_EVENT = new int[]{0, 0};

    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        final int totalCases = in.nextInt();
        for (int i = 0; i < totalCases; i++) {
            final LinkedList<int[]> events = new LinkedList<>();
            while (true) {
                final int[] event = new int[]{in.nextInt(), in.nextInt()};
                final boolean isEOF = Arrays.equals(LAST_EVENT, event);
                if (isEOF) break;
                events.addLast(event);
            }

            final Input input = new Input();
            input.events = events.toArray(new int[0][0]);

            final Output output = process.process(input);
            out.println(output.totalEvents);
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int[][] events;
}

class Output {
    public int totalEvents;
}

class Process {
    private static final Comparator<int[]> ORDER_BY_START_AND_FINISH = Comparator
        .comparingInt((int[] e) -> e[0])
        .thenComparingInt((int[] e) -> e[1]);

    public Output process(final Input input) {
        final int[][] events = input.events;

        final int maxFinishTime = getMaxFinishTime(events);
        final int[] totalEventsPerTime = new int[maxFinishTime + 1];

        final PriorityQueue<int[]> queue = new PriorityQueue<>(ORDER_BY_START_AND_FINISH);
        queue.addAll(Arrays.asList(events));

        for (int time = 0; time <= maxFinishTime; time++) {
            if (time > 0) {
                totalEventsPerTime[time] = Math.max(
                    totalEventsPerTime[time],
                    totalEventsPerTime[time - 1]
                );
            }

            while (!queue.isEmpty()) {
                final int[] event = queue.peek();
                final int startTime = event[0], finishTime = event[1];
                if (startTime > time) break;

                totalEventsPerTime[finishTime] = Math.max(totalEventsPerTime[finishTime], totalEventsPerTime[startTime] + 1);
                queue.remove();
            }
        }

        final Output output = new Output();
        output.totalEvents = totalEventsPerTime[maxFinishTime];
        return output;
    }

    private int getMaxFinishTime(final int[][] events) {
        int max = 0;
        for (int[] event : events) {
            max = Math.max(max, event[1]);
        }
        return max;
    }
}
