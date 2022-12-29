package io.elsci.multinomial;

import org.junit.Test;

import static org.junit.Assert.*;

public class SyllableIteratorTest {
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