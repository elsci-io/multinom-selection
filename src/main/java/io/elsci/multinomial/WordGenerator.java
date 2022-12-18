package io.elsci.multinomial;

import java.util.Iterator;

public interface WordGenerator {
    Iterator<Word> generate(WordSpec spec);
}
