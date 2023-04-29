package io.elsci.multinomialselection;

class MathUtils {
    private static final double SQRT_2PI = Math.sqrt(2 * Math.PI);

    /**
     * Uses <a href="https://en.wikipedia.org/wiki/Multinomial_distribution">Gamma functions</a>. It's modified
     * to use some calculations in log scale.
     */
    public static double combinationProbability(int[] combinations, double[] probabilities) {
        double prob = 1;
        for (int i = 0; i < probabilities.length; i++)
            prob *= Math.pow(probabilities[i], combinations[i]);
        // Now approximate the number of combinations

        // We use log-scale because otherwise the factorials/gamma get too large very quickly and overflow
        // even double - we end up with Infinities.
        double numerator = logGamma(sum(combinations) + 1);
        double denominator = 0;
        for (int number : combinations)
            denominator += logGamma(number + 1);
        return prob * Math.exp(numerator - denominator);
    }

    public static int sum(int[] a) {
        int result = 0;
        for (int e : a)
            result += e;
        return result;
    }
    /**
     * <a href="https://introcs.cs.princeton.edu/java/91float/Gamma.java.html">Sedgewick's implementation</a> that
     * uses <a href="https://en.wikipedia.org/wiki/Lanczos_approximation">Lanczo's approximation</a>.
     * <p>
     * This is the source of largest errors during computation. If needed we can make it more exact but adding more
     * terms, but it's also going to work slower. And not sure if we need the result so precise anyway.
     * </p>
     */
    private static double logGamma(double x) {
        double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
        double ser = 1.0 + 76.18009173    / (x + 0)   - 86.50532033    / (x + 1)
                     + 24.01409822    / (x + 2)   -  1.231739516   / (x + 3)
                     +  0.00120858003 / (x + 4)   -  0.00000536382 / (x + 5);
        return tmp + Math.log(ser * SQRT_2PI);
    }
}
