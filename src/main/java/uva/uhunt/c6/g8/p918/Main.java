package uva.uhunt.c6.g8.p918;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 918 - ASCII Mandelbrot
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=859
 */
public class Main {
    public static void main(final String... args) throws IOException {
        final BufferedIO io = new BufferedIO(System.in, System.out);
        final Process process = new Process();

        final int totalCases = Integer.parseInt(io.readLine());
        for (int i = 0; i < totalCases; i++) {
            final String[] line = io.readLine(" ");

            final Input input = new Input();
            input.letters = line[0].substring(1, 13).toCharArray();
            input.imaginaryMin = new BigDecimal(line[1]);
            input.imaginaryMax = new BigDecimal(line[2]);
            input.imaginaryPrecision = new BigDecimal(line[3]);
            input.realMin = new BigDecimal(line[4]);
            input.realMax = new BigDecimal(line[5]);
            input.realPrecision = new BigDecimal(line[6]);

            final Output output = process.process(input);
            if (i > 0) io.write("\n");
            for (final char[] ploti : output.plot) {
                io.write("%s\n", new String(ploti));
            }
        }

        io.close();
    }
}

class Input {
    public char[] letters;
    public BigDecimal imaginaryMin;
    public BigDecimal imaginaryMax;
    public BigDecimal realMin;
    public BigDecimal realMax;
    public BigDecimal imaginaryPrecision;
    public BigDecimal realPrecision;
}

class Output {
    public char[][] plot;
}

class Process {
    public Output process(final Input input) {
        final Output output = new Output();

        final int vertical = getLength(input.imaginaryMin, input.imaginaryMax, input.imaginaryPrecision);
        final int horizontal = getLength(input.realMin, input.realMax, input.realPrecision);
        output.plot = new char[vertical + 1][horizontal + 1];
        fill(output.plot, ' ');

        BigDecimal imaginary = input.imaginaryMin;
        for (
                int row = 0;
                row < output.plot.length;
                row++, imaginary = imaginary.add(input.imaginaryPrecision)
        ) {
            BigDecimal real = input.realMin;
            for (
                    int col = 0;
                    col < output.plot[row].length;
                    col++, real = real.add(input.realPrecision)
            ) {
                ComplexNumber c = new ComplexNumber(real, imaginary);
                ComplexNumber z = new ComplexNumber(BigDecimal.ZERO, BigDecimal.ZERO);
                for (int iteration = 0; iteration < 12; iteration++) {
                    z = z.power2().add(c);
                    BigDecimal d2 = z.magnitude2();
                    if (d2.compareTo(BigDecimal.valueOf(4)) > 0) {
                        output.plot[row][col] = input.letters[iteration];
                        break;
                    }
                }
            }
        }

        return output;
    }

    private int getLength(final BigDecimal lower, final BigDecimal upper, final BigDecimal precision) {
        return upper.subtract(lower).divide(precision, RoundingMode.CEILING).intValue();
    }

    private void fill(final char[][] array, final char fill) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                array[i][j] = fill;
            }
        }
    }
}

class ComplexNumber {
    public final BigDecimal real;
    public final BigDecimal imaginary;

    public ComplexNumber(
            BigDecimal real,
            BigDecimal imaginary
    ) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public ComplexNumber power2() {
        final BigDecimal r2 = real.multiply(real);
        final BigDecimal i2 = imaginary.multiply(imaginary);
        final BigDecimal ri = real.multiply(imaginary);
        return new ComplexNumber(
                r2.subtract(i2),
                ri.multiply(BigDecimal.valueOf(2))
        );
    }

    public ComplexNumber add(ComplexNumber o) {
        return new ComplexNumber(
                real.add(o.real),
                imaginary.add(o.imaginary)
        );
    }

    public BigDecimal magnitude2() {
        final BigDecimal r2 = real.multiply(real);
        final BigDecimal i2 = imaginary.multiply(imaginary);
        return r2.add(i2);
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
