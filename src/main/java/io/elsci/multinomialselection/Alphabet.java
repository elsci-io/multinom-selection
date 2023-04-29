package io.elsci.multinomialselection;

import java.util.Arrays;

/**
 * Represents probabilities of its letters (symbols) with which they occur (e.g. in chemistry terms an Alphabet is
 * an element which contains probabilities of isotopes). The actual elements are represented by indices from 0 to N.
 */
public class Alphabet {
    final String name;
    final double[] probabilities;

    /**
     * @param probabilities an Alphabet must contain at least one symbol (one probability); must be between 0 and 1;
     *                      must be sorted in descending order; the sum of all probabilities must be 1
     */
    public Alphabet(String name, double... probabilities) {
        this.name = name;
        if (probabilities.length == 0)
            throw new InvalidProbabilityException(
                    "Alphabet '" + name + "' must contain at least one symbol (one probability)");
        double sum = 0;
        double prev = 1;
        for (double prob : probabilities) {
            if(Double.isNaN(prob) || Double.isInfinite(prob))
                throwInvalidProb(name, probabilities, prob, "contains not a number");
            else if(prob < 0)
                throwInvalidProb(name, probabilities, prob, "contains a negative probability");
            else if(prob > 1)
                throwInvalidProb(name, probabilities, prob, "contains a value greater than 1");
            else if(prev < prob)
                throwInvalidProb(name, probabilities, "has probabilities that are not sorted desc");
            prev = prob;
            sum += prob;
        }
        if (sum > 1.000_001 || sum < .999_999)
            throwInvalidProb(name, probabilities, sum, "has probabilities that don't sum up to 1");
        this.probabilities = probabilities;
    }

    /**
     * @return a particular Symbol (letter) from this Alphabet
     */
    public Symbol getSymbol(int letterIdx) {
        return new Symbol(this, letterIdx);
    }

    /**
     * @return particular Symbols (letters) from this Alphabet
     */
    public Symbol[] getSymbols(int... letterIndices) {
        Symbol[] result = new Symbol[letterIndices.length];
        for (int i = 0; i < letterIndices.length; i++)
            result[i] = getSymbol(letterIndices[i]);
        return result;
    }

    private static void throwInvalidProb(String alphabetName, double[] probabilities,
                                         double invalidProb, String errorDetails) {
        throwInvalidProb(alphabetName, probabilities, errorDetails + " (" + invalidProb + ")");
    }
    private static void throwInvalidProb(String alphabetName, double[] probabilities, String errorDetails) {
        throw new InvalidProbabilityException(
                "Alphabet '" + alphabetName + "' "+errorDetails+ ": " + Arrays.toString(probabilities)
        );
    }
}
