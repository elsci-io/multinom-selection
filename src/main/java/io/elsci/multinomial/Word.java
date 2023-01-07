package io.elsci.multinomial;

import java.util.Comparator;
import java.util.Objects;

public class Word {
    static Comparator<Word> BY_PROBABILITY_DESC = Comparator.<Word>comparingDouble((w) -> w.probability).reversed();
    public final SymbolSet symbols;
    public final double probability;
    // 2a, 1b
    public Word(Symbol[] symbols, double probability) {
        this(new MapBasedSymbolSet(symbols), probability);
    }
    public Word(SymbolSet symbols, double probability) {
        if(Double.isInfinite(probability) || Double.isNaN(probability) || probability < 0 || probability > 1)
            throw new InvalidProbabilityException("Invalid probability: " + probability);
        this.symbols = symbols;
        this.probability = probability;
    }

    /**
     * Concatenates {@link SymbolSet}s (see {@link SymbolSet#concat(SymbolSet...)} for details) and calculates
     * overall word probability.
     *
     * @param words to concatenate into a single word
     * @return a word that combines all the specified words and the probability of the overall combination
     */
    public static Word concat(Word ... words) {
        SymbolSet[] sets = new SymbolSet[words.length];
        double overallProbability = 1;
        for (int i = 0; i < words.length; i++) {
            Word word = words[i];
            sets[i] = word.symbols;
            overallProbability *= word.probability;
        }
        return new Word(SymbolSet.concat(sets), overallProbability);
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
