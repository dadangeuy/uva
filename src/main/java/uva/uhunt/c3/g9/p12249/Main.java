package uva.uhunt.c3.g9.p12249;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

/**
 * 12249 - Overlapping Scenes
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/external/122/12249.pdf
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        final int totalCases = in.nextInt();
        for (int caseId = 1; caseId <= totalCases; caseId++) {
            final Input input = new Input();
            input.caseId = caseId;
            input.totalScenes = in.nextInt();
            input.scenes = new String[input.totalScenes];
            for (int i = 0; i < input.totalScenes; i++) {
                input.scenes[i] = in.next();
            }

            final Output output = process.process(input);
            out.format("Case %d: %d\n", output.caseId, output.movieLength);
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int caseId;
    public int totalScenes;
    public String[] scenes;
}

class Output {
    public int caseId;
    public int movieLength;
}

class Process {
    public Output process(final Input input) {
        final Permutation<String> permutation = new Permutation<>(input.scenes, Comparator.naturalOrder());

        int minMovieLength = Integer.MAX_VALUE;
        for (String[] scenes = permutation.next(); scenes != null; scenes = permutation.next()) {
            final String movie = createMovie(scenes);
            minMovieLength = Math.min(minMovieLength, movie.length());
        }

        final Output output = new Output();
        output.caseId = input.caseId;
        output.movieLength = minMovieLength;
        return output;
    }

    private String createMovie(final String[] scenes) {
        final StringBuilder movie = new StringBuilder();
        for (final String scene : scenes) {
            int commonLength;
            for (commonLength = scene.length(); commonLength >= 0; commonLength--) {
                final String commonMovie = movie.substring(Math.max(0, movie.length() - commonLength), movie.length());
                final String commonScene = scene.substring(0, commonLength);
                if (commonMovie.equals(commonScene)) break;
            }

            final String uncommonScene = scene.substring(commonLength, scene.length());
            movie.append(uncommonScene);
        }

        return movie.toString();
    }
}

final class Permutation<V> {
    private final V[] items;
    private final Comparator<V> comparator;
    private boolean executed;

    public Permutation(final V[] items, final Comparator<V> comparator) {
        this.items = items.clone();
        this.comparator = comparator;
        this.executed = false;

        Arrays.sort(this.items, this.comparator);
    }

    public V[] next() {
        if (!executed) {
            executed = true;
            return items;
        }

        int index = -1;
        for (int i = items.length - 2; i >= 0; i--) {
            final int compare = comparator.compare(items[i], items[i + 1]);
            if (compare < 0) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return null;
        }

        for (int i = items.length - 1; i > index; i--) {
            final int compare = comparator.compare(items[i], items[index]);
            if (compare > 0) {
                swap(i, index);
                break;
            }
        }

        reverse(index + 1, items.length - 1);
        return items;
    }

    private void reverse(int start, int end) {
        while (start < end) {
            swap(start, end);
            start++;
            end--;
        }
    }

    private void swap(int i, int j) {
        V temp = items[i];
        items[i] = items[j];
        items[j] = temp;
    }
}
