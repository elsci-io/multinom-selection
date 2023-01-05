package io.elsci.multinomial;

import java.util.Iterator;

public class WordIteratorFactory {
    public static Iterator<Word> create(WordSpec wordSpec) {
        return new WordIterator(wordSpec);
    }
}
