package io.elsci.multinomial;

import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

import static io.elsci.multinomial.AssertUtils.assertWordsEqual;
import static io.qala.datagen.RandomShortApi.integer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class WordIteratorTest {
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
        Alphabet a1 = new Alphabet("A", .8, .2);

        Iterator<Word> it = generate(map(a0, 1, a1, 1));

        assertWordsEqual(new Word(new Symbol[]{a0.getSymbol(0), a1.getSymbol(0)}, .6), it.next());
        assertWordsEqual(new Word(new Symbol[]{a0.getSymbol(1), a1.getSymbol(0)}, .2), it.next());
        assertWordsEqual(new Word(new Symbol[]{a0.getSymbol(0), a1.getSymbol(1)}, .15), it.next());
        assertWordsEqual(new Word(new Symbol[]{a0.getSymbol(1), a1.getSymbol(1)}, .05), it.next());
        assertFalse(it.hasNext());
    }
    @Test
    public void alphabets3x2_generate9words_length2() {
        Alphabet a0 = new Alphabet("a", .5, .49, .01);
        Alphabet a1 = new Alphabet("A", .4, .39, .21);

        Iterator<Word> it = generate(map(a0, 1, a1, 1));

        assertWordsEqual(new Word(new Symbol[]{a0.getSymbol(0), a1.getSymbol(0)}, .5 *.4), it.next());
        assertWordsEqual(new Word(new Symbol[]{a0.getSymbol(1), a1.getSymbol(0)}, .49*.4), it.next());
        assertWordsEqual(new Word(new Symbol[]{a0.getSymbol(0), a1.getSymbol(1)}, .5 *.39), it.next());
        assertWordsEqual(new Word(new Symbol[]{a0.getSymbol(1), a1.getSymbol(1)}, .49*.39), it.next());
        assertWordsEqual(new Word(new Symbol[]{a0.getSymbol(0), a1.getSymbol(2)}, .5 *.21), it.next());
        assertWordsEqual(new Word(new Symbol[]{a0.getSymbol(1), a1.getSymbol(2)}, .49*.21), it.next());
        assertWordsEqual(new Word(new Symbol[]{a0.getSymbol(2), a1.getSymbol(0)}, .01*.4), it.next());
        assertWordsEqual(new Word(new Symbol[]{a0.getSymbol(2), a1.getSymbol(1)}, .01*.39), it.next());
        assertWordsEqual(new Word(new Symbol[]{a0.getSymbol(2), a1.getSymbol(2)}, .01*.21), it.next());
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

    @Test @Ignore// this is supposed to be run manually only as it's too slow
    public void generatesSameMostProbableWordAsNaiveWordGenerator() {
        Alphabet[] alphabets = RandomFactory.alphabets(integer(1, 6), integer(1, 5));
        Map<Alphabet, Integer> wordSpec = new HashMap<>();
        for (Alphabet alphabet : alphabets)
            wordSpec.put(alphabet, integer(1, 5));
        Iterator<Word> naiveIterator = NaiveWordIteratorFactory.createNaiveIterator(new WordSpec(wordSpec));
        Iterator<Word> quickIterator = generate(wordSpec);

        assertWordsEqual(naiveIterator.next(), quickIterator.next());
    }

    @Test @Ignore// this is supposed to be run manually only as it's too slow
    public void generatesSameResultsAsNaiveWordGenerator() {
        Alphabet[] alphabets = RandomFactory.alphabets(integer(1, 6), integer(1, 5));
        Map<Alphabet, Integer> wordSpec = new HashMap<>();
        for (Alphabet alphabet : alphabets)
            wordSpec.put(alphabet, integer(1, 5));
        Iterator<Word> naiveIterator = NaiveWordIteratorFactory.createNaiveIterator(new WordSpec(wordSpec));
        Iterator<Word> quickIterator = generate(wordSpec);
        List<Word> naiveWords = new ArrayList<>();
        while(naiveIterator.hasNext()) {
            Word naiveWord = naiveIterator.next();
            naiveWords.add(naiveWord);
            Word quickWord = quickIterator.next();
            assertWordsEqual(naiveWord, quickWord);
        }
        assertFalse(quickIterator.hasNext());
        System.out.println("Number of isotopes: " + naiveWords.size());
    }

    private static Iterator<Word> generate(Map<Alphabet, Integer> wordSpec) {
        return WordIteratorFactory.create(new WordSpec(wordSpec));
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