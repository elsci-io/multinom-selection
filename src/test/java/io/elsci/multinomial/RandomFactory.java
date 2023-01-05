package io.elsci.multinomial;

import java.util.Arrays;

import static io.qala.datagen.RandomShortApi.Double;
import static io.qala.datagen.RandomShortApi.*;

public class RandomFactory {
    public static SymbolSet symbolSet(Alphabet a, int ... frequencies) {
        return new MapBasedSymbolSet(a, frequencies);
    }
    public static SymbolSet symbolSet(int ... frequencies) {
        return symbolSet(alphabet(alphanumeric(1), frequencies.length), frequencies);
    }

    public static Alphabet[] alphabets(int n, int maxAlphabetSize) {
        Alphabet[] result = new Alphabet[n];
        for (int i = 0; i < n; i++)
            result[i] = alphabet(alphanumeric(2), integer(1, maxAlphabetSize));
        return result;
    }
    public static Alphabet alphabet(String name, int size) {
        double[] prob = new double[size];
        double probLeft = 1;
        for (int i = 1; i < prob.length; i++) {
            double newProb = Double(0D, probLeft);
            prob[i] = newProb;
            probLeft -= newProb;
        }
        prob[0] = probLeft;
        Arrays.sort(prob);
        reverse(prob);
        return new Alphabet(name, prob);
    }

    private static void reverse(double[] prob) {
        for (int i = 0; i < prob.length / 2; i++) {
            int endIdx = prob.length - i - 1;
            double tmp = prob[endIdx];
            prob[endIdx] = prob[i];
            prob[i] = tmp;
        }
    }
}
