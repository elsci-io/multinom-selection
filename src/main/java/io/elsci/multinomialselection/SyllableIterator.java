package io.elsci.multinomialselection;

import java.util.*;

import static io.elsci.multinomialselection.MathUtils.combinationProbability;

/**
 * "Syllable" is a group of symbols from the same {@link Alphabet}. It iterates over all syllable in descending order.
 * As opposed to {@link WordIterator} that can iterate across different alphabets.
 */
class SyllableIterator implements Iterator<Word> {
    private final PriorityQueue<Word> queue = new PriorityQueue<>(Word.BY_PROBABILITY_DESC);
    private final Alphabet alphabet;
    private final int syllableLength;
    private final Set<IntArray> alreadySeen = new HashSet<>();

    /**
     * @param syllableLength how many symbols of the given Alphabet are in a syllable (e.g. aaa syllable has 3 symbols)
     */
    public SyllableIterator(Alphabet alphabet, int syllableLength) {
        this.alphabet = alphabet;
        this.syllableLength = syllableLength;
        Word mostProbable = createMostProbableSyllable();
        alreadySeen.add(new IntArray(toFrequencyArray(mostProbable)));
        queue.add(mostProbable);
    }

    @Override
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    @Override
    public Word next() {
        Word head = queue.poll();
        if (head == null)
            throw new NoSuchElementException("Reached the end");
        int[][] children = childrenOf(toFrequencyArray(head));
        for (int[] symbolFreq : children) {
            IntArray freq = new IntArray(symbolFreq);
            if (alreadySeen.contains(freq))
                continue;
            alreadySeen.add(freq);
            queue.add(createSyllable(symbolFreq));
        }
        return head;
    }

    Word createMostProbableSyllable() {
        // First we make our best guess of what should be the most probable syllable, but it's only a guess.
        // After that we'll have to do navigation and explicitly check the probabilities of the neighbours
        // (aka children) - maybe they'll turn out to be more probable.
        int[] symbolFreq = new int[alphabet.probabilities.length];
        int sum = 0;
        // It's possible that the most probable symbol has probability less than 0.5, so after rounding we'll
        // end up with 0 elements of such symbol. So we have to "normalize" all the probabilities in such scenario
        // to make sure we choose >0 of them.
        double k = 1;
        double d = alphabet.probabilities[0] * this.syllableLength;
        if (d < 0.5)
            k = 0.5 / d;
        for (int i = 0; i < alphabet.probabilities.length; i++) {
            if (sum < this.syllableLength) {
                symbolFreq[i] = (int) Math.round(alphabet.probabilities[i] * this.syllableLength * k);
                sum += symbolFreq[i];
            }
        }
        // After all our trials above it's still possible that we didn't spread all the syllable length
        // across all the symbols of alphabet. E.g. if alphabet contains symbols with probabilities .85, .09, .06,
        // and we requested 5 symbols, then the logic above would generate a syllable of length 5: [4, 0, 0].
        // This is a critical problem as the length must always equal the requested value. So in these rare cases
        // we simply add what's missing to some random element (well, we choose the 1st one). After that
        // we still run the logic that navigates and finds more probable syllables - that will correct our
        // "hasty" choice anyway.
        symbolFreq[0] += (this.syllableLength - sum);
        return navigateToTheTop(symbolFreq);
    }

    /**
     * Starts with the specified symbol frequencies and steps into the direction (children) with higher probabilities
     * until it reaches the top (the word with max probability). We have to do this because there's no good way
     * to find the most probable element accurately in all cases - we can only start with our best guess, and
     * then call this method to get to the top quickly.
     *
     * @param bestGuessForMostProbable symbol frequencies of the current guess for the most probable word
     * @return a word with highest probability
     */
    private Word navigateToTheTop(int[] bestGuessForMostProbable) {
        Word currentWord = createSyllable(bestGuessForMostProbable);
        int[] currentFrequencies = bestGuessForMostProbable;
        boolean keepSearching = true;
        while (keepSearching) {
            keepSearching = false;
            for (int[] child : childrenOf(currentFrequencies)) {
                Word word = createSyllable(child);
                if (currentWord.probability < word.probability) {
                    currentWord = word;
                    currentFrequencies = child;
                    keepSearching = true;
                }
            }
        }
        return currentWord;
    }

    private Word createSyllable(int[] symbolFreq) {
        double syllableProb = combinationProbability(symbolFreq, alphabet.probabilities);
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
