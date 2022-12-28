package io.elsci.multinomial;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.apache.commons.math3.special.Gamma.gamma;

public class QuickSyllableGenerator implements SyllableGenerator {

    public Iterator<Word> generate(Alphabet a, int numberOfSymbols) {
        int[] numbers = new int[a.probabilities.length];
        for (int i = 0; i < a.probabilities.length; i++)
            // TODO: will this always round correctly?
            numbers[i] = (int) Math.round(a.probabilities[i] * numberOfSymbols);

        double syllableProb = multinomMassFunction(numbers, a.probabilities);

        // 1. Write an algorithm to enumerate all children of a given element: `numbers[][] childrenOf(numbers[])`
        // 2. Use PriorityQueue<Word, Comparator> and custom Iterator<Word>
        List<Word> words = new ArrayList<>();
        words.add(new Word(new SymbolSet(a, numbers), syllableProb));

        // 100: (.8, .19, .01)
        //       80 ,  19 , 1
        // 10:   8     1.9  0.1
        // r:    8     2  , 0
        // 5*.8, 5*.19, 5*.01
        // 4      0.95  0.05
        // 4      1     0

        // 4x1x0
        // 4x0x1 or 3x2x0 ?
        // 4x0x1 -> (5x0x0, 4x1x0(!), 3x1x1, 3x0x2)
        return words.iterator();
    }

    /**
     * It returns elements with Manhattan Distance = 2. We need this to find a symbolSet with the next highest
     * probability - and it's going to differ from the "parent" symbolSet by 1 symbol transfer. Since that symbol
     * will be transferred to the other one, we'll have differences in 2 positions - which effectively results in
     * Manhattan Distance = 2.
     * <p>
     * It is supposed to be used in {@link #generate(Alphabet, int)} to enumerate all children of a given symbolSet.
     *
     * @param symbolSet is an array where the index of each element represents an index of the symbol of an alphabet
     *                  and its value is the number of this symbol occurrences. It's very similar to {@link SymbolSet},
     *                  just a different structure.
     * @return all children, which means all possible variants of changes to symbolSet with the step of Manhattan
     * distance = 2
     */
    static int[][] childrenOf(int[] symbolSet) {
        int numbersLength = symbolSet.length;
        if (numbersLength == 1)
            return new int[0][];
        int f = 0;
        for (int num : symbolSet) {
            if (num != 0)
                f++;
        }
        int[][] result = new int[f * (numbersLength - 1)][numbersLength];
        for (int i = 0; i < result.length; i++)
            result[i] = symbolSet.clone();
        int i = 0;
        outer:
        while (true) {
            for (int j = 0; j < numbersLength; j++) {
                for (int e = 0; e < numbersLength; e++) {
                    if (e != j && result[i][e] != 0) {
                        result[i][j]++;
                        result[i][e]--;
                        if (++i >= result.length)
                            break outer;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Uses <a href="https://en.wikipedia.org/wiki/Multinomial_distribution">Gamma functions</a>
     */
    private static double multinomMassFunction(int[] numbers, double[] probabilities) {
        double numerator = gamma(sum(numbers) + 1);
        double denominator = 1;
        for (int number : numbers)
            denominator *= gamma(number + 1);
        double prob = 1;
        for (int i = 0; i < probabilities.length; i++)
            prob *= Math.pow(probabilities[i], numbers[i]);
        return prob * numerator / denominator;
    }

    private static double sum(int[] a) {
        int result = 0;
        for (int e : a)
            result += e;
        return result;
    }
}
