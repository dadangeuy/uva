package uva.uhunt.c4.g6.p12826;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 12826 - Incomplete Chessboard
 * Time limit: 1.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=4691
 */
public class Main {
    public static void main(final String... args) throws IOException {
        final BufferedIO io = new BufferedIO(System.in, System.out);
        final Process process = new Process();

        for (int caseId = 1; ; caseId++) {
            final String[] lines = io.readLine(" ");
            final boolean isEOF = lines == null || lines.length == 0;
            if (isEOF) break;

            final Input input = new Input();
            input.caseId = caseId;
            input.origin = new int[]{Integer.parseInt(lines[0]), Integer.parseInt(lines[1])};
            input.destination = new int[]{Integer.parseInt(lines[2]), Integer.parseInt(lines[3])};
            input.avoid = new int[]{Integer.parseInt(lines[4]), Integer.parseInt(lines[5])};

            final Output output = process.process(input);
            io.write("Case %d: %d\n", output.caseId, output.totalMoves);
        }

        io.close();
    }
}

class Input {
    public int caseId;
    public int[] origin;
    public int[] destination;
    public int[] avoid;
}

class Output {
    public int caseId;
    public int totalMoves;
}

class Process {
    public Output process(final Input input) {
        final Output output = new Output();
        output.caseId = input.caseId;

        final Queue<Path> pathq = new LinkedList<>();
        final Path initial = new Path(input.origin, input.destination, input.avoid);
        pathq.add(initial);

        final int[][] totalMovesPerCell = new int[9][9];
        fill(totalMovesPerCell, Integer.MAX_VALUE);
        totalMovesPerCell[initial.origin[0]][initial.origin[1]] = 0;

        while (!pathq.isEmpty()) {
            final Path currentPath = pathq.remove();
            if (currentPath.isFinished()) {
                output.totalMoves = currentPath.totalMoves;
                break;
            }

            for (final Path nextPath : currentPath.nextPaths()) {
                final int oldTotalMoves = totalMovesPerCell[nextPath.origin[0]][nextPath.origin[1]];
                final int newTotalMoves = nextPath.totalMoves;
                if (newTotalMoves < oldTotalMoves) {
                    pathq.add(nextPath);
                    totalMovesPerCell[nextPath.origin[0]][nextPath.origin[1]] = nextPath.totalMoves;
                }
            }
        }

        return output;
    }

    private void fill(final int[][] array, final int value) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                array[i][j] = value;
            }
        }
    }
}

class Path {
    public final int[] origin;
    public final int[] destination;
    public final int[] avoid;
    public final int totalMoves;

    public Path(final int[] origin, final int[] destination, final int[] avoid) {
        this(origin, destination, avoid, 0);
    }

    private Path(final int[] origin, final int[] destination, final int[] avoid, final int totalMoves) {
        this.origin = origin;
        this.destination = destination;
        this.avoid = avoid;
        this.totalMoves = totalMoves;
    }

    public int distance() {
        final int d1 = Math.abs(origin[0] - destination[0]) + Math.abs(origin[1] - destination[1]);
        final int d2 = Math.abs(origin[1] - destination[1]) + Math.abs(origin[1] - destination[1]);
        return d1 + d2;
    }

    public Path[] nextPaths() {
        final Path[] nextPaths = new Path[]{
            nextPath(new int[]{origin[0] - 1, origin[1] - 1}),
            nextPath(new int[]{origin[0] - 1, origin[1]}),
            nextPath(new int[]{origin[0] - 1, origin[1] + 1}),
            nextPath(new int[]{origin[0], origin[1] - 1}),
            nextPath(new int[]{origin[0], origin[1] + 1}),
            nextPath(new int[]{origin[0] + 1, origin[1] - 1}),
            nextPath(new int[]{origin[0] + 1, origin[1]}),
            nextPath(new int[]{origin[0] + 1, origin[1] + 1}),
        };
        final Path[] validNextPaths = Arrays.stream(nextPaths)
            .filter(Path::isValid)
            .toArray(Path[]::new);

        return validNextPaths;
    }

    public Path nextPath(final int[] next) {
        return new Path(next, destination, avoid, totalMoves + 1);
    }

    public boolean isValid() {
        final boolean valid1 = 1 <= origin[0] && origin[0] <= 8;
        final boolean valid2 = 1 <= origin[1] && origin[1] <= 8;
        final boolean valid3 = !Arrays.equals(origin, avoid);

        return valid1 && valid2 && valid3;
    }

    public boolean isFinished() {
        return Arrays.equals(origin, destination);
    }
}

final class BufferedIO {
    private final BufferedReader in;
    private final BufferedWriter out;

    public BufferedIO(final InputStream in, final OutputStream out) {
        this.in = new BufferedReader(new InputStreamReader(in));
        this.out = new BufferedWriter(new OutputStreamWriter(out));
    }

    public String[] readLine(final String separator) throws IOException {
        final String line = readLine();
        return line == null ? null : line.split(separator);
    }

    public String readLine() throws IOException {
        String line = in.readLine();
        while (line != null && line.isEmpty()) line = in.readLine();
        return line;
    }

    public void write(final String format, Object... args) throws IOException {
        final String string = String.format(format, args);
        write(string);
    }

    public void write(final String string) throws IOException {
        out.write(string);
    }

    public void close() throws IOException {
        in.close();
        out.flush();
        out.close();
    }
}
