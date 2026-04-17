package uva.uhunt.c3.g8.p11368;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 * 11368 - Nested Dolls
 * Time limit: 1.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2353
 */
public class Main {
    public static void main(final String... args) throws IOException {
        final BufferedIO io = new BufferedIO(System.in, System.out);
        final Process process = new Process();

        final int totalCases = Integer.parseInt(io.readLine());
        for (int i = 0; i < totalCases; i++) {
            final Input input = new Input();
            input.totalDolls = Integer.parseInt(io.readLine());
            input.dimensions = new int[input.totalDolls][2];
            final String[] dimensionLines = io.readLine(" ");
            for (int j = 0, k = 0; j < input.totalDolls; j++) {
                input.dimensions[j][0] = Integer.parseInt(dimensionLines[k++]);
                input.dimensions[j][1] = Integer.parseInt(dimensionLines[k++]);
            }

            final Output output = process.process(input);
            io.write("%d\n", output.totalNestedDolls);
        }

        io.close();
    }
}

class Input {
    public int totalDolls;
    public int[][] dimensions;
}

class Output {
    public int totalNestedDolls;
}

class Process {
    public Output process(final Input input) {
        final Doll[] dolls = new Doll[input.totalDolls];
        for (int i = 0; i < input.totalDolls; i++) {
            final int[] dimension = input.dimensions[i];
            final Doll doll = new Doll(i, dimension[0], dimension[1]);
            dolls[i] = doll;
        }
        Arrays.sort(dolls, Doll.ORDER_BY_WIDTH);

        final TreeSet<Doll> previousWidthDolls = new TreeSet<>(Doll.ORDER_BY_HEIGHT);
        final LinkedList<Doll> currentWidthDolls = new LinkedList<>();
        for (final Doll doll : dolls) {
            final boolean isCurrentWidth = currentWidthDolls.isEmpty() || doll.width == currentWidthDolls.getFirst().width;
            if (!isCurrentWidth) {
                previousWidthDolls.addAll(currentWidthDolls);
                currentWidthDolls.clear();
            }
            currentWidthDolls.addLast(doll);

            final Doll query = new Doll(-1, doll.width, doll.height);
            final Doll innerDoll = previousWidthDolls.lower(query);
            if (innerDoll != null) {
                previousWidthDolls.remove(innerDoll);
            }
        }
        previousWidthDolls.addAll(currentWidthDolls);
        currentWidthDolls.clear();

        final Output output = new Output();
        output.totalNestedDolls = previousWidthDolls.size();
        return output;
    }
}

class Doll {
    public static final Comparator<Doll> ORDER_BY_WIDTH = Comparator
        .comparingInt((Doll d) -> d.width)
        .thenComparingInt((Doll d) -> d.id);
    public static final Comparator<Doll> ORDER_BY_HEIGHT = Comparator
        .comparingInt((Doll d) -> d.height)
        .thenComparingInt((Doll d) -> d.id);

    public final int id;
    public final int width;
    public final int height;

    public Doll(final int id, final int width, final int height) {
        this.id = id;
        this.width = width;
        this.height = height;
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
