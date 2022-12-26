package io.elsci.multinomial;

import java.util.Objects;

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
}
