package io.elsci.multinomialselection;

import org.junit.Test;

import java.util.Map;

import static io.elsci.multinomialselection.RandomFactory.alphabet;
import static org.junit.Assert.assertEquals;

public class SymbolSetTest {

    @Test
    public void concatenatingEmptySet_producesEmptySymbolSet() {
        assertEquals(new MapBasedSymbolSet(new Symbol[0]), SymbolSet.concat());
    }
    @Test
    public void concatenatingSingleSet_producesSameSymbolSet() {
        Alphabet a = alphabet("a", 2);
        SymbolSet mapBased = new MapBasedSymbolSet(Map.of(a.getSymbol(0), 1));
        assertEquals(mapBased, SymbolSet.concat(mapBased));

        SymbolSet arrayBased = new ArrayBasedSymbolSet(a, new int[]{1, 0});
        assertEquals(mapBased, SymbolSet.concat(arrayBased));
    }
    @Test
    public void concatenatingEmptySymbolSetDoesNothingToResult() {
        Alphabet a = alphabet("a", 2);
        SymbolSet set = new MapBasedSymbolSet(Map.of(a.getSymbol(0), 1));
        assertEquals(set, SymbolSet.concat(new MapBasedSymbolSet(), set, new ArrayBasedSymbolSet(a, new int[]{0, 0})));
    }
    @Test
    public void canConcatSymbolSetsOfDifferentClasses() {
        Alphabet a = alphabet("a", 3);
        Alphabet b = alphabet("b", 3);
        Alphabet c = alphabet("c", 4);
        Map<Symbol, Integer> frequency = Map.of(
                a.getSymbol(1), 2,
                b.getSymbol(2), 2,
                a.getSymbol(0), 1);
        SymbolSet[] actual = {new MapBasedSymbolSet(frequency), new ArrayBasedSymbolSet(c, new int[]{3, 0, 1, 2})};

        assertEquals(new MapBasedSymbolSet(Map.of(
                a.getSymbol(0), 1,
                a.getSymbol(1), 2,
                b.getSymbol(2), 2,
                c.getSymbol(0), 3,
                c.getSymbol(2), 1,
                c.getSymbol(3), 2)), SymbolSet.concat(actual));
    }
}