package uva.uhunt.c3.g7.p11147;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

/**
 * 11147 - KuPellaKeS BST
 * Time limit: 3.000 seconds
 * https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2088
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
            input.totalNumbers = in.nextInt();
            input.numbers = new int[input.totalNumbers];
            for (int i = 0; i < input.totalNumbers; i++) {
                input.numbers[i] = in.nextInt();
            }

            final Output output = process.process(input);
            out.format("Case #%d: %s\n", output.caseId, output.bst);
        }

        in.close();
        out.flush();
        out.close();
    }
}

class Input {
    public int caseId;
    public int totalNumbers;
    public int[] numbers;
}

class Output {
    public int caseId;
    public String bst;
}

class Process {
    public Output process(final Input input) {
        final Output output = new Output();
        output.caseId = input.caseId;

        final int[] numbers = input.numbers;
        Arrays.sort(numbers);

        final Tree bst = createKupellakesBST(numbers);
        output.bst = bst.toString();

        return output;
    }

    private Tree createKupellakesBST(final int[] numbers) {
        if (numbers.length == 0) return null;
        else if (numbers.length == 1) return new Tree(numbers[0]);

        final int rootIndex = findRootIndex(numbers);
        final int[] leftNumbers = Arrays.copyOfRange(numbers, 0, rootIndex);
        final int[] rightNumbers = Arrays.copyOfRange(numbers, rootIndex + 1, numbers.length);

        final Tree root = new Tree(numbers[rootIndex]);
        root.left = createKupellakesBST(leftNumbers);
        root.right = createKupellakesBST(rightNumbers);
        return root;
    }

    private int findRootIndex(final int[] numbers) {
        int leftSum = 0;
        int rightSum = sum(numbers);
        int minimumDifference = Integer.MAX_VALUE;
        int middle = 0;

        for (int i = 0; i < numbers.length; i++) {
            rightSum -= numbers[i];
            final int difference = Math.abs(leftSum - rightSum);
            leftSum += numbers[i];

            final boolean strictlyIncreasingRight = i + 1 == numbers.length || numbers[i + 1] > numbers[i];
            final boolean minimizeDifference = difference < minimumDifference;
            if (strictlyIncreasingRight && minimizeDifference) {
                minimumDifference = difference;
                middle = i;
            }
        }

        return middle;
    }

    private int sum(final int[] numbers) {
        int sum = 0;
        for (final int number : numbers) sum += number;
        return sum;
    }
}

class Tree {
    public final int value;
    public Tree left;
    public Tree right;

    public Tree(final int value) {
        this.value = value;
    }

    public String toString() {
        final StringBuilder builder = new StringBuilder();
        buildString(builder, this);
        return builder.toString();
    }

    private void buildString(final StringBuilder builder, final Tree tree) {
        if (tree == null) return;

        builder.append(tree.value);
        if (tree.left != null || tree.right != null) builder.append('(');
        buildString(builder, tree.left);
        if (tree.left != null && tree.right != null) builder.append(',');
        buildString(builder, tree.right);
        if (tree.left != null || tree.right != null) builder.append(')');
    }
}
