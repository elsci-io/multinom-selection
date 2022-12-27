package io.elsci.multinomial;

import java.util.Iterator;

public interface SyllableGenerator {
    Iterator<Word> generate(Alphabet a, int numberOfSymbols);
}
