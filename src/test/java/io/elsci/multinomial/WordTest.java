package io.elsci.multinomial;

import org.junit.Test;

import static io.elsci.multinomial.RandomFactory.alphabet;
import static io.elsci.multinomial.RandomFactory.symbolSet;
import static org.junit.Assert.*;

public class WordTest {
    @Test public void concat_returnsSameWordIfThereIsOnlyOneWordToConcat() {
        Word word = new Word(symbolSet(1, 1), .1);
        assertEquals(word, Word.concat(word));
    }
    @Test public void concat_combinesSymbolSets_asWellAsProbabilities() {
        Alphabet a1 = alphabet("a", 3);
        Alphabet a2 = alphabet("b", 2);
        SymbolSet s1 = symbolSet(a1, 0, 1, 1);
        SymbolSet s2 = symbolSet(a2, 1, 0);

        Word concatenated = Word.concat(new Word(s1, .9), new Word(s2, .3));
        assertEquals(.9*.3, concatenated.probability, 0);
        assertEquals(SymbolSet.concat(s1, s2), concatenated.symbols);
    }
}