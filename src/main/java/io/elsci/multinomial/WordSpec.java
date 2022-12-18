package io.elsci.multinomial;

import java.util.Map;

public class WordSpec {
    final Map<Alphabet, Integer> numberOfSymbols;

    public WordSpec(Map<Alphabet, Integer> numberOfSymbols) {
        this.numberOfSymbols = numberOfSymbols;
    }
}
