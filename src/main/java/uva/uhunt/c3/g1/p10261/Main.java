package uva.uhunt.c3.g1.p10261;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * 10261 - Ferry Loading
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=1202
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        final int totalCases = in.nextInt();
        for (int i = 0; i < totalCases; i++) {
            final Input input = new Input();
            input.ferry = in.nextInt();
            final List<Integer> cars = new LinkedList<>();
            for (int car = in.nextInt(); car != 0; car = in.nextInt()) {
                cars.add(car);
            }
            input.cars = cars.stream().mapToInt(v -> v).toArray();

            final Output output = process.process(input);
            if (i > 0) out.println();
            out.println(output.totalCars);
            for (final String direction : output.directions) {
                out.println(direction);
            }
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int ferry;
    public int[] cars;
}

class Output {
    public int totalCars;
    public String[] directions;
}

class Process {
    private static final String PORT = "port";
    private static final String STARBOARD = "starboard";

    public Output process(final Input input) {
        final int ferryLength = input.ferry * 100;
        int totalCarLength = 0;

        final State[][] knapsack = new State[input.cars.length + 1][ferryLength + 1];
        final State initial = new State(null, 0, 0, null);
        knapsack[0][0] = initial;
        State best = initial;

        for (int car = 0; car < input.cars.length; car++) {
            final int carLength = input.cars[car];
            totalCarLength += carLength;

            for (int portLength = 0; portLength < ferryLength; portLength++) {
                final State oldState = knapsack[car][portLength];
                if (oldState == null) continue;

                // option 1: use port
                {
                    int newPortLength = portLength + carLength;
                    int newStarboardLength = totalCarLength - newPortLength;
                    final State newState = new State(oldState, newPortLength, newStarboardLength, PORT);

                    if (newState.fit(ferryLength)) {
                        knapsack[car + 1][newPortLength] = newState;
                        best = newState;
                    }
                }

                // option 2: use starboard
                {
                    int newPortLength = portLength;
                    int newStarboardLength = totalCarLength - newPortLength;
                    final State newState = new State(oldState, newPortLength, newStarboardLength, STARBOARD);

                    if (newState.fit(ferryLength)) {
                        knapsack[car + 1][newPortLength] = newState;
                        best = newState;
                    }
                }
            }
        }

        final List<State> states = best.collect();
        final String[] directions = states.stream()
                .map(state -> state.direction)
                .filter(Objects::nonNull)
                .toArray(String[]::new);

        final Output output = new Output();
        output.totalCars = directions.length;
        output.directions = directions;
        return output;
    }
}

class State {
    public final State previous;
    public final int portLength;
    public final int starboardLength;
    public final String direction;

    public State(final State previous, final int portLength, final int starboardLength, final String direction) {
        this.previous = previous;
        this.portLength = portLength;
        this.starboardLength = starboardLength;
        this.direction = direction;
    }

    public boolean fit(final int ferryLength) {
        return portLength <= ferryLength && starboardLength <= ferryLength;
    }

    public List<State> collect() {
        final LinkedList<State> states = new LinkedList<>();
        for (State state = this; state != null; state = state.previous) {
            states.addFirst(state);
        }
        return states;
    }
}
