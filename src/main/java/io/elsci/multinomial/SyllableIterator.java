package io.elsci.multinomial;

import java.util.*;

import static io.elsci.multinomial.MathUtils.multinomMassFunction;

class SyllableIterator implements Iterator<Word> {
    private final PriorityQueue<Word> queue = new PriorityQueue<>(Word.BY_PROBABILITY_DESC);
    private final Alphabet alphabet;
    private final int syllableLength;
    private final Set<SymbolFrequencies> alreadySeen = new HashSet<>();

    public SyllableIterator(Alphabet alphabet, int syllableLength) {
        this.alphabet = alphabet;
        this.syllableLength = syllableLength;
        Word mostProbable = createMostProbableSyllable();
        alreadySeen.add(new SymbolFrequencies(toFrequencyArray(mostProbable)));
        queue.add(mostProbable);
    }

    @Override
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    @Override
    public Word next() {
        Word head = queue.poll();
        if(head == null)
            throw new NoSuchElementException("Reached the end");
        int[][] children = childrenOf(toFrequencyArray(head));
        for (int[] symbolFreq : children) {
            SymbolFrequencies freq = new SymbolFrequencies(symbolFreq);
            if(alreadySeen.contains(freq))
                continue;
            alreadySeen.add(freq);
            queue.add(createSyllable(symbolFreq));
        }
        return head;
    }
    public Word peekAtNext() {
        return queue.peek();
    }

    private Word createMostProbableSyllable() {
        int[] symbolFreq = new int[alphabet.probabilities.length];
        for (int i = 0; i < alphabet.probabilities.length; i++)
            // TODO: will this always round correctly?
            symbolFreq[i] = (int) Math.round(alphabet.probabilities[i] * this.syllableLength);
        return createSyllable(symbolFreq);
    }
    private Word createSyllable(int[] symbolFreq) {
        double syllableProb = multinomMassFunction(symbolFreq, alphabet.probabilities);
        return new Word(new ArrayBasedSymbolSet(alphabet, symbolFreq), syllableProb);
    }
    private static int[] toFrequencyArray(Word mostProbable) {
        return ((ArrayBasedSymbolSet) mostProbable.symbols).symbolFrequencies;
    }

    /**
     * It returns elements with Manhattan Distance = 2. We need this to find a symbolSet with the next highest
     * probability - and it's going to differ from the "parent" symbolSet by 1 symbol transfer. Since that symbol
     * will be transferred to the other one, we'll have differences in 2 positions - which effectively results in
     * Manhattan Distance = 2.
     * <p>
     * It is supposed to be used in {@link #next()} to enumerate all children of a given symbolSet.
     *
     * @param symbolSet is an array where the index of each element represents an index of the symbol of an alphabet
     *                  and its value is the number of this symbol occurrences. It's very similar to {@link MapBasedSymbolSet},
     *                  just a different structure.
     * @return all children, which means all possible variants of changes to symbolSet with the step of Manhattan
     * distance = 2
     */
    static int[][] childrenOf(int[] symbolSet) {
        int numbersLength = symbolSet.length;
        if (numbersLength == 1)
            return new int[0][];
        int f = 0;
        for (int num : symbolSet) {
            if (num != 0)
                f++;
        }
        int[][] result = new int[f * (numbersLength - 1)][numbersLength];
        for (int i = 0; i < result.length; i++)
            result[i] = symbolSet.clone();
        int i = 0;
        outer:
        while (true) {
            for (int j = 0; j < numbersLength; j++) {
                for (int e = 0; e < numbersLength; e++) {
                    if (e != j && result[i][e] != 0) {
                        result[i][j]++;
                        result[i][e]--;
                        if (++i >= result.length)
                            break outer;
                    }
                }
            }
        }
        return result;
    }
}
