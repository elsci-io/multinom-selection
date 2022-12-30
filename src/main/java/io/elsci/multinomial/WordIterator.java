package io.elsci.multinomial;

import java.util.*;

public class WordIterator implements Iterator<Word> {
    private final Queue<WordAndSyllables> queue = new PriorityQueue<>();
    private final SyllableIterator[] iterators;

    public WordIterator(WordSpec spec) {
        this.iterators = new SyllableIterator[spec.numberOfSymbols.size()];

        Word[] syllables = new Word[spec.numberOfSymbols.size()];
        int w = 0;
        for (Map.Entry<Alphabet, Integer> entry : spec.numberOfSymbols.entrySet()) {
            SyllableIterator iterator = new SyllableIterator(entry.getKey(), entry.getValue());
            syllables[w] = iterator.next();
            iterators[w++] = iterator;
        }
        Word mostProbableWord = Word.concat(syllables);
        queue.add(new WordAndSyllables(mostProbableWord, syllables, new NullIterator<>()));
    }

    @Override public Word next() {
        WordAndSyllables nextProbable = queue.poll();
        if(nextProbable == null)
            throw new NoSuchElementException("Reached the end of queue, don't forget to use hasNext() method!");
        Iterator<Word> iterator = nextProbable.iterator;
        if(iterator.hasNext())
            iterator.next();
        queue.addAll(childrenOf(nextProbable.syllables, iterators));
        return nextProbable.word;
    }
    @Override public boolean hasNext() {
        return !queue.isEmpty();
    }

    static List<WordAndSyllables> childrenOf(Word[] syllables, SyllableIterator[] iterators) {
        List<WordAndSyllables> children = new ArrayList<>();
        for (int i = 0; i < syllables.length; i++) {
            if(!iterators[i].hasNext())
                continue;
            Word[] child = syllables.clone();
            child[i] = iterators[i].peekAtNext();
            children.add(new WordAndSyllables(Word.concat(child), child, iterators[i]));
        }
        return children;
    }
    private static class WordAndSyllables implements Comparable<WordAndSyllables> {
        final Word word;
        final Word[] syllables;
        private final Iterator<Word> iterator;

        WordAndSyllables(Word word, Word[] syllables, Iterator<Word> iterator) {
            this.word = word;
            this.syllables = syllables;
            this.iterator = iterator;
        }

        @Override public int compareTo(WordAndSyllables o) {
            return Word.BY_PROBABILITY_DESC.compare(word, o.word);
        }
    }
}
