package io.elsci.multinomial;

import java.util.HashMap;
import java.util.Map;

/**
 * A combination of {@link Symbol}s of different {@link Alphabet}. Represents the result of the calculations, but
 * doesn't have the probability inside it - for that look at {@link Word}.
 */
public interface SymbolSet {
    void increment(Symbol s);
    int getWordLength();
    Map<Symbol, Integer> getSymbolFrequencies();

    /**
     * Note, that if 2 sets have same symbols, they'll be overwritten.
     *
     * @param sets sets to concatenate
     * @return creates an "uber" set that contains all the {@link Symbol}s from all the specified sets
     */
    static SymbolSet concat(SymbolSet... sets) {
        Map<Symbol, Integer> map = new HashMap<>();
        for (SymbolSet symbolSet : sets)
            //
            map.putAll(symbolSet.getSymbolFrequencies());
        return new MapBasedSymbolSet(map);
    }
}
