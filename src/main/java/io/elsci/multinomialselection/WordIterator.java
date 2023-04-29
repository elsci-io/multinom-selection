package io.elsci.multinomialselection;

import java.util.*;

/**
 * Iterates over all possible combinations of words. The words come sorted by probability in descending
 * order.
 */
class WordIterator implements Iterator<Word> {
    private final Queue<WordAndSyllables> queue = new PriorityQueue<>();
    private final List<LazyList<Word>> alphabetSyllables;
    private final Set<IntArray> alreadySeen = new HashSet<>();


    public WordIterator(WordSpec spec) {
        this.alphabetSyllables = new ArrayList<>(spec.numberOfSymbols.size());
        for (Map.Entry<Alphabet, Integer> entry : spec.numberOfSymbols.entrySet())
            this.alphabetSyllables.add(new LazyList<>(new SyllableIterator(entry.getKey(), entry.getValue())));
        queue.add(createWord(new int[alphabetSyllables.size()]));
    }

    @Override public Word next() {
        WordAndSyllables nextProbable = queue.poll();
        if(nextProbable == null)
            throw new NoSuchElementException("Reached the end of queue, don't forget to use hasNext() method!");
        List<WordAndSyllables> newChildren = childrenOf(nextProbable);
        for (WordAndSyllables newChild : newChildren) {
            IntArray newIndices = new IntArray(newChild.listIndices);
            if(alreadySeen.contains(newIndices))
                continue;
            alreadySeen.add(newIndices);
            queue.add(newChild);
        }
        return nextProbable.word;
    }
    @Override public boolean hasNext() {
        return !queue.isEmpty();
    }

    List<WordAndSyllables> childrenOf(WordAndSyllables wordAndSyllables) {
        int[] currentWordPosition = wordAndSyllables.listIndices;
        List<WordAndSyllables> children = new ArrayList<>(currentWordPosition.length);
        for (int col = 0; col < wordAndSyllables.syllables.length; col++) {
            int newSyllableIdx = currentWordPosition[col] + 1;
            if(!this.alphabetSyllables.get(col).has(newSyllableIdx))
                continue;
            int[] newWordPosition = currentWordPosition.clone();
            newWordPosition[col] = newSyllableIdx;
            children.add(createWord(newWordPosition));
        }
        return children;
    }
    private WordAndSyllables createWord(int[] syllablePositions) {
        Word[] syllables = new Word[syllablePositions.length];
        for (int i = 0; i < this.alphabetSyllables.size(); i++)
            syllables[i] = this.alphabetSyllables.get(i).get(syllablePositions[i]);
        return new WordAndSyllables(Word.concat(syllables), syllables, syllablePositions);
    }
     static class WordAndSyllables implements Comparable<WordAndSyllables> {
        final Word word;
        final Word[] syllables;
        final int[] listIndices;

        WordAndSyllables(Word word, Word[] syllables, int[] listIndices) {
            this.word = word;
            this.syllables = syllables;
            this.listIndices = listIndices;
        }

        @Override public int compareTo(WordAndSyllables o) {
            return Word.BY_PROBABILITY_DESC.compare(word, o.word);
        }
    }
}
