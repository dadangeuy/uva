package uva.uhunt.c3.g2.p12862;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * 12862 - Intrepid climber
 * Time limit: 2.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=4727
 */
public class Main {
    public static void main(final String... args) throws IOException {
        final BufferedIO io = new BufferedIO(System.in, System.out);
        final Process process = new Process();

        for (int caseId = 1; ; caseId++) {
            final String[] line1 = io.readLine(" ");
            final boolean isEOF = line1 == null;
            if (isEOF) break;

            final Input input = new Input();
            input.caseId = caseId;
            input.totalLandmarks = Integer.parseInt(line1[0]);
            input.totalFriends = Integer.parseInt(line1[1]);
            input.tracks = new Track[input.totalLandmarks - 1];
            for (int i = 0; i < input.tracks.length; i++) {
                final String[] line2 = io.readLine(" ");
                input.tracks[i] = new Track();
                input.tracks[i].upperLandmark = Integer.parseInt(line2[0]);
                input.tracks[i].lowerLandmark = Integer.parseInt(line2[1]);
                input.tracks[i].energy = Integer.parseInt(line2[2]);
            }
            final String[] line3 = io.readLine(" ");
            input.friendLandmarks = new int[input.totalFriends];
            for (int i = 0; i < input.totalFriends; i++) {
                input.friendLandmarks[i] = Integer.parseInt(line3[i]);
            }

            final Output output = process.process(input);
            io.write("%d\n", output.totalEnergies);
        }

        io.close();
    }
}

class Input {
    public int caseId;
    public int totalLandmarks;
    public int totalFriends;
    public Track[] tracks;
    public int[] friendLandmarks;
}

class Output {
    public int caseId;
    public int totalEnergies;
}

class Process {
    private static final int TOP_LANDMARK = 1;

    public Output process(final Input input) {
        final Mountain mountain = buildMountain(input.totalLandmarks, input.tracks);
        final Mountain filteredMountain = excludeUnvisitedLandmarks(input.totalLandmarks, input.friendLandmarks, mountain);

        final int totalEnergies = getTotalEnergies(filteredMountain);
        final int[] totalEnergiesPerLandmark = getTotalEnergiesPerLandmark(filteredMountain, input.totalLandmarks);
        final int maxFriendTotalEnergies = getMaxLandmarkTotalEnergies(totalEnergiesPerLandmark, input.friendLandmarks);

        final Output output = new Output();
        output.caseId = input.caseId;
        output.totalEnergies = totalEnergies - maxFriendTotalEnergies;
        return output;
    }

    private Mountain buildMountain(final int totalLandmarks, final Track[] tracks) {
        final Mountain mountain = new Mountain(totalLandmarks);
        for (final Track track : tracks) mountain.add(track);
        return mountain;
    }

    private Mountain excludeUnvisitedLandmarks(
        final int totalLandmarks,
        final int[] destinations,
        final Mountain mountain
    ) {
        final Mountain filteredMountain = new Mountain(totalLandmarks);
        final boolean[] visitedLandmarks = new boolean[totalLandmarks + 1];

        for (final int destination : destinations) {
            for (
                int landmark = destination;
                mountain.upperTrack(landmark).isPresent();
                landmark = mountain.upperTrack(landmark).get().upperLandmark
            ) {
                if (visitedLandmarks[landmark]) break;

                final Track track = mountain.upperTrack(landmark).get();
                filteredMountain.add(track);
                visitedLandmarks[landmark] = true;
            }
        }

        return filteredMountain;
    }

    private int getTotalEnergies(final Mountain mountain) {
        return dfsTotalEnergies(mountain, TOP_LANDMARK);
    }

    private int dfsTotalEnergies(
        final Mountain mountain,
        final int landmark
    ) {
        int totalEnergies = 0;
        for (final Track track : mountain.lowerTracks(landmark)) {
            totalEnergies += track.energy;
            totalEnergies += dfsTotalEnergies(mountain, track.lowerLandmark);
        }
        return totalEnergies;
    }

    private int[] getTotalEnergiesPerLandmark(
        final Mountain mountain,
        final int totalLandmarks
    ) {
        final int[] totalEnergiesPerLandmark = new int[totalLandmarks + 1];
        dfsTotalEnergiesPerLandmark(mountain, TOP_LANDMARK, 0, totalEnergiesPerLandmark);
        return totalEnergiesPerLandmark;
    }

    private void dfsTotalEnergiesPerLandmark(
        final Mountain mountain,
        final int landmark,
        final int totalEnergies,
        final int[] totalEnergiesPerLandmark
    ) {
        totalEnergiesPerLandmark[landmark] = totalEnergies;
        for (final Track track : mountain.lowerTracks(landmark)) {
            dfsTotalEnergiesPerLandmark(
                mountain,
                track.lowerLandmark,
                totalEnergies + track.energy,
                totalEnergiesPerLandmark
            );
        }
    }

    private int getMaxLandmarkTotalEnergies(
        final int[] totalEnergiesPerLandmark,
        final int[] landmarks
    ) {
        return Arrays.stream(landmarks)
            .map(landmark -> totalEnergiesPerLandmark[landmark])
            .max()
            .orElse(0);
    }
}

final class Track {
    public int upperLandmark;
    public int lowerLandmark;
    public int energy;
}

final class Mountain {
    public final LinkedList<Track>[] lowerTracksPerLandmark;
    public final Track[] upperTrackPerLandmark;

    public Mountain(final int totalLandmarks) {
        this.lowerTracksPerLandmark = new LinkedList[totalLandmarks + 1];
        for (int landmark = 1; landmark <= totalLandmarks; landmark++) {
            this.lowerTracksPerLandmark[landmark] = new LinkedList<>();
        }
        this.upperTrackPerLandmark = new Track[totalLandmarks + 1];
    }

    public void add(final Track track) {
        lowerTracksPerLandmark[track.upperLandmark].addLast(track);
        upperTrackPerLandmark[track.lowerLandmark] = track;
    }

    public List<Track> lowerTracks(final int landmark) {
        final LinkedList<Track> children = lowerTracksPerLandmark[landmark];
        return children == null ? Collections.emptyList() : children;
    }

    public Optional<Track> upperTrack(final int landmark) {
        return Optional.ofNullable(upperTrackPerLandmark[landmark]);
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
