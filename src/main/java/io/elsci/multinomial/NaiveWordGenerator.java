package io.elsci.multinomial;

import com.google.common.collect.Lists;

import java.util.*;
import java.util.Map.Entry;

public class NaiveWordGenerator implements WordGenerator {

    public Iterator<Word> generate(WordSpec spec) {
        List<List<Integer>> symbolChoices = new ArrayList<>();
        List<Alphabet> as = new ArrayList<>();
        for (Entry<Alphabet, Integer> specEntry : spec.numberOfSymbols.entrySet()) {
            Alphabet a = specEntry.getKey();
            int syllableLength = specEntry.getValue();
            for (int i = 0; i < syllableLength; i++) {
                as.add(a);
                symbolChoices.add(seq(a.probabilities.length));
            }

        }

        Map<SymbolSet, Double> combinationProb = new HashMap<>();
        List<List<Integer>> words = Lists.cartesianProduct(symbolChoices);
        for (List<Integer> wordSymbolIndices : words) {
            SymbolSet symbols = new SymbolSet();
            double wordProb = 1;
            for (int i = 0; i < wordSymbolIndices.size(); i++) {
                Alphabet a = as.get(i);
                int symbolFromAlphabet = wordSymbolIndices.get(i);
                symbols.add(a.getSymbol(symbolFromAlphabet));
                wordProb *= a.probabilities[symbolFromAlphabet];
            }
            Double existingProb = combinationProb.getOrDefault(symbols, 0D);
            combinationProb.put(symbols, existingProb + wordProb);
        }

        List<Word> result = new ArrayList<>(combinationProb.size());
        for (Entry<SymbolSet, Double> set : combinationProb.entrySet())
            result.add(new Word(set.getKey(), set.getValue()));

        result.sort(Word.BY_PROBABILITY_DESC);
        return result.iterator();
    }

    private static List<Integer> seq(int n) {
        List<Integer> result = new ArrayList<>(n);
        for (int i = 0; i < n; i++)
            result.add(i);
        return result;
    }
}
