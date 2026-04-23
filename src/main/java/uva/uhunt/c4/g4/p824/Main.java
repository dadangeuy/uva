package uva.uhunt.c4.g4.p824;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * 824 - Coast Tracker
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=765
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        for (int caseId = 1; in.hasNextInt(); caseId++) {
            final int x = in.nextInt();
            final boolean isEOF = x == -1;
            if (isEOF) break;

            final Input input = new Input();
            input.caseId = caseId;
            input.robot = new Pose(new Point(x, in.nextInt()), in.nextInt());
            input.surfaces = new Surface[8];
            for (int i = 0; i < 8; i++) {
                input.surfaces[i] = new Surface(new Point(in.nextInt(), in.nextInt()), in.nextInt());
            }

            final Output output = process.process(input);
            out.println(output.orientation);
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int caseId;
    public Pose robot;
    public Surface[] surfaces;
}

class Output {
    public int caseId;
    public int orientation;
}

class Process {
    private static final Point CENTER_POSITION = new Point(1, 1);

    public Output process(final Input input) {
        final Output output = new Output();
        output.caseId = input.caseId;

        final int[][] grid = new int[3][3];
        grid[CENTER_POSITION.x][CENTER_POSITION.y] = SurfaceType.LAND;
        for (final Surface surface : input.surfaces) {
            final Point relativePosition = surface.position.subtract(input.robot.position).add(CENTER_POSITION);
            grid[relativePosition.x][relativePosition.y] = surface.surfaceType;
        }

        for (int i = 0; i < 8; i++) {
            final int nextOrientation = (input.robot.orientation + 5 + i) % 8;
            final Point nextPosition = CENTER_POSITION.move(nextOrientation);
            final int nextSurface = grid[nextPosition.x][nextPosition.y];

            if (nextSurface == SurfaceType.LAND) {
                output.orientation = nextOrientation;
                break;
            }
        }

        return output;
    }
}

class Surface {
    public final Point position;
    public final int surfaceType;

    Surface(final Point position, final int surfaceType) {
        this.position = position;
        this.surfaceType = surfaceType;
    }
}

class Pose {
    public final Point position;
    public final int orientation;

    public Pose(final Point position, final int orientation) {
        this.position = position;
        this.orientation = orientation;
    }
}

class Point {
    public final int x;
    public final int y;

    public Point(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public Point add(final Point o) {
        return new Point(x + o.x, y + o.y);
    }

    public Point subtract(final Point o) {
        return new Point(x - o.x, y - o.y);
    }

    public Point move(final int orientation) {
        switch (orientation) {
            case Orientation.NORTH:
                return new Point(x, y + 1);
            case Orientation.NORTH_WEST:
                return new Point(x - 1, y + 1);
            case Orientation.WEST:
                return new Point(x - 1, y);
            case Orientation.SOUTH_WEST:
                return new Point(x - 1, y - 1);
            case Orientation.SOUTH:
                return new Point(x, y - 1);
            case Orientation.SOUTH_EAST:
                return new Point(x + 1, y - 1);
            case Orientation.EAST:
                return new Point(x + 1, y);
            case Orientation.NORTH_EAST:
                return new Point(x + 1, y + 1);
        }
        return this;
    }
}

class Orientation {
    public static final int NORTH = 0;
    public static final int NORTH_WEST = 1;
    public static final int WEST = 2;
    public static final int SOUTH_WEST = 3;
    public static final int SOUTH = 4;
    public static final int SOUTH_EAST = 5;
    public static final int EAST = 6;
    public static final int NORTH_EAST = 7;
}

class SurfaceType {
    public static final int WATER = 0;
    public static final int LAND = 1;
}
