package io.elsci.multinomial;

import static io.qala.datagen.RandomShortApi.Double;
import static io.qala.datagen.RandomShortApi.alphanumeric;

public class RandomFactory {
    public static SymbolSet symbolSet(Alphabet a, int ... frequencies) {
        return new MapBasedSymbolSet(a, frequencies);
    }
    public static SymbolSet symbolSet(int ... frequencies) {
        return symbolSet(alphabet(alphanumeric(1), frequencies.length), frequencies);
    }

    public static Alphabet alphabet(String name, int size) {
        double[] prob = new double[size];
        double probLeft = 1;
        for (int i = 0; i < prob.length; i++) {
            double newProb = Double(0D, probLeft);
            prob[i] = newProb;
            probLeft -= newProb;
        }
        return new Alphabet(name, prob);
    }
}
