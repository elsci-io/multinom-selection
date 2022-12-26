package io.elsci.multinomial;

import java.util.Arrays;
import java.util.Objects;

public class Word {
    public final Symbol[] symbols;
    public final double probability;

    public Word(Symbol[] symbols, double probability) {
        this.symbols = symbols;
        this.probability = probability;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return Double.compare(word.probability, probability) == 0 && Arrays.equals(symbols, word.symbols);
    }
    @Override public int hashCode() {
        int result = Objects.hash(probability);
        result = 31 * result + Arrays.hashCode(symbols);
        return result;
    }
    @Override public String toString() {
        return "{" + probability +": " +Arrays.toString(symbols)+"}";
    }
}
