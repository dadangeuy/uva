package uva.uhunt.c3.g0.p11420;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * 11420 - Chest of Drawers
 * Time limit: 1.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2415
 */
public class Main {
    public static void main(final String... args) throws IOException {
        final BufferedIO io = new BufferedIO(System.in, System.out);
        final Process process = new Process();

        while (true) {
            final String[] lines = io.readLines(" ");

            final Input input = new Input();
            input.totalDrawers = Integer.parseInt(lines[0]);
            input.totalSecuredDrawers = Integer.parseInt(lines[1]);
            if (input.isEOF()) break;

            final Output output = process.process(input);
            io.write("%d\n", output.totalWays);
        }

        io.close();
    }
}

final class BufferedIO {
    private final BufferedReader in;
    private final BufferedWriter out;

    public BufferedIO(final InputStream in, final OutputStream out) {
        this.in = new BufferedReader(new InputStreamReader(in));
        this.out = new BufferedWriter(new OutputStreamWriter(out));
    }

    public String[] readLines(final String separator) throws IOException {
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

class Input {
    public int totalDrawers;
    public int totalSecuredDrawers;

    public boolean isEOF() {
        return totalDrawers < 0 && totalSecuredDrawers < 0;
    }
}

class Output {
    public long totalWays;
}

class Process {
    private static final int UNLOCKED = 0;
    private static final int LOCKED = 1;

    public Output process(final Input input) {
        final Output output = new Output();
        final Memoization memoization = new Memoization(input);
        output.totalWays = getTotalWays(memoization, UNLOCKED, input.totalDrawers, input.totalSecuredDrawers);
        return output;
    }

    private long getTotalWays(
        final Memoization memoization,
        final int lastDrawer,
        final int remainingHeight,
        final int remainingTotalSecuredDrawers
    ) {
        final boolean invalid = remainingHeight < 0 || remainingTotalSecuredDrawers < 0;
        if (invalid) {
            return 0;
        }

        final boolean topmost = remainingHeight == 0;
        if (topmost) {
            final boolean secured = lastDrawer == LOCKED;
            final boolean valid = 0 == remainingTotalSecuredDrawers - (secured ? 1 : 0);
            return valid ? 1 : 0;
        }

        final boolean computed = memoization.contains(lastDrawer, remainingHeight, remainingTotalSecuredDrawers);
        if (computed) {
            return memoization.get(lastDrawer, remainingHeight, remainingTotalSecuredDrawers);
        }

        long totalWays = 0;
        totalWays += getTotalWays(memoization, UNLOCKED, remainingHeight - 1, remainingTotalSecuredDrawers);
        final boolean secured = lastDrawer == LOCKED;
        totalWays += getTotalWays(memoization, LOCKED, remainingHeight - 1, remainingTotalSecuredDrawers - (secured ? 1 : 0));

        return memoization.set(lastDrawer, remainingHeight, remainingTotalSecuredDrawers, totalWays);
    }
}

class Memoization {
    private final long[][][] totalWays;
    private final boolean[][][] exists;

    public Memoization(final Input input) {
        totalWays = new long[2][input.totalDrawers + 1][input.totalSecuredDrawers + 1];
        exists = new boolean[2][input.totalDrawers + 1][input.totalSecuredDrawers + 1];
    }

    public long set(
        final int lastDrawer,
        final int remainingHeight,
        final int remainingTotalSecuredDrawers,
        final long totalWays
    ) {
        exists[lastDrawer][remainingHeight][remainingTotalSecuredDrawers] = true;
        return this.totalWays[lastDrawer][remainingHeight][remainingTotalSecuredDrawers] = totalWays;
    }

    public boolean contains(
        final int lastDrawer,
        final int remainingHeight,
        final int remainingTotalSecuredDrawers
    ) {
        return exists[lastDrawer][remainingHeight][remainingTotalSecuredDrawers];
    }

    public long get(
        final int lastDrawer,
        final int remainingHeight,
        final int remainingTotalSecuredDrawers
    ) {
        return totalWays[lastDrawer][remainingHeight][remainingTotalSecuredDrawers];
    }
}
