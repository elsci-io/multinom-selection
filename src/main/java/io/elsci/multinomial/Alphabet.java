package io.elsci.multinomial;

public class Alphabet {
    final String name;
    final double[] probabilities;

    public Alphabet(String name, double ... probabilities) {
        this.name = name;
        if(probabilities.length == 0)
            throw new IllegalArgumentException("Forgot to fill the alphabet?");
        this.probabilities = probabilities;
    }

    public Symbol getSymbol(int letterIdx) {
        return new Symbol(this, letterIdx);
    }

    public Symbol[] getSymbols(int ... letterIndices) {
        Symbol[] result = new Symbol[letterIndices.length];
        for (int i = 0; i < letterIndices.length; i++)
            result[i] = getSymbol(letterIndices[i]);
        return result;
    }
}
