package io.elsci.multinomial;

import static org.junit.Assert.*;

public final class AssertUtils {
    public static void assertWordsEqual(Word w1, Word w2) {
        assertEquals(getMapBasedSymbolSet(w1.symbols), getMapBasedSymbolSet(w2.symbols));
        assertEquals(w1.probability, w2.probability, 1e-6);
    }
    private static SymbolSet getMapBasedSymbolSet(SymbolSet s) {
        if(s instanceof ArrayBasedSymbolSet) {
            ArrayBasedSymbolSet ab = (ArrayBasedSymbolSet) s;
            return new MapBasedSymbolSet(ab.alphabet, ab.symbolFrequencies);
        }
        return s;
    }
}
