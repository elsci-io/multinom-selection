package io.elsci.multinomial;

import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.elsci.multinomial.AssertUtils.assertWordsEqual;
import static org.junit.Assert.*;

public class NaiveWordIteratorFactoryTest {
    @Test
    public void thereIsOnlyOneEmptyWord_andItsProbabilityIs1() {
        Alphabet a = new Alphabet("A", 1);
        Iterator<Word> it = generate(map(a, 0));
        Word next = it.next();
        assertEquals(1, next.probability, 0);
        assertEquals(0, next.symbols.getWordLength());
        assertFalse(it.hasNext());
    }
    @Test
    public void wordCreatedOutOf1SymbolOf1Alphabet_hasProbability1() {
        Alphabet a = new Alphabet("A", 1);
        Iterator<Word> it = generate(map(a, 1));
        Word next = it.next();
        assertWordsEqual(new Word(a.getSymbols(0), 1), next);
        assertFalse(it.hasNext());
    }
    @Test
    public void wordCreatedOutOf2SymbolsOf1Alphabet_hasProbability1() {
        Alphabet a = new Alphabet("A", 1);

        Iterator<Word> it = NaiveWordIteratorFactory.createNaiveIterator(new WordSpec(map(a, 2)));
        Word next = it.next();
        Word expected = new Word(a.getSymbols(0, 0), 1);
        assertWordsEqual(expected, next);
        assertFalse(it.hasNext());
    }

    @Test
    public void wordCreatedOutOf1SymbolsOf2Alphabet_has2Words() {
        Alphabet a = new Alphabet("A", .75, .25);
        Iterator<Word> it = generate(map(a, 1));

        Word next = it.next();
        Word expected = new Word(a.getSymbols(0), .75);
        assertWordsEqual(expected, next);

        next = it.next();
        expected = new Word(new Symbol[]{new Symbol(a, 1)}, .25);
        assertWordsEqual(expected, next);
        assertFalse(it.hasNext());
    }

    @Test
    public void alphabets2x1_generate2words_length2() {
        Alphabet a0 = new Alphabet("a", .75, .25);
        Alphabet a1 = new Alphabet("A", 1);

        Iterator<Word> it = generate(map(a0, 1, a1, 1));

        Word next = it.next();
        Word expected = new Word(new Symbol[]{a0.getSymbol(0), a1.getSymbol(0)}, .75);
        assertWordsEqual(expected, next);

        next = it.next();
        expected = new Word(new Symbol[]{a0.getSymbol(1), a1.getSymbol(0)}, .25);
        assertWordsEqual(expected, next);
        assertFalse(it.hasNext());
    }

    @Test
    public void alphabets2x2_generate4words_length2() {
        Alphabet a0 = new Alphabet("a", .75, .25);
        Alphabet a1 = new Alphabet("A", .2, .8);

        Iterator<Word> it = generate(map(a0, 1, a1, 1));

        assertWordsEqual(new Word(new Symbol[]{a0.getSymbol(0), a1.getSymbol(1)}, .6), it.next());
        assertWordsEqual(new Word(new Symbol[]{a0.getSymbol(1), a1.getSymbol(1)}, .2), it.next());
        assertWordsEqual(new Word(new Symbol[]{a0.getSymbol(0), a1.getSymbol(0)}, .15), it.next());
        assertWordsEqual(new Word(new Symbol[]{a0.getSymbol(1), a1.getSymbol(0)}, .05), it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void wordsAreSortedByProbability_desc() {
        // {{.6, .4}}
        // 2 letters: (.6 * .6), (2*.4*.6) - most probable, (.4 * .4) - least probable
        Alphabet a = new Alphabet("a", .6, .4);
        Iterator<Word> words = generate(map(a, 2));

        assertWordsEqual(new Word(a.getSymbols(0, 1), .48), words.next());
        assertWordsEqual(new Word(a.getSymbols(0, 0), .36), words.next());
        assertWordsEqual(new Word(a.getSymbols(1, 1), .16), words.next());
        assertFalse(words.hasNext());
    }

    @Test
    public void findTheMostPopularWordIn1Alphabet() {
        Alphabet a = new Alphabet("a", .2, .8);
        Iterator<Word> it = generate(map(a, 10));

        assertWordsEqual(new Word(new MapBasedSymbolSet(map(a.getSymbol(0), 2, a.getSymbol(1), 8)),  0.3019899), it.next());

    }


    private static Iterator<Word> generate(Map<Alphabet, Integer> wordSpec) {
        return NaiveWordIteratorFactory.createNaiveIterator(new WordSpec(wordSpec));
    }

    private static <K, V> Map<K, V> map(K k, V v) {
        LinkedHashMap<K, V> map = new LinkedHashMap<>();
        map.put(k, v);
        return map;
    }
    private static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2) {
        LinkedHashMap<K, V> map = new LinkedHashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }
}