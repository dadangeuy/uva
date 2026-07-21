package uva.uhunt.c4.g5.p705;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

/**
 * 705 - Slash Maze
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=646
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        for (int caseId = 1; in.hasNextInt(); caseId++) {
            final Input input = new Input();
            input.width = in.nextInt();
            input.height = in.nextInt();
            if (input.isEOF()) break;
            input.maze = new char[input.height][];
            for (int h = 0; h < input.height; h++) {
                input.maze[h] = in.next().toCharArray();
            }

            final Output output = process.process(input);
            if (output.totalCycles == 0) {
                out.format("Maze #%d:\nThere are no cycles.\n\n", caseId);
            } else {
                out.format("Maze #%d:\n%d Cycles; the longest has length %d.\n\n", caseId, output.totalCycles, output.maxLength);
            }
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int width;
    public int height;
    public char[][] maze;

    public boolean isEOF() {
        return width == 0 && height == 0;
    }
}

class Output {
    public int totalCycles;
    public int maxLength;
}

class Process {
    public Output process(final Input input) {
        final Output output = new Output();
        output.totalCycles = 0;
        output.maxLength = 0;

        final Maze maze = createRotatedMaze(input.height, input.width, input.maze);
        final boolean[][] visited = new boolean[maze.dimension][maze.dimension];

        for (int row = 0; row < maze.dimension; row++) {
            for (int col = 0; col < maze.dimension; col++) {
                final Point point = new Point(col, row);
                if (visited[point.y][point.x]) continue;

                final List<Point> steps = floodFill(maze, visited, point);
                if (steps.isEmpty()) continue;

                output.totalCycles++;
                output.maxLength = Math.max(output.maxLength, steps.size());
            }
        }

        return output;
    }

    private Maze createRotatedMaze(final int height, final int width, final char[][] maze) {
        final Maze rotatedMaze = new Maze(height + width);

        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                final int x = w - h + height, y = w + h;
                final Point point = new Point(x, y);

                final char m = maze[h][w];
                if (m == '/') {
                    rotatedMaze.addBottomBorder(point);
                    rotatedMaze.addBottomBorder(point.right());
                } else if (m == '\\') {
                    rotatedMaze.addRightBorder(point);
                    rotatedMaze.addRightBorder(point.bottom());
                }
            }
        }

        return rotatedMaze;
    }

    private List<Point> floodFill(final Maze maze, final boolean[][] visited, final Point initial) {
        if (!maze.isInside(initial)) return Collections.emptyList();
        if (visited[initial.y][initial.x]) return Collections.emptyList();

        final List<Point> steps = new LinkedList<>();
        final Queue<Point> queue = new LinkedList<>();

        steps.add(initial);
        queue.add(initial);
        visited[initial.y][initial.x] = true;

        boolean isOutside = false;
        while (!queue.isEmpty()) {
            final Point current = queue.remove();
            final Point[] adjacent = maze.adjacent(current);
            for (final Point next : adjacent) {
                if (!maze.isInside(next)) {
                    isOutside = true;
                    continue;
                }
                if (visited[next.y][next.x]) continue;

                steps.add(next);
                queue.add(next);
                visited[next.y][next.x] = true;
            }
        }

        return isOutside ? Collections.emptyList() : steps;
    }
}

class Maze {
    public final int dimension;
    public final Border[][] borders;

    public Maze(final int dimension) {
        this.dimension = dimension;
        this.borders = new Border[dimension + 1][dimension + 1];

        for (int i = 0; i <= dimension; i++) {
            for (int j = 0; j <= dimension; j++) {
                this.borders[i][j] = new Border();
            }
        }
    }

    public void addRightBorder(final Point point) {
        final Point left = point, right = point.right();
        borders[left.y][left.x].right = borders[right.y][right.x].left = true;
    }

    public void addBottomBorder(final Point point) {
        final Point top = point, bottom = point.bottom();
        borders[top.y][top.x].bottom = borders[bottom.y][bottom.x].top = true;
    }

    public Point[] adjacent(final Point point) {
        final Border border = borders[point.y][point.x];
        final List<Point> adjacent = new LinkedList<>();

        if (!border.top) adjacent.add(point.top());
        if (!border.bottom) adjacent.add(point.bottom());
        if (!border.left) adjacent.add(point.left());
        if (!border.right) adjacent.add(point.right());

        return adjacent.toArray(new Point[0]);
    }

    public boolean isInside(final Point point) {
        return 0 <= point.x && point.x < dimension && 0 <= point.y && point.y < dimension;
    }
}

class Point {
    public final int x;
    public final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point top() {
        return new Point(x, y - 1);
    }

    public Point bottom() {
        return new Point(x, y + 1);
    }

    public Point left() {
        return new Point(x - 1, y);
    }

    public Point right() {
        return new Point(x + 1, y);
    }
}

class Border {
    public boolean top;
    public boolean bottom;
    public boolean left;
    public boolean right;
}
