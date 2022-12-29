package io.elsci.multinomial;

import java.util.Arrays;

class SymbolFrequencies {
    final int[] frequencies;

    public SymbolFrequencies(int[] frequencies) {
        this.frequencies = frequencies;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SymbolFrequencies that = (SymbolFrequencies) o;
        return Arrays.equals(frequencies, that.frequencies);
    }
    @Override public int hashCode() {
        return Arrays.hashCode(frequencies);
    }
}
