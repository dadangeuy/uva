package uva.uhunt.c3.g1.p1281;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * 1281 - Bus Tour
 * Time limit: 20.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=3894
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        for (int caseId = 1; in.hasNextInt(); caseId++) {
            final Input input = new Input();
            input.caseId = caseId;
            input.totalLocations = in.nextInt();
            input.totalRoads = in.nextInt();
            input.roads = new int[input.totalRoads][3];
            for (int i = 0; i < input.totalRoads; i++) {
                input.roads[i][0] = in.nextInt();
                input.roads[i][1] = in.nextInt();
                input.roads[i][2] = in.nextInt();
            }

            final Output output = process.process(input);
            out.format("Case %d: %d\n", output.caseId, output.totalTimes);
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int caseId;
    public int totalLocations;
    public int totalRoads;
    public int[][] roads;
}

class Output {
    public int caseId;
    public int totalTimes;
}

class Process {
    private final FloydWarshallAlgorithm floydWarshallAlgorithm = new FloydWarshallAlgorithm();
    private final HeldKarpAlgorithm heldKarpAlgorithm = new HeldKarpAlgorithm();

    public Output process(final Input input) {
        final int headquarter = 0;
        final int attraction = input.totalLocations - 1;
        final int[] locations = sequence(headquarter, attraction);
        final int locationsMask = Mask.mask(locations);
        final int[] hotels = sequence(1, input.totalLocations - 2);
        final int hotelsMask = Mask.mask(hotels);

        final int[][] APSP = floydWarshallAlgorithm.findAllPairsShortestPaths(
            input.totalLocations,
            input.roads
        );

        if (input.totalLocations == 3) {
            final Output output = new Output();
            output.caseId = input.caseId;
            output.totalTimes = 2 * (APSP[headquarter][1] + APSP[1][attraction]);
            return output;
        }

        final int[][] headquarterSSSHP = heldKarpAlgorithm.findSingleSourceShortestHamiltonianPaths(
            headquarter,
            input.totalLocations,
            APSP
        );
        final int[][] attractionSSSHP = heldKarpAlgorithm.findSingleSourceShortestHamiltonianPaths(
            attraction,
            input.totalLocations,
            APSP
        );

        final List<Integer> firstHotelsMasks = new LinkedList<>();
        for (int mask = 0; mask <= locationsMask; mask++) {
            if (Mask.contains(mask, headquarter)) continue;
            if (Mask.contains(mask, attraction)) continue;
            if (Mask.hammingWeight(mask) != (hotels.length / 2)) continue;
            firstHotelsMasks.add(mask);
        }

        // store shortest paths from:
        // headquarter -> [hotels] -> attraction
        // for every possible first hotels
        final int[] pickupShortestPaths = new int[locationsMask + 1];
        Arrays.fill(pickupShortestPaths, Integer.MAX_VALUE);

        // store shortest paths from:
        // headquarter -> [hotels] -> attraction
        // for every possible first hotels
        final int[] dropoffShortestPaths = new int[locationsMask + 1];
        Arrays.fill(dropoffShortestPaths, Integer.MAX_VALUE);

        int minTotalTimes = Integer.MAX_VALUE;

        for (final int firstHotelsMask : firstHotelsMasks) {
            final int secondHotelsMask = (~firstHotelsMask) & hotelsMask;

            // find optimal pickup path from:
            // headquarter -> [first hotels] -> last first hotel -> [second hotels] -> attraction
            for (int lastFirstHotel : hotels) {
                if (!Mask.contains(firstHotelsMask, lastFirstHotel)) continue;

                final int firstMask = Mask.add(firstHotelsMask, headquarter, lastFirstHotel);
                final int firstPath = headquarterSSSHP[firstMask][lastFirstHotel];

                final int secondMask = Mask.add(secondHotelsMask, lastFirstHotel, attraction);
                final int secondPath = attractionSSSHP[secondMask][lastFirstHotel];

                final int oldPath = pickupShortestPaths[firstHotelsMask];
                final int newPath = firstPath + secondPath;
                pickupShortestPaths[firstHotelsMask] = Math.min(oldPath, newPath);
            }

            // find optimal dropoff path from:
            // attraction -> [first hotels] -> last first hotel -> [second hotels] -> headquarter
            for (final int lastFirstHotel : hotels) {
                if (!Mask.contains(firstHotelsMask, lastFirstHotel)) continue;

                final int firstMask = Mask.add(firstHotelsMask, attraction, lastFirstHotel);
                final int firstTotalWeights = attractionSSSHP[firstMask][lastFirstHotel];

                final int secondMask = Mask.add(secondHotelsMask, lastFirstHotel, headquarter);
                final int secondTotalWeights = headquarterSSSHP[secondMask][lastFirstHotel];

                final int oldPath = dropoffShortestPaths[firstHotelsMask];
                final int newPath = firstTotalWeights + secondTotalWeights;
                dropoffShortestPaths[firstHotelsMask] = Math.min(oldPath, newPath);
            }

            final int totalTimes = pickupShortestPaths[firstHotelsMask] + dropoffShortestPaths[firstHotelsMask];
            minTotalTimes = Math.min(minTotalTimes, totalTimes);
        }

        final Output output = new Output();
        output.caseId = input.caseId;
        output.totalTimes = minTotalTimes;
        return output;
    }

    private int[] sequence(final int from, final int until) {
        final int length = until - from + 1;
        final int[] sequence = new int[until - from + 1];
        for (int i = 0; i < length; i++) sequence[i] = from + i;
        return sequence;
    }
}

class FloydWarshallAlgorithm {
    public int[][] findAllPairsShortestPaths(final int totalVertices, final int[][] edges) {
        final int[][] shortestPaths = new int[totalVertices][totalVertices];
        fill(shortestPaths, Integer.MAX_VALUE);

        for (int vertex = 0; vertex < totalVertices; vertex++) {
            shortestPaths[vertex][vertex] = 0;
        }

        for (final int[] edge : edges) {
            shortestPaths[edge[0]][edge[1]] = shortestPaths[edge[1]][edge[0]] = edge[2];
        }

        for (int alternativeVertex = 0; alternativeVertex < totalVertices; alternativeVertex++) {
            for (int originVertex = 0; originVertex < totalVertices; originVertex++) {
                if (shortestPaths[originVertex][alternativeVertex] == Integer.MAX_VALUE) continue;
                for (int destinationVertex = 0; destinationVertex < totalVertices; destinationVertex++) {
                    if (shortestPaths[alternativeVertex][destinationVertex] == Integer.MAX_VALUE) continue;

                    final int oldPath = shortestPaths[originVertex][destinationVertex];
                    final int newPath = shortestPaths[originVertex][alternativeVertex] + shortestPaths[alternativeVertex][destinationVertex];
                    shortestPaths[originVertex][destinationVertex] = Math.min(oldPath, newPath);
                }
            }
        }

        return shortestPaths;
    }

    private void fill(final int[][] array2, final int value) {
        for (final int[] array1 : array2) {
            Arrays.fill(array1, value);
        }
    }
}

final class HeldKarpAlgorithm {
    public int[][] findSingleSourceShortestHamiltonianPaths(
        final int originVertex,
        final int totalVertices,
        final int[][] paths
    ) {
        final int totalMasks = 1 << totalVertices;
        final int[][] shortestHamiltonianPaths = new int[totalMasks][totalVertices];
        fill(shortestHamiltonianPaths, Integer.MAX_VALUE);

        for (int destinationVertex = 0; destinationVertex < totalVertices; destinationVertex++) {
            if (originVertex == destinationVertex) continue;

            final int visitedMask = Mask.mask(originVertex, destinationVertex);
            shortestHamiltonianPaths[visitedMask][destinationVertex] = paths[originVertex][destinationVertex];
        }

        for (int endMask = 0; endMask < totalMasks; endMask++) {
            for (int endVertex = 0; endVertex < totalVertices; endVertex++) {
                if (!Mask.contains(endMask, endVertex)) continue;

                final int prevMask = Mask.remove(endMask, endVertex);
                for (int prevVertex = 0; prevVertex < totalVertices; prevVertex++) {
                    if (!Mask.contains(prevMask, prevVertex)) continue;
                    if (shortestHamiltonianPaths[prevMask][prevVertex] == Integer.MAX_VALUE) continue;
                    if (paths[prevVertex][endVertex] == Integer.MAX_VALUE) continue;

                    final int oldPath = shortestHamiltonianPaths[endMask][endVertex];
                    final int newPath = shortestHamiltonianPaths[prevMask][prevVertex] + paths[prevVertex][endVertex];
                    shortestHamiltonianPaths[endMask][endVertex] = Math.min(oldPath, newPath);
                }
            }
        }

        return shortestHamiltonianPaths;
    }

    private void fill(final int[][] array2, final int value) {
        for (final int[] array1 : array2) {
            Arrays.fill(array1, value);
        }
    }
}

final class Mask {
    public static int mask(final int... items) {
        return add(0, items);
    }

    public static int add(int mask, final int... items) {
        for (final int item : items) mask = add(mask, item);
        return mask;
    }

    private static int add(final int mask, final int item) {
        return mask | (1 << item);
    }

    public static int remove(int mask, final int... items) {
        for (final int item : items) mask = remove(mask, item);
        return mask;
    }

    private static int remove(final int mask, final int item) {
        return mask ^ (1 << item);
    }

    public static boolean contains(final int mask, final int item) {
        return ((mask >> item) & 1) > 0;
    }

    public static int hammingWeight(final int mask) {
        return Integer.bitCount(mask);
    }
}
