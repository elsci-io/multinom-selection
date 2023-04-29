package io.elsci.multinomialselection;

import java.util.Iterator;

/**
 * Returns an iterator to get all possible combinations of words. The words come sorted by probability in descending
 * order.
 */
public class WordIteratorFactory {
    public static Iterator<Word> create(WordSpec wordSpec) {
        return new WordIterator(wordSpec);
    }
}
