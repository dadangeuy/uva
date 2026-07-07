package uva.uhunt.c6.g0.p10010;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * 10010 - Where's Waldorf?
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=951
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        final int totalCases = in.nextInt();
        for (int i = 0; i < totalCases; i++) {
            final Input input = new Input();
            input.totalRows = in.nextInt();
            input.totalColumns = in.nextInt();
            input.grid = new char[input.totalRows][];
            for (int j = 0; j < input.totalRows; j++) {
                input.grid[j] = in.next().toCharArray();
            }
            input.totalWords = in.nextInt();
            input.words = new String[input.totalWords];
            for (int j = 0; j < input.totalWords; j++) {
                input.words[j] = in.next();
            }

            final Output output = process.process(input);
            if (i > 0) out.println();
            for (final int[] cell : output.cells) {
                out.format("%d %d\n", cell[0], cell[1]);
            }
        }


        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int totalRows;
    public int totalColumns;
    public char[][] grid;
    public int totalWords;
    public String[] words;
}

class Output {
    public int[][] cells;
}

class Process {
    public Output process(final Input input) {
        final Output output = new Output();
        output.cells = new int[input.totalWords][];

        for (int i = 0; i < input.totalWords; i++) {
            final String word = input.words[i];
            output.cells[i] = findFirst(input.grid, word);
        }

        return output;
    }

    private int[] findFirst(
            final char[][] grid,
            final String word
    ) {
        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[0].length; column++) {
                if (contains(grid, row, column, word)) {
                    return new int[]{row + 1, column + 1};
                }
            }
        }
        return null;
    }

    private boolean contains(
            final char[][] grid,
            final int row,
            final int column,
            final String word
    ) {
        for (int rowStep = -1; rowStep <= 1; rowStep++) {
            for (int columnStep = -1; columnStep <= 1; columnStep++) {
                if (rowStep == 0 && columnStep == 0) continue;
                final String gridWord = read(grid, row, column, rowStep, columnStep, word.length());
                if (word.equalsIgnoreCase(gridWord)) return true;
            }
        }
        return false;
    }

    private String read(
            final char[][] grid,
            final int startRow,
            final int startColumn,
            final int rowStep,
            final int columnStep,
            final int length
    ) {
        final StringBuilder word = new StringBuilder();
        for (
                int row = startRow, column = startColumn;
                0 <= row && row < grid.length && 0 <= column && column < grid[0].length && word.length() < length;
                row += rowStep, column += columnStep
        ) {
            word.append(grid[row][column]);
        }
        return word.toString();
    }
}
