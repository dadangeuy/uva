package uva.uhunt.c6.g4.p10854;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

/**
 * 10854 - Number of Paths
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=1795
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        final int totalCases = in.nextInt();
        for (int i = 0; i < totalCases; i++) {
            final LinkedList<String> keywords = new LinkedList<>();
            do {
                keywords.addLast(in.next());
            } while (!"ENDPROGRAM".equals(keywords.getLast()));

            final Input input = new Input();
            input.keywords = keywords.toArray(new String[0]);

            final Output output = process.process(input);
            out.println(output.totalPaths);
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public String[] keywords;
}

class Output {
    public long totalPaths;
}

class Process {
    public Output process(final Input input) {
        final Stack<String> stack = new Stack<>();
        final Map<String, List<String>> tree = new HashMap<>();
        tree.put(null, new LinkedList<>());

        for (int idx = 0; idx < input.keywords.length; idx++) {
            final String keyword = input.keywords[idx];
            final String line = String.format("%s:%d", keyword, idx);

            if (line.startsWith("IF")) {
                final String parentLine = stack.isEmpty() ? null : stack.peek();
                tree.get(parentLine).add(line);
                tree.putIfAbsent(line, new LinkedList<>());

                stack.push(line);
            } else if (line.startsWith("ELSE")) {
                stack.pop();

                final String parentLine = stack.isEmpty() ? null : stack.peek();
                tree.get(parentLine).add(line);
                tree.putIfAbsent(line, new LinkedList<>());

                stack.push(line);
            } else if (line.startsWith("END_IF")) {
                stack.pop();
            }
        }

        final Output output = new Output();
        output.totalPaths = getTotalPaths(tree, null);
        return output;
    }

    private long getTotalPaths(final Map<String, List<String>> tree, final String parent) {
        final List<String> children = tree.get(parent);
        final LinkedList<List<String>> groups = new LinkedList<>();
        for (final String child : children) {
            if (child.startsWith("IF")) {
                groups.addLast(new LinkedList<>());
            }
            groups.getLast().add(child);
        }

        long totalPaths = 1;
        for (final List<String> group : groups) {
            long groupTotalPaths = 0;
            for (final String child : group) {
                groupTotalPaths += getTotalPaths(tree, child);
            }
            totalPaths *= groupTotalPaths;
        }

        return totalPaths;
    }
}
