package io.elsci.multinomial;

import static org.apache.commons.math3.special.Gamma.gamma;

class MathUtils {

    /**
     * Uses <a href="https://en.wikipedia.org/wiki/Multinomial_distribution">Gamma functions</a>
     */
    public static double multinomMassFunction(int[] numbers, double[] probabilities) {
        double numerator = gamma(sum(numbers) + 1);
        double denominator = 1;
        for (int number : numbers)
            denominator *= gamma(number + 1);
        double prob = 1;
        for (int i = 0; i < probabilities.length; i++)
            prob *= Math.pow(probabilities[i], numbers[i]);
        return prob * numerator / denominator;
    }

    public static int sum(int[] a) {
        int result = 0;
        for (int e : a)
            result += e;
        return result;
    }

}
