package io.elsci.multinomial;

public class Alphabet {
    final double[] probabilities;

    public Alphabet(double ... probabilities) {
        if(probabilities.length == 0)
            throw new IllegalArgumentException("Forgot to fill the alphabet?");
        this.probabilities = probabilities;
    }
}
