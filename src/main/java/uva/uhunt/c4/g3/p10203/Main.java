package uva.uhunt.c4.g3.p10203;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * 10203 - Snow Clearing
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=1144
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        final int totalTestCases = in.nextInt();
        in.nextLine();
        for (int caseId = 1; caseId <= totalTestCases; caseId++) {
            final Input input = nextInput(caseId, in);
            final Output output = process.process(input);
            printOutput(caseId, out, output);
        }

        in.close();
        out.flush();
        out.close();
    }

    private static Input nextInput(final int caseId, final Scanner in) {
        final Point hangar = new Point(in.nextLong(), in.nextLong());
        in.nextLine();

        final LinkedList<String[]> roadLines = new LinkedList<>();
        while (in.hasNextLong()) {
            final String line = in.nextLine();
            if (line.isEmpty()) break;
            roadLines.addLast(line.split(" "));
        }
        final Line[] roads = roadLines.stream()
            .map(roadLine -> new Line(
                    new Point(Long.parseLong(roadLine[0]), Long.parseLong(roadLine[1])),
                    new Point(Long.parseLong(roadLine[2]), Long.parseLong(roadLine[3]))
                )
            )
            .toArray(Line[]::new);

        final Input input = new Input();
        input.caseId = caseId;
        input.hangar = hangar;
        input.roads = roads;
        return input;
    }

    private static void printOutput(final int caseId, final PrintWriter out, final Output output) {
        if (caseId > 1) out.println();
        out.format("%d:%02d\n", output.hours, output.minutes);
    }
}

class Input {
    public int caseId;
    public Point hangar;
    public Line[] roads;
}

class Output {
    public int caseId;
    public long hours;
    public long minutes;
}

class Process {
    private static final double PLOWING_SPEED = 20;

    public Output process(final Input input) {
        final double totalDistances = Arrays.stream(input.roads)
            .mapToDouble(Line::distance)
            .sum();
        final double totalTimes = 2 * totalDistances * 60 / (PLOWING_SPEED * 1000);
        final Duration duration = Duration.ofMinutes(Math.round(totalTimes));

        final Output output = new Output();
        output.caseId = input.caseId;
        output.hours = duration.toHours();
        output.minutes = duration.minusHours(output.hours).toMinutes();
        return output;
    }
}

class Line {
    public final Point a;
    public final Point b;

    public Line(final Point a, final Point b) {
        this.a = a;
        this.b = b;
    }

    public double distance() {
        final long dx = Math.abs(a.x - b.x);
        final long dy = Math.abs(a.y - b.y);

        return Math.sqrt((dx * dx) + (dy * dy));
    }
}

class Point {
    public final long x;
    public final long y;

    public Point(final long x, final long y) {
        this.x = x;
        this.y = y;
    }
}
