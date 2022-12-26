package io.elsci.multinomial;

import org.junit.Test;

import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.*;

public class NaiveWordGeneratorTest {
    @Test
    public void thereIsOnlyOneEmptyWord_andItsProbabilityIs1() {
        Alphabet a = new Alphabet(1);
        Alphabets as = new Alphabets(new Alphabet[]{a});

        NaiveWordGenerator generator = new NaiveWordGenerator(as);
        Iterator<Word> it = generator.generate(new WordSpec(Map.of(a, 0)));
        Word next = it.next();
        assertEquals(1, next.probability, 0);
        assertEquals(0, next.symbols.length);
        assertFalse(it.hasNext());
    }
    @Test
    public void wordCreatedOutOf1SymbolOf1Alphabet_hasProbability1() {
        Alphabet a = new Alphabet(1);
        Alphabets as = new Alphabets(new Alphabet[]{a});

        NaiveWordGenerator generator = new NaiveWordGenerator(as);
        Iterator<Word> it = generator.generate(new WordSpec(Map.of(a, 1)));
        Word next = it.next();
        assertEquals(1, next.probability, 0);
        assertEquals(new Symbol(a, 0), next.symbols[0]);
        assertFalse(it.hasNext());
    }
    @Test
    public void wordCreatedOutOf2SymbolsOf1Alphabet_hasProbability1() {
        Alphabet a = new Alphabet(1);
        Alphabets as = new Alphabets(new Alphabet[]{a});

        NaiveWordGenerator generator = new NaiveWordGenerator(as);
        Iterator<Word> it = generator.generate(new WordSpec(Map.of(a, 2)));
        Word next = it.next();
        Word expected = new Word(new Symbol[]{new Symbol(a, 0), new Symbol(a, 0)}, 1);
        assertEquals(expected, next);
        assertFalse(it.hasNext());
    }

    @Test
    public void wordCreatedOutOf1SymbolsOf2Alphabet_has2Words() {
        Alphabet a = new Alphabet(.75, .25);
        Alphabets as = new Alphabets(new Alphabet[]{a});

        NaiveWordGenerator generator = new NaiveWordGenerator(as);
        Iterator<Word> it = generator.generate(new WordSpec(Map.of(a, 1)));

        Word next = it.next();
        Word expected = new Word(new Symbol[]{new Symbol(a, 0)}, .75);
        assertEquals(expected, next);

        next = it.next();
        expected = new Word(new Symbol[]{new Symbol(a, 1)}, .25);
        assertEquals(expected, next);
        assertFalse(it.hasNext());
    }
}