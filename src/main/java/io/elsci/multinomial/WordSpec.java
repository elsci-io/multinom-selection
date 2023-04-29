package io.elsci.multinomial;

import java.util.Map;

public class WordSpec {
    /**
     * How many Symbols of each Alphabet we have (want to pull)
     */
    final Map<Alphabet, Integer> numberOfSymbols;

    public WordSpec(Map<Alphabet, Integer> numberOfSymbols) {
        this.numberOfSymbols = numberOfSymbols;
    }
}
