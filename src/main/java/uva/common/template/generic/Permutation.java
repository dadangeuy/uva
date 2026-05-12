package uva.common.template.generic;

import java.util.Arrays;
import java.util.Comparator;

@SuppressWarnings("unused")
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
