package uva.uhunt.c4.g3.p10913;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * 10913 - Walking on a Grid
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=1854
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        for (int caseId = 1; in.hasNextInt(); caseId++) {
            final Input input = new Input();
            input.caseId = caseId;
            input.gridSize = in.nextInt();
            input.negativeCount = in.nextInt();
            if (input.isEOF()) break;

            input.grid = new int[input.gridSize][input.gridSize];
            for (int i = 0; i < input.gridSize; i++) {
                for (int j = 0; j < input.gridSize; j++) {
                    input.grid[i][j] = in.nextInt();
                }
            }

            final Output output = process.process(input);
            if (output.isPossible) {
                out.format("Case %d: %d\n", output.caseId, output.sum);
            } else {
                out.format("Case %d: impossible\n", output.caseId);
            }
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int caseId;
    public int gridSize;
    public int negativeCount;
    public int[][] grid;

    public boolean isEOF() {
        return gridSize == 0 && negativeCount == 0;
    }
}

class Output {
    public int caseId;
    public boolean isPossible;
    public int sum;
}

class Process {
    private static final int DOWN = 0;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;

    public Output process(final Input input) {
        final Output output = new Output();
        output.caseId = input.caseId;
        output.sum = Integer.MIN_VALUE;

        final Tabulation tabulation = new Tabulation(2, input.negativeCount, input.gridSize - 1);
        final int startValue = input.grid[0][0];
        final int startCount = startValue < 0 ? 1 : 0;
        tabulation.set(DOWN, startCount, 0, 0, startValue);

        for (int count = 0; count <= input.negativeCount; count++) {
            for (int row = 0; row < input.gridSize; row++) {
                // (DOWN/RIGHT) + RIGHT
                for (int direction : new int[]{DOWN, RIGHT}) {
                    for (int col = 0; col < input.gridSize - 1; col++) {
                        final int sum = tabulation.getOrDefault(direction, count, row, col, Integer.MIN_VALUE);
                        final int rightCol = col + 1;
                        final int rightVal = input.grid[row][rightCol];
                        final int rightSum = sum + rightVal;
                        final int rightCount = count + (rightVal < 0 ? 1 : 0);

                        if (sum == Integer.MIN_VALUE) continue;
                        if (rightCount > input.negativeCount) continue;

                        tabulation.setMax(RIGHT, rightCount, row, rightCol, rightSum);
                    }
                }

                // (DOWN/LEFT) + LEFT
                for (int direction : new int[]{DOWN, LEFT}) {
                    for (int col = input.gridSize - 1; col > 0; col--) {
                        if (!tabulation.contains(direction, count, row, col)) continue;

                        final int sum = tabulation.getOrDefault(direction, count, row, col, Integer.MIN_VALUE);
                        final int leftCol = col - 1;
                        final int leftVal = input.grid[row][leftCol];
                        final int leftSum = sum + leftVal;
                        final int leftCount = count + (leftVal < 0 ? 1 : 0);

                        if (sum == Integer.MIN_VALUE) continue;
                        if (leftCount > input.negativeCount) continue;

                        tabulation.setMax(LEFT, leftCount, row, leftCol, leftSum);
                    }
                }

                // (DOWN/LEFT/RIGHT) + DOWN
                if (row == input.gridSize - 1) continue;
                for (int direction : new int[]{DOWN, LEFT, RIGHT}) {
                    for (int col = 0; col < input.gridSize; col++) {
                        final int sum = tabulation.getOrDefault(direction, count, row, col, Integer.MIN_VALUE);
                        final int downRow = row + 1;
                        final int downVal = input.grid[downRow][col];
                        final int downSum = sum + downVal;
                        final int downCount = count + (downVal < 0 ? 1 : 0);

                        if (sum == Integer.MIN_VALUE) continue;
                        if (downCount > input.negativeCount) continue;

                        tabulation.setMax(DOWN, downCount, downRow, col, downSum);
                    }
                }
            }
        }

        for (int count = 0; count <= input.negativeCount; count++) {
            for (int direction : new int[]{DOWN, LEFT, RIGHT}) {
                final int total = tabulation.getOrDefault(direction, count, input.gridSize - 1, input.gridSize - 1, Integer.MIN_VALUE);
                output.sum = Math.max(output.sum, total);
            }
        }
        output.isPossible = output.sum > Integer.MIN_VALUE;

        return output;
    }
}

class Tabulation {
    private final int[][][][] sums;
    private final boolean[][][][] exists;

    public Tabulation(final int maxDirection, final int maxCount, final int maxGridSize) {
        this.sums = new int[maxDirection + 1][maxCount + 1][maxGridSize + 1][maxGridSize + 1];
        this.exists = new boolean[maxDirection + 1][maxCount + 1][maxGridSize + 1][maxGridSize + 1];
    }

    public boolean contains(final int direction, final int count, final int row, final int col) {
        return exists[direction][count][row][col];
    }

    public int getOrDefault(final int direction, final int count, final int row, final int col, final int defaultSum) {
        return exists[direction][count][row][col] ? sums[direction][count][row][col] : defaultSum;
    }

    public int get(final int direction, final int count, final int row, final int col) {
        return sums[direction][count][row][col];
    }

    public void setMax(final int direction, final int count, final int row, final int col, final int sum) {
        final int maxSum = Math.max(getOrDefault(direction, count, row, col, Integer.MIN_VALUE), sum);
        set(direction, count, row, col, maxSum);
    }

    public void set(final int direction, final int count, final int row, final int col, final int sum) {
        sums[direction][count][row][col] = sum;
        exists[direction][count][row][col] = true;
    }
}
