package io.elsci.multinomial;

import java.util.Arrays;

/**
 * Need it for its equals() & hashCode() in order to put it into {@link java.util.HashSet} and check for duplicates.
 */
class IntArray {
    final int[] frequencies;

    public IntArray(int[] frequencies) {
        this.frequencies = frequencies;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntArray that = (IntArray) o;
        return Arrays.equals(frequencies, that.frequencies);
    }
    @Override public int hashCode() {
        return Arrays.hashCode(frequencies);
    }
}
