package io.elsci.multinomial;

import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.elsci.multinomial.AssertUtils.assertWordsEqual;
import static org.junit.Assert.*;

public class QuickSyllableGeneratorTest {
    @Test
    public void thereIsOnlyOneEmptyWord_andItsProbabilityIs1() {
        Alphabet a = new Alphabet("A", 1);
        Iterator<Word> it = generate(a, 0);
        Word next = it.next();
        assertEquals(1, next.probability, 0);
        assertEquals(0, next.symbols.getWordLength());
        assertFalse(it.hasNext());
    }
    @Test
    public void wordCreatedOutOf1SymbolOf1Alphabet_hasProbability1() {
        Alphabet a = new Alphabet("A", 1);
        Iterator<Word> it = generate(a, 1);
        Word next = it.next();
        assertWordsEqual(new Word(a.getSymbols(0), 1), next);
        assertFalse(it.hasNext());
    }
    @Test
    public void wordCreatedOutOf2SymbolsOf1Alphabet_hasProbability1() {
        Alphabet a = new Alphabet("A", 1);
        NaiveWordGenerator generator = new NaiveWordGenerator();

        Iterator<Word> it = generator.generate(new WordSpec(map(a, 2)));
        Word next = it.next();
        Word expected = new Word(a.getSymbols(0, 0), 1);
        assertWordsEqual(expected, next);
        assertFalse(it.hasNext());
    }

    @Test
    public void creates2words_outOf2SymbolAlphabet() {
        Alphabet a = new Alphabet("A", .75, .25);
        Iterator<Word> it = generate(a, 1);

        Word next = it.next();
        Word expected = new Word(a.getSymbols(0), .75);
        assertWordsEqual(expected, next);

        next = it.next();
        expected = new Word(new Symbol[]{new Symbol(a, 1)}, .25);
        assertWordsEqual(expected, next);
        assertFalse(it.hasNext());
    }

    @Test
    public void wordsAreSortedByProbability_desc() {
        // {{.6, .4}}
        // 2 letters: (.6 * .6), (2*.4*.6) - most probable, (.4 * .4) - least probable
        Alphabet a = new Alphabet("a", .6, .4);
        Iterator<Word> words = generate(a, 2);

        assertWordsEqual(new Word(a.getSymbols(0, 1), .48), words.next());
        assertWordsEqual(new Word(a.getSymbols(0, 0), .36), words.next());
        assertWordsEqual(new Word(a.getSymbols(1, 1), .16), words.next());
        assertFalse(words.hasNext());
    }
    @Test
    public void findTheMostPopular10letterWord() {
        Alphabet a = new Alphabet("a", .2, .3, .5);
        Iterator<Word> it = generate(a, 6);

        Word expected = new Word(new SymbolSet(map(a.getSymbol(0), 1, a.getSymbol(1), 2, a.getSymbol(2), 3)), 0.135);
        assertWordsEqual(expected, it.next());
    }

    @Test
    public void childrenOf_returnsElementsWithManhattanDistanceOf2() {
        int[] numbers = {2, 3, 1};
        int[][] actual = QuickSyllableGenerator.childrenOf(numbers);
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
        int[][] actual = QuickSyllableGenerator.childrenOf(numbers);
        assertEquals(4, actual.length);
        assertArrayEquals(new int[] {3, 2, 0}, actual[0]);
        assertArrayEquals(new int[] {1, 4, 0}, actual[1]);
        assertArrayEquals(new int[] {1, 3, 1}, actual[2]);
        assertArrayEquals(new int[] {2, 2, 1}, actual[3]);

    }

    @Test
    public void childrenOf_neverReturnsNegativesValues_whenZeroIsNotAtTheEnd() {
        int[] numbers = {0, 0, 4};
        int[][] actual = QuickSyllableGenerator.childrenOf(numbers);
        assertEquals(2, actual.length);
        assertArrayEquals(new int[] {1, 0, 3}, actual[0]);
        assertArrayEquals(new int[] {0, 1, 3}, actual[1]);
    }

    @Test
    public void childrenOfArrayWithOneElement_returnsEmptyArray() {
        int[] numbers = {1};
        int[][] actual = QuickSyllableGenerator.childrenOf(numbers);
        assertEquals(0, actual.length);

    }

    private static Iterator<Word> generate(Alphabet a, int syllableLength) {
        return new QuickSyllableGenerator().generate(a, syllableLength);
    }

    private static <K, V> Map<K, V> map(K k, V v) {
        LinkedHashMap<K, V> map = new LinkedHashMap<>();
        map.put(k, v);
        return map;
    }
    private static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2, K k3, V v3) {
        LinkedHashMap<K, V> map = new LinkedHashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }
    private static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2) {
        LinkedHashMap<K, V> map = new LinkedHashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }
}