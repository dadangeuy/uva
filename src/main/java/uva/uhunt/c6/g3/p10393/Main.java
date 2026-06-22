package uva.uhunt.c6.g3.p10393;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * 10393 - The One-Handed Typist
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=1334
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        while (in.hasNextInt()) {
            final Input input = new Input();
            input.totalNonUsableFingers = in.nextInt();
            input.totalWords = in.nextInt();
            input.nonUsableFingers = new int[input.totalNonUsableFingers];
            for (int i = 0; i < input.nonUsableFingers.length; i++) {
                input.nonUsableFingers[i] = in.nextInt();
            }
            input.words = new String[input.totalWords];
            in.nextLine();
            for (int i = 0; i < input.words.length; i++) {
                input.words[i] = in.nextLine();
            }

            final Output output = process.process(input);
            out.println(output.totalLongestTypeableWords);
            for (final String word : output.longestTypeableWords) {
                out.println(word);
            }
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int totalNonUsableFingers;
    public int totalWords;
    public int[] nonUsableFingers;
    public String[] words;
}

class Output {
    public int totalLongestTypeableWords;
    public String[] longestTypeableWords;
}

class Process {
    private static final String[] lettersPerFingers = new String[]{
            "",
            "qaz",
            "wsx",
            "edc",
            "rfvtgb",
            " ",
            " ",
            "yhnujm",
            "ik",
            "ol.",
            "p;/"
    };

    public Output process(final Input input) {
        final Set<Integer> unusableFingers = new HashSet<>(10);
        for (final int finger : input.nonUsableFingers) {
            unusableFingers.add(finger);
        }

        final Set<Character> typeableLetters = new HashSet<>(32);
        for (int finger = 1; finger <= 10; finger++) {
            if (unusableFingers.contains(finger)) continue;

            for (final char letter : lettersPerFingers[finger].toCharArray()) {
                typeableLetters.add(letter);
            }
        }

        final List<String> typeableWords = new LinkedList<>();
        int maxLength = 0;
        for (final String word : input.words) {
            if (word.length() < maxLength) continue;
            if (!isTypeable(typeableLetters, word)) continue;

            if (word.length() > maxLength) {
                maxLength = word.length();
                typeableWords.clear();
                typeableWords.add(word);
            } else {
                typeableWords.add(word);
            }
        }

        final Output output = new Output();
        output.longestTypeableWords = typeableWords.stream().distinct().sorted().toArray(String[]::new);
        output.totalLongestTypeableWords = output.longestTypeableWords.length;
        return output;
    }

    private boolean isTypeable(final Set<Character> typeableLetters, final String word) {
        for (final char letter : word.toCharArray()) {
            if (!typeableLetters.contains(letter)) return false;
        }
        return true;
    }
}
