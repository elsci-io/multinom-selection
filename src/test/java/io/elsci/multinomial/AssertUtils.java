package io.elsci.multinomial;

import static org.junit.Assert.assertEquals;

public final class AssertUtils {
    public static void assertWordsEqual(Word w1, Word w2) {
        assertEquals(w1.probability, w2.probability, 1e-6);
        assertEquals(w1.symbols, w2.symbols);
    }
}
