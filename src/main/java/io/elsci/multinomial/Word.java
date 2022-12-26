package io.elsci.multinomial;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class Word {
    static Comparator<Word> BY_PROBABILITY_DESC = Comparator.<Word>comparingDouble((w) -> w.probability).reversed();
    public final SymbolSet symbols;
    public final double probability;
    // 2a, 1b
    public Word(Symbol[] symbols, double probability) {
        this(new SymbolSet(symbols), probability);
    }
    public Word(SymbolSet symbols, double probability) {
        this.symbols = symbols;
        this.probability = probability;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return Double.compare(word.probability, probability) == 0 && symbols.equals(word.symbols);
    }

    @Override public int hashCode() {
        int result = Objects.hash(probability);
        result = 31 * result + symbols.hashCode();
        return result;
    }
    @Override public String toString() {
        return "{prob=" + probability +": " +symbols+"}";
    }
}
