package io.elsci.multinomial;

import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class NaiveWordGeneratorTest {
    @Test
    public void thereIsOnlyOneEmptyWord_andItsProbabilityIs1() {
        Alphabet a = new Alphabet("A", 1);

        NaiveWordGenerator generator = new NaiveWordGenerator();
        Iterator<Word> it = generator.generate(new WordSpec(map(a, 0)));
        Word next = it.next();
        assertEquals(1, next.probability, 0);
        assertEquals(0, next.symbols.length);
        assertFalse(it.hasNext());
    }
    @Test
    public void wordCreatedOutOf1SymbolOf1Alphabet_hasProbability1() {
        Alphabet a = new Alphabet("A", 1);
        NaiveWordGenerator generator = new NaiveWordGenerator();

        Iterator<Word> it = generator.generate(new WordSpec(map(a, 1)));
        Word next = it.next();
        assertEquals(1, next.probability, 0);
        assertArrayEquals(a.getSymbols(0), next.symbols);
        assertFalse(it.hasNext());
    }
    @Test
    public void wordCreatedOutOf2SymbolsOf1Alphabet_hasProbability1() {
        Alphabet a = new Alphabet("A", 1);
        NaiveWordGenerator generator = new NaiveWordGenerator();

        Iterator<Word> it = generator.generate(new WordSpec(map(a, 2)));
        Word next = it.next();
        Word expected = new Word(a.getSymbols(0, 0), 1);
        assertEquals(expected, next);
        assertFalse(it.hasNext());
    }

    @Test
    public void wordCreatedOutOf1SymbolsOf2Alphabet_has2Words() {
        Alphabet a = new Alphabet("A", .75, .25);
        NaiveWordGenerator generator = new NaiveWordGenerator();

        Iterator<Word> it = generator.generate(new WordSpec(map(a, 1)));

        Word next = it.next();
        Word expected = new Word(a.getSymbols(0), .75);
        assertEquals(expected, next);

        next = it.next();
        expected = new Word(new Symbol[]{new Symbol(a, 1)}, .25);
        assertEquals(expected, next);
        assertFalse(it.hasNext());
    }

    @Test
    public void alphabets2x1_generate2words_length2() {
        Alphabet a0 = new Alphabet("a", .75, .25);
        Alphabet a1 = new Alphabet("A", 1);

        NaiveWordGenerator generator = new NaiveWordGenerator();
        Iterator<Word> it = generator.generate(new WordSpec(map(a0, 1, a1, 1)));

        Word next = it.next();
        Word expected = new Word(new Symbol[]{a0.getSymbol(0), a1.getSymbol(0)}, .75);
        assertEquals(expected, next);

        next = it.next();
        expected = new Word(new Symbol[]{a0.getSymbol(1), a1.getSymbol(0)}, .25);
        assertEquals(expected, next);
        assertFalse(it.hasNext());
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