package uva.uhunt.c4.g4.p12644;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * 12644 - Vocabulary
 * Time limit: 6.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=4392
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        while (in.hasNextInt()) {
            final Input input = new Input();
            input.totalVocabularies = in.nextInt();
            input.totalChallenges = in.nextInt();
            input.vocabularies = new String[input.totalVocabularies];
            input.challenges = new String[input.totalChallenges];
            for (int i = 0; i < input.totalVocabularies; i++) {
                input.vocabularies[i] = in.next();
            }
            for (int i = 0; i < input.totalChallenges; i++) {
                input.challenges[i] = in.next();
            }

            final Output output = process.process(input);
            out.println(output.maxTotalChallenges);
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int totalVocabularies;
    public int totalChallenges;
    public String[] vocabularies;
    public String[] challenges;
}

class Output {
    public int maxTotalChallenges;
}

class Process {
    private final KuhnAlgorithm<String> kuhnAlgorithm = new KuhnAlgorithm<>();

    public Output process(final Input input) {
        final Output output = new Output();

        final int[][] countsPerVocabulary = countLetters(input.vocabularies);
        final int[][] countsPerChallenge = countLetters(input.challenges);

        final UndirectedGraph<String> graph = new UndirectedGraph<>();
        final Set<String> vocabularyVertices = new HashSet<>();
        final Set<String> challengeVertices = new HashSet<>();

        for (int vocabularyId = 0; vocabularyId < input.totalVocabularies; vocabularyId++) {
            final int[] vocabularyCounts = countsPerVocabulary[vocabularyId];
            final String vocabulary = input.vocabularies[vocabularyId];
            final String vocabularyVertex = String.format("V_%d_%s", vocabularyId, vocabulary);

            for (int challengeId = 0; challengeId < input.totalChallenges; challengeId++) {
                final int[] challengeCouts = countsPerChallenge[challengeId];
                final String challenge = input.challenges[challengeId];
                final String challengeVertex = String.format("C_%d_%s", challengeId, challenge);

                final boolean matches = matches(vocabularyCounts, challengeCouts);
                if (matches) {
                    graph.add(vocabularyVertex, challengeVertex);
                    vocabularyVertices.add(vocabularyVertex);
                    challengeVertices.add(challengeVertex);
                }
            }
        }

        final List<List<String>> list = kuhnAlgorithm.findMaximumBipartiteMatching(graph, vocabularyVertices);
        output.maxTotalChallenges = list.size();

        return output;
    }

    private int[][] countLetters(final String[] words) {
        final int[][] countsPerWord = new int[words.length][];
        for (int i = 0; i < words.length; i++) {
            final String word = words[i];
            final int[] counts = countLetters(word);
            countsPerWord[i] = counts;
        }
        return countsPerWord;
    }

    private int[] countLetters(final String word) {
        final int[] counts = new int[26];
        for (final char letter : word.toCharArray()) {
            counts[letter - 'a']++;
        }
        return counts;
    }

    private boolean matches(final int[] vocabulary, final int[] challenge) {
        for (char letter = 'a'; letter <= 'z'; letter++) {
            final int vocabularyCount = vocabulary[letter - 'a'];
            final int challengeCount = challenge[letter - 'a'];
            if (challengeCount > vocabularyCount) return false;
        }
        return true;
    }
}

final class KuhnAlgorithm<V> {
    private final Map<V, V> matched = new HashMap<>();
    private final Set<V> used = new HashSet<>();

    public List<List<V>> findMaximumBipartiteMatching(final UndirectedGraph<V> graph) {
        return findMaximumBipartiteMatching(graph, graph.get());
    }

    public List<List<V>> findMaximumBipartiteMatching(final UndirectedGraph<V> graph, final Set<V> vertices) {
        matched.clear();
        for (final V vertex : vertices) {
            used.clear();
            depthFirstSearch(graph, vertex);
        }

        final List<List<V>> list = new LinkedList<>();
        for (final Map.Entry<V, V> entry : matched.entrySet()) {
            list.add(Arrays.asList(entry.getKey(), entry.getValue()));
        }

        return list;
    }

    private boolean depthFirstSearch(final UndirectedGraph<V> graph, final V vertex) {
        if (used.contains(vertex)) {
            return false;
        }

        used.add(vertex);
        for (final V nextVertex : graph.get(vertex)) {
            final V matchedNextVertex = matched.get(nextVertex);
            final boolean matches = matchedNextVertex == null || depthFirstSearch(graph, matchedNextVertex);
            if (matches) {
                matched.put(nextVertex, vertex);
                return true;
            }
        }
        return false;
    }
}

final class UndirectedGraph<V> {
    public final Map<V, Set<V>> edges = new HashMap<>();

    public void add(final V vertex1, final V vertex2) {
        addUni(vertex1, vertex2);
        addUni(vertex2, vertex1);
    }

    private void addUni(final V fromVertex, final V intoVertex) {
        edges.computeIfAbsent(fromVertex, k -> new HashSet<>()).add(intoVertex);
        edges.computeIfAbsent(intoVertex, k -> new HashSet<>());
    }

    public Set<V> get() {
        return edges.keySet();
    }

    public Set<V> get(final V fromVertex) {
        return edges.getOrDefault(fromVertex, Collections.emptySet());
    }
}
