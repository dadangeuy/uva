package uva.uhunt.c3.g7.p13177;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 13177 - Orchestral scores
 * Time limit: 1.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=5088
 */
public class Main {
    public static void main(final String... args) throws IOException {
        final BufferedIO io = new BufferedIO(System.in, System.out, 1 << 16);
        final Process process = new Process();

        while (true) {
            final String[] line1 = io.readLine(" ");
            final String[] line2 = io.readLine(" ");
            final boolean isEOF = line1 == null;
            if (isEOF) break;

            final Input input = new Input();
            input.totalScores = Integer.parseInt(line1[0]);
            input.totalInstruments = Integer.parseInt(line1[1]);
            input.totalMusiciansPerInstrument = new int[input.totalInstruments];
            for (int i = 0; i < input.totalInstruments; i++) {
                input.totalMusiciansPerInstrument[i] = Integer.parseInt(line2[i]);
            }

            final Output output = process.process(input);
            io.write("%d\n", output.totalCrowds);
        }

        io.close();
    }
}

class Input {
    public int totalScores;
    public int totalInstruments;
    public int[] totalMusiciansPerInstrument;
}

class Output {
    public int totalCrowds;
}

class Process {
    public Output process(final Input input) {
        final int totalMusicians = sum(input.totalMusiciansPerInstrument);
        final int[] totalScoresPerInstrument = new int[input.totalInstruments];
        int remainingScores = input.totalScores;

        // assign scores based on ratio
        for (int instrument = 0; instrument < input.totalInstruments; instrument++) {
            final int thisTotalMusicians = totalScoresPerInstrument[instrument];
            final int thisTotalScores = Math.max(1, input.totalScores * thisTotalMusicians / totalMusicians);

            totalScoresPerInstrument[instrument] = thisTotalScores;
            remainingScores -= thisTotalScores;
        }

        // assign remaining scores based on crowd
        final Comparator<Integer> orderByCrowdDesc = Comparator
            .comparingInt(instrument -> -getTotalCrowds(
                input.totalMusiciansPerInstrument[instrument],
                totalScoresPerInstrument[instrument]
            ));
        final PriorityQueue<Integer> instrumentq = new PriorityQueue<>(orderByCrowdDesc);
        for (int instrument = 0; instrument < input.totalInstruments; instrument++) {
            instrumentq.add(instrument);
        }

        while (remainingScores > 0){
            final int instrument = instrumentq.remove();
            totalScoresPerInstrument[instrument]++;
            remainingScores--;
            instrumentq.add(instrument);
        }

        final int maxInstrument = instrumentq.peek();
        final int maxTotalCrowds = getTotalCrowds(
            input.totalMusiciansPerInstrument[maxInstrument],
            totalScoresPerInstrument[maxInstrument]
        );

        final Output output = new Output();
        output.totalCrowds = maxTotalCrowds;
        return output;
    }

    private int getTotalCrowds(final int totalMusicians, final int totalScores) {
        if (totalScores == 0) return Integer.MAX_VALUE;
        return ceilDiv(totalMusicians, totalScores);
    }

    private int ceilDiv(final int a, final int b) {
        return (a + b - 1) / b;
    }

    private int sum(final int[] array) {
        int sum = 0;
        for (final int value : array) sum += value;
        return sum;
    }
}

final class BufferedIO {
    private final BufferedReader in;
    private final BufferedWriter out;

    public BufferedIO(final InputStream in, final OutputStream out, final int bufferSize) {
        this.in = new BufferedReader(new InputStreamReader(in), bufferSize);
        this.out = new BufferedWriter(new OutputStreamWriter(out), bufferSize);
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
