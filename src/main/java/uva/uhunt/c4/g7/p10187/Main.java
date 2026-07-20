package uva.uhunt.c4.g7.p10187;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

/**
 * 10187 - From Dusk Till Dawn
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=1128
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        final int totalCases = in.nextInt();
        for (int caseId = 1; caseId <= totalCases; caseId++) {
            final Input input = new Input();
            input.totalSchedules = in.nextInt();
            input.schedules = new Schedule[input.totalSchedules];
            for (int i = 0; i < input.totalSchedules; i++) {
                input.schedules[i] = new Schedule();
                input.schedules[i].origin = in.next();
                input.schedules[i].destination = in.next();
                input.schedules[i].departure = in.nextInt();
                input.schedules[i].duration = in.nextInt();
            }
            input.origin = in.next();
            input.destination = in.next();

            final Output output = process.process(input);
            if (output.isPossible) {
                out.format("Test Case %d.\nVladimir needs %d litre(s) of blood.\n", caseId, output.totalBloods);
            } else {
                out.format("Test Case %d.\nThere is no route Vladimir can take.\n", caseId);
            }
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int totalSchedules;
    public Schedule[] schedules;
    public String origin;
    public String destination;
}

class Output {
    public final boolean isPossible;
    public final int totalBloods;

    public Output(final boolean isPossible, final int totalBloods) {
        this.isPossible = isPossible;
        this.totalBloods = totalBloods;
    }
}

class Process {
    private static final Output IMPOSSIBLE = new Output(false, 0);

    public Output process(final Input input) {
        final Graph graph = createGraph(input.schedules);
        final PriorityQueue<Route> queue = new PriorityQueue<>(Route.PRIORITY);
        final Map<String, Route> routePerCity = new HashMap<>();

        final Route initialRoute = new Route(input.origin);
        queue.add(initialRoute);
        routePerCity.put(initialRoute.city, initialRoute);

        while (!queue.isEmpty()) {
            final Route route = queue.poll();

            final boolean isFinished = input.destination.equals(route.city);
            if (isFinished) return new Output(true, route.totalBloods);

            for (final String nextCity : graph.get(route.city)) {
                for (final Schedule nextSchedule : graph.get(route.city, nextCity)) {
                    final Route nextRoute = route.next(nextSchedule);
                    final Route oldRoute = routePerCity.get(nextCity);

                    if (nextRoute.isBetterThan(oldRoute)) {
                        queue.add(nextRoute);
                        routePerCity.put(nextCity, nextRoute);
                    }
                }
            }
        }

        return IMPOSSIBLE;
    }

    private Graph createGraph(final Schedule[] schedules) {
        final Graph graph = new Graph();
        for (final Schedule schedule : schedules) {
            if (!schedule.isTravelable()) continue;
            graph.add(schedule);
        }
        return graph;
    }
}

class Graph {
    public Map<String, Map<String, List<Schedule>>> graph = new HashMap<>();

    public void add(final Schedule schedule) {
        graph.computeIfAbsent(schedule.origin, k -> new HashMap<>()).computeIfAbsent(schedule.destination, k -> new LinkedList<>()).add(schedule);
    }

    public Set<String> get(final String origin) {
        return graph.getOrDefault(origin, Collections.emptyMap()).keySet();
    }

    public List<Schedule> get(final String origin, final String destination) {
        return graph.getOrDefault(origin, Collections.emptyMap()).getOrDefault(destination, Collections.emptyList());
    }
}

class Schedule {
    public String origin;
    public String destination;
    public int departure;
    public int duration;

    public int arrival() {
        return departure + duration;
    }

    public boolean isTravelable() {
        final boolean inPeriod1 = within(departure, 0, 6) && within(arrival(), 0, 6);
        final boolean inPeriod2 = within(departure, 18, 30) && within(arrival(), 18, 30);
        return inPeriod1 || inPeriod2;
    }

    private boolean within(final int value, final int from, final int until) {
        return from <= value && value <= until;
    }
}

class Route {
    public static final Comparator<Route> PRIORITY = Comparator.nullsLast(Comparator.comparingInt((Route r) -> r.totalBloods).thenComparingInt((Route r) -> r.totalHours));

    public final Route previous;
    public final String city;
    public final int totalHours;
    public final int totalBloods;

    public Route(final String city) {
        this(null, city, 0, 0);
    }

    private Route(final Route previous, final String city, final int totalHours, final int totalBloods) {
        this.previous = previous;
        this.city = city;
        this.totalHours = totalHours;
        this.totalBloods = totalBloods;
    }

    public Route next(final Schedule schedule) {
        final int waiting = Math.floorMod(schedule.departure - totalHours, 24);
        final int traveling = schedule.duration;

        final int prevTime = totalHours % 24;
        final int nextTime = prevTime + waiting + traveling;

        final boolean isFirst = previous == null;
        final boolean isBloodRequired = !isFirst && (within(12, prevTime, nextTime) || within(36, prevTime, nextTime));

        return new Route(this, schedule.destination, totalHours + waiting + traveling, totalBloods + (isBloodRequired ? 1 : 0));
    }

    public boolean isBetterThan(final Route o) {
        return PRIORITY.compare(this, o) < 0;
    }

    private boolean within(final int value, final int from, final int until) {
        return from <= value && value <= until;
    }
}
