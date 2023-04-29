package io.elsci.multinomialselection;

import org.junit.Test;

import static org.junit.Assert.*;

public class AlphabetTest {
    @Test
    public void requiresAtLeastOneSymbol() {
        Exception e = assertThrows(InvalidProbabilityException.class, () -> new Alphabet("a", new double[0]));
        assertEquals("Alphabet 'a' must contain at least one symbol (one probability)", e.getMessage());
    }
    @Test
    public void allProbabilitiesAreNumbers() {
        Exception e = assertThrows(InvalidProbabilityException.class, () -> new Alphabet("a", new double[]{1, Double.NaN}));
        assertEquals("Alphabet 'a' contains not a number (NaN): [1.0, NaN]", e.getMessage());

        e = assertThrows(InvalidProbabilityException.class, () -> new Alphabet("a", new double[]{Double.NEGATIVE_INFINITY, 1}));
        assertEquals("Alphabet 'a' contains not a number (-Infinity): [-Infinity, 1.0]", e.getMessage());

        e = assertThrows(InvalidProbabilityException.class, () -> new Alphabet("a", new double[]{Double.POSITIVE_INFINITY}));
        assertEquals("Alphabet 'a' contains not a number (Infinity): [Infinity]", e.getMessage());
    }
    @Test
    public void allProbabilitiesMustBeBetween0and1() {
        Exception e = assertThrows(InvalidProbabilityException.class, () -> new Alphabet("a", new double[]{1, -0.1}));
        assertEquals("Alphabet 'a' contains a negative probability (-0.1): [1.0, -0.1]", e.getMessage());

        e = assertThrows(InvalidProbabilityException.class, () -> new Alphabet("a", new double[]{1.01, 0.5}));
        assertEquals("Alphabet 'a' contains a value greater than 1 (1.01): [1.01, 0.5]", e.getMessage());
    }
    @Test
    public void allProbabilitiesMustSumUpTo1_roundedToMillionth() {
        Exception e = assertThrows(InvalidProbabilityException.class, () -> new Alphabet("a", new double[]{.500_002, .5}));
        assertEquals("Alphabet 'a' has probabilities that don't sum up to 1 (1.0000019999999998): [0.500002, 0.5]", e.getMessage());

        e = assertThrows(InvalidProbabilityException.class, () -> new Alphabet("a", new double[]{.5, .499_998}));
        assertEquals("Alphabet 'a' has probabilities that don't sum up to 1 (0.9999979999999999): [0.5, 0.499998]", e.getMessage());
    }
    @Test
    public void allProbabilitiesMustBeSortedDesc() {
        Exception e = assertThrows(InvalidProbabilityException.class, () -> new Alphabet("a", new double[]{.4, .5}));
        assertEquals("Alphabet 'a' has probabilities that are not sorted desc: [0.4, 0.5]", e.getMessage());
    }
}