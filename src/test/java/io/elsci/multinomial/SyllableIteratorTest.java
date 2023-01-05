package io.elsci.multinomial;

import org.junit.Test;

import static io.elsci.multinomial.AssertUtils.assertWordsEqual;
import java.util.Map;

import static org.junit.Assert.*;

public class SyllableIteratorTest {
    /**
     * "natural" here refers to natural abundance of isotopes. So if we have 2 symbols with .6 and .4 probabilities
     * and we request a word of length 10, then we should end up with 6 and 4 symbols.
     */
    @Test
    public void mostProbableSyllableContainsSymbolsInNaturalProportions_whenSyllableLengthGreaterThanAlphabetSize() {
        Alphabet a = new Alphabet("a", .7, .3);
        SyllableIterator it = new SyllableIterator(a, 10);

        Word expected = new Word(new MapBasedSymbolSet(Map.of(a.getSymbol(0), 7, a.getSymbol(1), 3)), .2668279319999999);
        Word actual = Word.concat(it.next()); // need concat to turn ArrayBasedSymbolSet to MapBasedSymbolSet
        assertEquals(expected, actual);
    }

    @Test
    public void mostProbableSyllableContainsMostProbableSymbols_whenSyllableLengthIsLessThanAlphabetSize() {
        Alphabet a = new Alphabet("a", .7, .3);
        SyllableIterator it = new SyllableIterator(a, 1);
        Word expected = new Word(new ArrayBasedSymbolSet(a, new int[]{1, 0}), .7);
        assertEquals(expected, it.next());
    }
    @Test
    public void mostProbableSyllableCanContainSymbols_withProbabilityLessThan05() {
        Alphabet a = new Alphabet("a", .4, .3, .3);
        SyllableIterator it = new SyllableIterator(a, 1);
        Word expected = new Word(new ArrayBasedSymbolSet(a, new int[]{1, 0, 0}), .4);
        assertEquals(expected, it.next());
    }

    @Test
    public void whenThereIsOnlyOneSymbolInAlphabet_itFullyRepresentsMostProbableSyllable() {
        Alphabet a = new Alphabet("a", 1);
        SyllableIterator it = new SyllableIterator(a, 1);
        assertWordsEqual(new Word(a.getSymbols(0), 1), it.createMostProbableSyllable());

        it = new SyllableIterator(a, 2);
        assertWordsEqual(new Word(a.getSymbols(0, 0), 1), it.createMostProbableSyllable());
    }

    @Test
    public void createsMostProbableSyllableWhenAlphabetContainsMarginalProbabilities() {
        Alphabet a = new Alphabet("a", 0.5, 0.3, 0.1, 0.1);
        SyllableIterator it = new SyllableIterator(a, 5);
        assertWordsEqual(new Word(a.getSymbols(0,0,0,1,1), .1125), it.createMostProbableSyllable());
    }

    @Test
    public void createsMostProbableSyllableWithFirstSymbolOfAlphabetHavingProbabilityLessThanHalf_wordLength1() {
        Alphabet a = new Alphabet("a", 0.3, 0.3, 0.25, 0.1, 0.05);
        SyllableIterator it = new SyllableIterator(a, 1);
        assertWordsEqual(new Word(new Symbol[]{a.getSymbol(0)}, .3), it.createMostProbableSyllable());
    }

    @Test
    public void createsMostProbableSyllableWithFirstSymbolOfAlphabetHavingProbabilityLessThanHalf_wordLength5() {
        Alphabet a = new Alphabet("a", 0.3, 0.3, 0.25, 0.1, 0.05);
        SyllableIterator it = new SyllableIterator(a, 5);
        assertWordsEqual(new Word(a.getSymbols(0,0,1,1,2), .06075), it.createMostProbableSyllable());
    }
    @Test
    public void createsMostProbableSyllableWithLastSymbolOfAlphabetHavingProbabilityLessThanHalf() {
        // .85*5 = 4.45 is rounded to 4 symbols
        // .09*5 = 0.45 is rounded to 0 in case of a bug (which we had)
        Alphabet a = new Alphabet("a", .85, .09, .06);
        SyllableIterator it = new SyllableIterator(a, 5);
        assertWordsEqual(new Word(a.getSymbols(0,0,0,0,0), .4437053), it.createMostProbableSyllable());
    }

    @Test
    // This happens because there are more combinations of mixed syllables than there are with just one symbol, so
    // even if the symbol is not very probable, a combination with it may still have higher probability
    public void mostProbableSyllableCanContainEitherMostProbableSymbolOnly_orMixWithLessProbableOnes() {
        // 1/3 for the first symbol is a boundary case, after that the probabilities shift towards having
        // 2 instances of the 1st symbol
        Alphabet a = new Alphabet("a", .65, .35);
        SyllableIterator it = new SyllableIterator(a, 2);
        assertWordsEqual(new Word(a.getSymbols(0, 1), 2*.65*.35), it.createMostProbableSyllable());

        a = new Alphabet("a", .67, .33);
        it = new SyllableIterator(a, 2);
        assertWordsEqual(new Word(a.getSymbols(0, 0), .67*.67), it.createMostProbableSyllable());
    }

    @Test
    public void childrenOf_returnsElementsWithManhattanDistanceOf2() {
        int[] numbers = {2, 3, 1};
        int[][] actual = SyllableIterator.childrenOf(numbers);
        assertEquals(6, actual.length);
        assertArrayEquals(new int[] {3, 2, 1}, actual[0]);
        assertArrayEquals(new int[] {3, 3, 0}, actual[1]);
        assertArrayEquals(new int[] {1, 4, 1}, actual[2]);
        assertArrayEquals(new int[] {2, 4, 0}, actual[3]);
        assertArrayEquals(new int[] {1, 3, 2}, actual[4]);
        assertArrayEquals(new int[] {2, 2, 2}, actual[5]);

    }

    @Test
    public void childrenOf_neverReturnsNegativesValues() {
        int[] numbers = {2, 3, 0};
        int[][] actual = SyllableIterator.childrenOf(numbers);
        assertEquals(4, actual.length);
        assertArrayEquals(new int[] {3, 2, 0}, actual[0]);
        assertArrayEquals(new int[] {1, 4, 0}, actual[1]);
        assertArrayEquals(new int[] {1, 3, 1}, actual[2]);
        assertArrayEquals(new int[] {2, 2, 1}, actual[3]);

    }

    @Test
    public void childrenOf_neverReturnsNegativesValues_whenZeroIsNotAtTheEnd() {
        int[] numbers = {0, 0, 4};
        int[][] actual = SyllableIterator.childrenOf(numbers);
        assertEquals(2, actual.length);
        assertArrayEquals(new int[] {1, 0, 3}, actual[0]);
        assertArrayEquals(new int[] {0, 1, 3}, actual[1]);
    }

    @Test
    public void childrenOfArrayWithOneElement_returnsEmptyArray() {
        int[] numbers = {1};
        int[][] actual = SyllableIterator.childrenOf(numbers);
        assertEquals(0, actual.length);
    }

}