package io.elsci.multinomial;

public class Word {
    public final Alphabet[] alphabets;
    public final int[] symbolsFromAlphabets;
    public final double probability;

    public Word(Alphabet[] alphabets, int[] symbolsFromAlphabets, double probability) {
        this.alphabets = alphabets;
        this.symbolsFromAlphabets = symbolsFromAlphabets;
        this.probability = probability;
    }
}
