package io.elsci.multinomial;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.apache.commons.math3.special.Gamma.gamma;

public class QuickSyllableGenerator implements SyllableGenerator {

    public Iterator<Word> generate(Alphabet a, int numberOfSymbols) {
        int[] numbers = new int[a.probabilities.length];
        for (int i=0; i < a.probabilities.length; i++)
            // TODO: will this always round correctly?
            numbers[i] = (int) Math.round(a.probabilities[i] * numberOfSymbols);

        double syllableProb = multinomMassFunction(a, numbers);

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
     * Uses <a href="https://en.wikipedia.org/wiki/Multinomial_distribution">Gamma functions</a>
     */
    private static double multinomMassFunction(Alphabet a, int[] numbers) {
        double numerator = gamma(sum(numbers) + 1);
        double denominator = 1;
        for (int number : numbers)
            denominator *= gamma(number + 1);
        double prob = 1;
        for (int i = 0; i < a.probabilities.length; i++)
            prob *= Math.pow(a.probabilities[i], numbers[i]);
        return prob * numerator / denominator;
    }

    private static double sum(int[] a) {
        int result = 0;
        for (int e : a)
            result += e;
        return result;
    }
}
