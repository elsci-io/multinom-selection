package io.elsci.multinomial;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class ArrayBasedSymbolSet implements SymbolSet {
    final Alphabet alphabet;
    final int[] symbolFrequencies;

    ArrayBasedSymbolSet(Alphabet alphabet, int[] symbolFrequencies) {
        this.alphabet = alphabet;
        this.symbolFrequencies = symbolFrequencies;
    }

    public void increment(Symbol s) {
        symbolFrequencies[s.letterIndex]++;
    }
    public int getWordLength() {
        return MathUtils.sum(symbolFrequencies);
    }

    @Override
    public Map<Symbol, Integer> getSymbolFrequencies() {
        Map<Symbol, Integer> result = new HashMap<>();
        for (int i = 0, symbolFrequenciesLength = symbolFrequencies.length; i < symbolFrequenciesLength; i++) {
            if (symbolFrequencies[i] != 0) {
                result.put(alphabet.getSymbol(i), symbolFrequencies[i]);
            }
        }
        return result;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrayBasedSymbolSet that = (ArrayBasedSymbolSet) o;
        return alphabet.equals(that.alphabet) && Arrays.equals(symbolFrequencies, that.symbolFrequencies);
    }
    @Override public int hashCode() {
        int result = Objects.hash(alphabet);
        result = 31 * result + Arrays.hashCode(symbolFrequencies);
        return result;
    }
    public String toString() {
        return new MapBasedSymbolSet(getSymbolFrequencies()).toString();
    }
}
