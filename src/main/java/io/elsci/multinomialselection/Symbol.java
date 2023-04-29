package io.elsci.multinomialselection;

import java.util.Objects;

/**
 * A letter in an Alphabet and therefore its probability with which it occurs (e.g. in chemistry terms a
 * Symbol is a particular isotope of an element).
 */
public class Symbol {
    public final Alphabet alphabet;
    public final int letterIndex;

    public Symbol(Alphabet alphabet, int letterIndex) {
        if(alphabet == null)
            throw new IllegalArgumentException("Alphabet cannot be null");
        if(letterIndex < 0)
            throw new IllegalArgumentException("Letter index has to be non-negative");
        this.alphabet = alphabet;
        this.letterIndex = letterIndex;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Symbol symbol = (Symbol) o;
        return letterIndex == symbol.letterIndex && alphabet.equals(symbol.alphabet);
    }
    @Override public int hashCode() {
        return Objects.hash(alphabet, letterIndex);
    }
    @Override public String toString() {
        return alphabet.name + "" + letterIndex;
    }
}
