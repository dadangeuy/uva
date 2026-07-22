package uva.uhunt.c2.g7.p297;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * 297 - Quadtrees
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=233
 */
public class Main {
    public static void main(final String... args) {
        final Scanner in = new Scanner(new BufferedInputStream(System.in));
        final PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        final Process process = new Process();

        final int totalCases = in.nextInt();
        for (int i = 0; i < totalCases; i++) {
            final Input input = new Input();
            input.quadtree1 = in.next();
            input.quadtree2 = in.next();

            final Output output = process.process(input);
            out.format("There are %d black pixels.\n", output.totalBlackPixels);
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public String quadtree1;
    public String quadtree2;
}

class Output {
    public int totalBlackPixels;
}

class Process {
    private static final char PARENT = 'p';
    private static final char BLACK = 'f';
    private static final char WHITE = 'e';

    public Output process(final Input input) {
        final QTree tree1 = createTree(createQueue(input.quadtree1), 1);
        final QTree tree2 = createTree(createQueue(input.quadtree2), 1);

        final int maxDepth1 = getMaxDepth(tree1);
        final int maxDepth2 = getMaxDepth(tree2);
        final int maxDepth = Math.max(maxDepth1, maxDepth2);

        extendTree(tree1, maxDepth);
        extendTree(tree2, maxDepth);

        final String leaves1 = collectLeaves(tree1);
        final String leaves2 = collectLeaves(tree2);
        final String addedLeaves = add(leaves1, leaves2);

        final Output output = new Output();
        output.totalBlackPixels= getTotalBlackPixels(addedLeaves);
        return output;
    }

    private Queue<Character> createQueue(final String string) {
        final Queue<Character> queue = new LinkedList<>();
        for (final char letter : string.toCharArray()) {
            queue.add(letter);
        }
        return queue;
    }

    private QTree createTree(final Queue<Character> queue, int depth) {
        if (queue.isEmpty()) return null;

        final QTree root = new QTree();
        root.depth = depth;
        root.value = queue.remove();

        if (root.value == 'p') {
            for (int i = 0; i < 4; i++) {
                root.children[i] = createTree(queue, depth + 1);
            }
        }

        return root;
    }

    private int getMaxDepth(final QTree tree) {
        int maxDepth = tree.depth;
        for (final QTree child : tree.children) {
            if (child == null) continue;
            final int depth = getMaxDepth(child);
            maxDepth = Math.max(maxDepth, depth);
        }

        return maxDepth;
    }

    private void extendTree(final QTree tree, final int targetDepth) {
        if (tree.depth == targetDepth) return;

        for (int i = 0; i < 4; i++) {
            if (tree.children[i] == null) {
                final QTree child = new QTree();
                child.depth = tree.depth + 1;
                child.value = tree.value;

                tree.children[i] = child;
            }
            extendTree(tree.children[i], targetDepth);
        }
        tree.value = PARENT;
    }

    private String collectLeaves(final QTree tree) {
        final LinkedList<QTree> leaves = new LinkedList<>();
        collectLeaves(tree, leaves);

        final StringBuilder text = new StringBuilder();
        for (final QTree leaf : leaves) {
            text.append(leaf.value);
        }

        return text.toString();
    }

    private void collectLeaves(final QTree tree, final LinkedList<QTree> leaves) {
        if (tree.value == PARENT) {
            for (int i = 0; i < 4; i++) {
                collectLeaves(tree.children[i], leaves);
            }
        } else {
            leaves.add(tree);
        }
    }

    private String add(final String leaves1, final String leaves2) {
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < leaves1.length(); i++) {
            final char leaf1 = leaves1.charAt(i);
            final char leaf2 = leaves2.charAt(i);

            if (leaf1 == BLACK || leaf2 == BLACK) result.append(BLACK);
            else result.append(WHITE);
        }

        return result.toString();
    }

    private int getTotalBlackPixels(final String leaves) {
        final int size = 1024 / leaves.length();
        int total = 0;
        for (char leaf : leaves.toCharArray()) {
            if (leaf == BLACK) {
                total++;
            }
        }

        return total * size;
    }
}

class QTree {
    public int depth = 0;
    public char value = 0;
    public QTree[] children = new QTree[4];
}
