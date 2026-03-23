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

        final Memoization memoization = new Memoization(input.totalDrawers);
        output.totalWays = getTotalWays(memoization, input, UNLOCKED, 0, 0);

        return output;
    }

    private long getTotalWays(
        final Memoization memoization,
        final Input input,
        final int lastDrawer,
        final int height,
        final int totalSecured
    ) {
        if (memoization.contains(lastDrawer, height, totalSecured)) {
            return memoization.get(lastDrawer, height, totalSecured);
        }

        if (height == input.totalDrawers) {
            final boolean secured = lastDrawer == LOCKED;
            final boolean valid = input.totalSecuredDrawers == totalSecured + (secured ? 1 : 0);
            final long totalWays = valid ? 1 : 0;
            return memoization.set(lastDrawer, height, totalSecured, totalWays);
        }

        long totalWays = 0;
        totalWays += getTotalWays(memoization, input, UNLOCKED, height + 1, totalSecured);
        final boolean secured = lastDrawer == LOCKED;
        totalWays += getTotalWays(memoization, input, LOCKED, height + 1, totalSecured + (secured ? 1 : 0));

        return memoization.set(lastDrawer, height, totalSecured, totalWays);
    }
}

class Memoization {
    private final long[][][] totalWays;
    private final boolean[][][] exists;

    public Memoization(final int totalDrawers) {
        this.totalWays = new long[2][totalDrawers + 1][totalDrawers + 1];
        this.exists = new boolean[2][totalDrawers + 1][totalDrawers + 1];
    }

    public long set(final int drawer, final int height, final int totalSecured, final long totalWays) {
        this.exists[drawer][height][totalSecured] = true;
        return this.totalWays[drawer][height][totalSecured] = totalWays;
    }

    public boolean contains(final int drawer, final int height, final int totalSecured) {
        return this.exists[drawer][height][totalSecured];
    }

    public long get(final int drawer, final int height, final int totalSecured) {
        return totalWays[drawer][height][totalSecured];
    }
}
