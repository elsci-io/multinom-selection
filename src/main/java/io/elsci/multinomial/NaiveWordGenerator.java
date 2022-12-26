package io.elsci.multinomial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

public class NaiveWordGenerator implements WordGenerator {
    private final Alphabets alphabets;

    public NaiveWordGenerator(Alphabets alphabets) {
        this.alphabets = alphabets;
    }

    public Iterator<Word> generate(WordSpec spec) {
        ArrayList<Word> result = new ArrayList<>();
        for (Map.Entry<Alphabet, Integer> specEntry : spec.numberOfSymbols.entrySet()) {
            Alphabet a = specEntry.getKey();
            int numOfLetters = specEntry.getValue();

            Symbol[] wordSymbols = new Symbol[numOfLetters];
            Arrays.fill(wordSymbols, new Symbol(a, 0));
            result.add(new Word(wordSymbols, 1));
        }
        return result.iterator();
    }
}
