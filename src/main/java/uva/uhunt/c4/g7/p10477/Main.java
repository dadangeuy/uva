package uva.uhunt.c4.g7.p10477;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * 10477 - The Hybrid Knight
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=1418
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        for (int setId = 1; ; setId++) {
            final Input input = new Input();
            input.setId = setId;
            input.boardSize = in.nextInt();
            if (input.isEOF()) break;
            input.totalQueries = in.nextInt();
            input.queries = new int[input.totalQueries][2];
            for (int queryId = 0; queryId < input.totalQueries; queryId++) {
                input.queries[queryId][0] = in.nextInt();
                input.queries[queryId][1] = in.nextInt();
            }

            final Output output = process.process(input);
            out.format("Set %d:\n", output.setId);
            for (final int answer : output.answers) {
                out.println(answer);
            }
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int setId;
    public int boardSize;
    public int totalQueries;
    public int[][] queries;

    public boolean isEOF() {
        return boardSize == 0;
    }
}

class Output {
    public int setId;
    public int[] answers;
}

class Process {
    public Output process(final Input input) {
        final Output output = new Output();
        output.setId = input.setId;
        output.answers = new int[input.totalQueries];

        for (int queryId = 0; queryId < input.totalQueries; queryId++) {
            final int[] query = input.queries[queryId];
            final int answer = findTotalMoves(input.boardSize, query);
            output.answers[queryId] = answer;
        }

        return output;
    }

    private int findTotalMoves(final int boardSize, final int[] originAndDestination) {
        final int originId = originAndDestination[0];
        final int destinationId = originAndDestination[1];
        if (originId == destinationId) {
            return 0;
        }

        final int[] origin = getPosition(boardSize, originId);
        final int[] destination = getPosition(boardSize, destinationId);

        final Queue<Path> pathQueue = new LinkedList<>();
        final Path initial = new Path(boardSize, origin);
        pathQueue.add(initial);

        final boolean[][][] visited = new boolean[4][boardSize][boardSize];
        visited[initial.type][initial.position[0]][initial.position[1]] = true;

        while (!pathQueue.isEmpty()) {
            final Path current = pathQueue.remove();

            final boolean finished = Arrays.equals(current.position, destination);
            if (finished) {
                return current.distance;
            }

            for (final Path next : current.nextPaths()) {
                if (visited[next.type][next.position[0]][next.position[1]]) {
                    continue;
                }
                pathQueue.add(next);
                visited[next.type][next.position[0]][next.position[1]] = true;
            }
        }

        return Integer.MAX_VALUE;
    }

    private int[] getPosition(final int boardSize, final int id) {
        final int id0 = id - 1;
        final int row = id0 / boardSize;
        final int col = id0 % boardSize;

        return new int[]{row, col};
    }
}

class Path {
    public static final int KNIGHT = 0;
    public static final int MUTANT_KNIGHT = 1;
    public static final int MUTANT_PAWN = 2;
    public static final int ATTACKING_MUTANT_PAWN = 3;

    public final int boardSize;
    public final int[] position;
    public final int distance;
    public final int type;

    public Path(final int boardSize, final int[] origin) {
        this(boardSize, origin, 0, KNIGHT);
    }

    private Path(final int boardSize, final int[] position, final int distance, final int type) {
        this.boardSize = boardSize;
        this.position = position;
        this.distance = distance;
        this.type = type;
    }

    public Path[] nextPaths() {
        final LinkedList<Path> nextPaths = new LinkedList<>();

        final int[][] knight = knightMoves(position);
        final int[][] mutantKnight = mutantKnightMoves(position);
        final int[][] mutantPawn = mutantPawnMoves(position);
        final int[][] attackingMutantPawn = attackingMutantPawnMoves(position);

        if (distance == 0) {
            for (final int[] position : knight) {
                if (!isValid(position)) continue;
                final Path path = new Path(boardSize, position, distance + 1, KNIGHT);
                nextPaths.add(path);
            }

            for (final int[] position : mutantKnight) {
                if (!isValid(position)) continue;
                final Path path = new Path(boardSize, position, distance + 1, MUTANT_KNIGHT);
                nextPaths.add(path);
            }

            for (final int[] position : mutantPawn) {
                if (!isValid(position)) continue;
                final Path path = new Path(boardSize, position, distance + 1, MUTANT_PAWN);
                nextPaths.add(path);
            }

            for (final int[] position : attackingMutantPawn) {
                if (!isValid(position)) continue;
                final Path path = new Path(boardSize, position, distance + 1, ATTACKING_MUTANT_PAWN);
                nextPaths.add(path);
            }

        } else if (type == KNIGHT) {
            for (final int[] position : mutantKnight) {
                if (!isValid(position)) continue;
                final Path path = new Path(boardSize, position, distance + 1, MUTANT_KNIGHT);
                nextPaths.add(path);
            }

            for (final int[] position : attackingMutantPawn) {
                if (!isValid(position)) continue;
                final Path path = new Path(boardSize, position, distance + 1, ATTACKING_MUTANT_PAWN);
                nextPaths.add(path);
            }

        } else if (type == MUTANT_KNIGHT) {
            for (final int[] position : mutantPawn) {
                if (!isValid(position)) continue;
                final Path path = new Path(boardSize, position, distance + 1, MUTANT_PAWN);
                nextPaths.add(path);
            }

            for (final int[] position : attackingMutantPawn) {
                if (!isValid(position)) continue;
                final Path path = new Path(boardSize, position, distance + 1, ATTACKING_MUTANT_PAWN);
                nextPaths.add(path);
            }

        } else if (type == MUTANT_PAWN) {
            for (final int[] position : knight) {
                if (!isValid(position)) continue;
                final Path path = new Path(boardSize, position, distance + 1, KNIGHT);
                nextPaths.add(path);
            }

            for (final int[] position : attackingMutantPawn) {
                if (!isValid(position)) continue;
                final Path path = new Path(boardSize, position, distance + 1, ATTACKING_MUTANT_PAWN);
                nextPaths.add(path);
            }
        }

        return nextPaths.toArray(new Path[0]);
    }

    private int[][] knightMoves(final int[] origin) {
        return new int[][]{
            new int[]{origin[0] - 1, origin[1] - 2},
            new int[]{origin[0] - 1, origin[1] + 2},
            new int[]{origin[0] - 2, origin[1] - 1},
            new int[]{origin[0] - 2, origin[1] + 1},
            new int[]{origin[0] + 1, origin[1] - 2},
            new int[]{origin[0] + 1, origin[1] + 2},
            new int[]{origin[0] + 2, origin[1] - 1},
            new int[]{origin[0] + 2, origin[1] + 1},
        };
    }

    private int[][] mutantKnightMoves(final int[] origin) {
        return new int[][]{
            new int[]{origin[0] - 1, origin[1] - 3},
            new int[]{origin[0] - 1, origin[1] + 3},
            new int[]{origin[0] - 3, origin[1] - 1},
            new int[]{origin[0] - 3, origin[1] + 1},
            new int[]{origin[0] + 1, origin[1] - 3},
            new int[]{origin[0] + 1, origin[1] + 3},
            new int[]{origin[0] + 3, origin[1] - 1},
            new int[]{origin[0] + 3, origin[1] + 1},
        };
    }

    private int[][] mutantPawnMoves(final int[] origin) {
        return new int[][]{
            new int[]{origin[0], origin[1] - 1},
            new int[]{origin[0], origin[1] + 1},
            new int[]{origin[0] - 1, origin[1]},
            new int[]{origin[0] + 1, origin[1]},
        };
    }

    private int[][] attackingMutantPawnMoves(final int[] origin) {
        return new int[][]{
            new int[]{origin[0] - 1, origin[1] - 1},
            new int[]{origin[0] - 1, origin[1] + 1},
            new int[]{origin[0] + 1, origin[1] - 1},
            new int[]{origin[0] + 1, origin[1] + 1},
        };
    }

    private boolean isValid(final int[] position) {
        final boolean valid1 = 0 <= position[0] && position[0] < boardSize;
        final boolean valid2 = 0 <= position[1] && position[1] < boardSize;
        return valid1 && valid2;
    }
}
