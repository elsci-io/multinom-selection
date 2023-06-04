# Multinomial selection in Java [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.elsci.multinomial-selection/multinomial-selection/badge.svg)](https://central.sonatype.com/artifact/io.elsci.multinomial-selection/multinomial-selection/)

Suppose you're picking 100 random balls from a bag. The probabilities of picking Red, Blue, Green are 0.95, 0.03, 0.02. What's the most probable combination? What are _k_ most probable combinations? What if we had to pull from yet another bag of cubes of different colors - then what are possible combinations of balls and cubes that we can pull? This is a problem of Multinomial Selection and it shows up in different contexts. E.g. in Mass Spectrometry, given a Molecular Formula $C_{20}H_{28}Cl_4N_2O_4$, we need to know the most probable isotope combination.

## How to use

This library is a generic implementation that doesn't depend on a particular application. The application-specific libraries can wrap this one (like [isotope-distribution](https://github.com/elsci-io/isotope-distribution)). Here's how to iterate over all possible combinations starting with the most probable:

```java
// Alphabets represent an Element (a single Bag of balls) - they
// have Symbols (Red, Blue, Green balls) with different probabilities.
//
// In Chemistry terminology an Alphabet is an Element, while Symbol
// is a particular isotope (well, its probability): 
Alphabet c = new Alphabet("C", .99, .1);
Alphabet cl = new Alphabet("Cl", .75, .25);
// Once we have our "Bags", we want to tell how many Symbols (Balls)
// of each Alphabet (Bag) we want to pull (which the map represents):  
Map<Alphabet, Integer> wordSpec = new HashMap<>();
wordSpec.put(c, 15);
wordSpec.put(cl, 5);
// We have all the data, it's time to iterate over all possibilities,
// starting with the most probable:
Iterator<Word> it = WordIteratorFactory.create(new WordSpec(wordSpec));
while(it.hasNext()) {
    Word next = it.next();
    System.out.println(next.probability + ": " + next.symbols);
}
```

The implementation resembles (a little) the one described in [Fast Exact Computation of the k Most Abundant Isotope Peaks with Layer-Ordered Heaps](https://pubs.acs.org/doi/10.1021/acs.analchem.0c01670#). Most likely we're somewhat slower.

The algorithm scales at $O(k^2 \times a^2 \times s^2 \times (log(k \times s) + s))$ , where $k$ is number of words we select, $a$ is the number of alphabets and $s$ is the number of symbols in an alphabet.  
k: $O(k^2 \times log(k))$  
a: $O(a^2)$  
s: $O(s^3)$  
We assume that f in method childrenOf() in SyllableIterator is constant for small k, in other case the algorithm becomes more difficult.

## Maven coordinates

Use Maven to download the library (latest version: [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.elsci.multinomial-selection/multinomial-selection/badge.svg)](https://central.sonatype.com/artifact/io.elsci.multinomial-selection/multinomial-selection/)):

```xml
<dependency>
    <groupId>io.elsci.multinomial-selection</groupId>
    <artifactId>multinomial-selection</artifactId>
    <version>LATEST VERSION</version>
</dependency>
```

## Multinonmial math

In mathematical terms all possible combinations and their probabilities can be generated using a multinomial:

$$
\text{Distribution of probabilities}=(a_{1}+a_{2}+...+a_{n})^A (b_{1}+b_{2}+...+b_{n})^B
$$

where
* $a1$, $a2$ are probabilities of 1st and 2nd elements of the first set (say.. probability of blue and red ball) and $b_1$, $b_2$ are probabilities of the elements of the 2nd set (say.. probabilities of blue and red cubes)
* $A$ and $B$ is the number of repeats of these elements (taking 2 balls and 3 cubes).