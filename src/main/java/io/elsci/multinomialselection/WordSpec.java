package io.elsci.multinomialselection;

import java.util.Map;

/**
 * Requirements for word generation: how many symbols can a word have, which alphabets should we sample from.
 */
public class WordSpec {
    final Map<Alphabet, Integer> numberOfSymbols;

    /**
     * @param numberOfSymbols how many Symbols of each Alphabet we have (want to pull)
     */
    public WordSpec(Map<Alphabet, Integer> numberOfSymbols) {
        this.numberOfSymbols = numberOfSymbols;
    }
}
