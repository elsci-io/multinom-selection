package io.elsci.multinomial;

import com.google.common.collect.Lists;

import java.util.*;

public class NaiveWordGenerator implements WordGenerator {

    public Iterator<Word> generate(WordSpec spec) {
        ArrayList<Word> result = new ArrayList<>();
        List<List<Integer>> blah = new ArrayList<>();
        ArrayList<Alphabet> as = new ArrayList<>();
        for (Map.Entry<Alphabet, Integer> specEntry : spec.numberOfSymbols.entrySet()) {
            Alphabet a = specEntry.getKey();
            int syllableLength = specEntry.getValue();
            for (int i = 0; i < syllableLength; i++) {
                as.add(a);
                blah.add(seq(a.probabilities.length));
            }

        }

        // We need a word consisting of 2 letters: {{a, b}, {C}},
        // so we end up with: aC , bC
        //

        List<List<Integer>> words = Lists.cartesianProduct(blah);
        for (List<Integer> wordSymbolIndices : words) {
            Symbol[] symbols = new Symbol[wordSymbolIndices.size()];
            double wordProb = 1;
            for (int i = 0; i < wordSymbolIndices.size(); i++) {
                Alphabet a = as.get(i);
                int symbolFromAlphabet = wordSymbolIndices.get(i);
                symbols[i] = new Symbol(a, symbolFromAlphabet);
                wordProb *= a.probabilities[symbolFromAlphabet];
            }
            result.add(new Word(symbols, wordProb));
        }

        return result.iterator();
    }

    private static List<Integer> seq(int n) {
        List<Integer> result = new ArrayList<>(n);
        for (int i = 0; i < n; i++)
            result.add(i);
        return result;
    }
}
