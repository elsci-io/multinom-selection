package io.elsci.multinomial;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MapBasedSymbolSet implements SymbolSet {
    private final Map<Symbol, Integer/*num of letters for each symbol*/> frequency = new HashMap<>();

    MapBasedSymbolSet() {}
    MapBasedSymbolSet(Symbol[] symbols) {
        for (Symbol next : symbols)
            increment(next);
    }
    MapBasedSymbolSet(Map<Symbol, Integer> frequency) {
        this.frequency.putAll(frequency);
    }

    MapBasedSymbolSet(Alphabet a, int[] frequencies) {
        if(a.probabilities.length != frequencies.length)
            throw new IllegalArgumentException("For each letter of alphabet we need to have its frequency");
        for (int i = 0; i < frequencies.length; i++) {
            int num = frequencies[i];
            if (num > 0)
                this.frequency.put(new Symbol(a, i), num);
        }
    }

    @Override
    public Map<Symbol, Integer> getSymbolFrequencies() {
        return Collections.unmodifiableMap(frequency);
    }

    @Override public void increment(Symbol s) {
        Integer cnt = frequency.getOrDefault(s, 0);
        frequency.put(s, cnt + 1);
    }
    @Override public int getWordLength() {
        int result = 0;
        for (Integer v : frequency.values())
            result += v;
        return result;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapBasedSymbolSet symbolSet = (MapBasedSymbolSet) o;
        return frequency.equals(symbolSet.frequency);
    }
    @Override public int hashCode() {
        return Objects.hash(frequency);
    }
    @Override public String toString() {
        return "MapBased" + frequency;
    }
}
