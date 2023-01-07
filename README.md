# Multinomial selection in Java

Suppose you're picking 100 random balls from a bag. The probabilities of picking Red, Blue, Green are 0.95, 0.03, 0.02. What's the most probable combination? What are _k_ most probable combinations? This is a problem of Multinomial Selection and it shows up in different contexts. E.g. in Mass Spectrometry, given a Molecular Formula $C_{20}H_{28}Cl_4N_2O_4$, we need to know the most probable isotope combination.

This library is a generic implementation that doesn't depend on a particular application. The application-specific libraries can wrap this one. Here's how to iterate over all possible combinations starting with the most probable:

```java
// Alphabets represent an Element (a single Bag of balls) - they
// have Symbols (Red, Blue, Green balls) with different probabilities.
//
// In Chemistry terminology an Alphabet is an Element, while Symbol
// is a particular isotope (well, its probability): 
Alphabet c = new Alphabet("C", .99, .1);
Alphabet cl = new Alphabet("Cl", .75, .25);
// Once we have our "Bags", we want to tell how many Symbols (Balls)
// of each Alphabet (Bag) we want to pull:  
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

The algorithm scales roughly at $O(k \times log(a \times s))$ (to be calculated precisely), where $k$ is number of words we select, $a$ is the number of Alphabets and $s$ is the number of Symbols in each Alphabet.

# Multinomial selection and Isotope Distribution

When doing Mass Spectrometry, after a spectrum is captured, we need to determine which of the masses within that spectrum belong to our compound. A compound consists of atoms, each of them may represent isotopes with different masses. Outside of chemistry this problem mostly boils down to **selecting most probable events out of a multinomial**. 

For instance $H_2O$ consists of 2 elements: $H$ and $O$. Each of them has a number of isotopes (e.g. $^1H$, $^2H$, $^{16}O$, $^{18}O$, etc), so eventually we end up with a lot of possible combinations:

| Isotopologue   | Molecular Mass                            | Probability                                        | Mass Spec Intensity |
|----------------|-------------------------------------------|----------------------------------------------------|---------------------|
| $^1H^1H^{16}O$ | $1.007825 + 1.007825 + 16.99913=19.01478$ | $.99985^2 \times .99762=.9973207$                  | 1                   |
| $^1H^1H^{18}O$ | $1.007825 + 1.007825 + 17.99916=20.01481$ | $.99985^2 \times .002=.002$                        | 0.002005373         |
| $^2H^1H^{16}O$ | $2.014102 + 1.007825 + 16.99913=20.02106$ | $2\times .00015 \times .99985 \times .99762=.0003$ | 0.0003008059        |
| ...            | ...                                       | ...                                                | ...                 |

We don't really want to go over all the combinations as most of them are too rare. In the example above only the primary isotopologue is interesting, the rest can be ignored. But in larger molecules we may be interested in a couple of dozens of isotopologues. And we need a way to list the ones that we'll be interested in. The naive approach will result in a quadratic algorithm which is too slow for large molecules.

## Multinonmial math

In mathematical terms all possible combinations and their probabilities can be generated using a multinomial:

$$
\text{Distribution of isotopologues}=(a_{1}^{\alpha_1}+a_{2}^{\alpha_2}+...+a_{n}^{\alpha_n})^A + (b_{1}^{\beta_1}+b_{2}^{\beta_2}+...+b_{n}^{\beta_n})^B
$$

where
* $a1$, $a2$ are probabilities of 1st and 2nd isotope of first element (basically $p(^1H)$, $p(^2H)$ in our example at the top) and $b_1$, $b_2$ are isotopes of 2nd element (it's $p(^{16}O)$ and $p(^{18}O)$ probabilities).
* $\alpha_1$, $\alpha_2$ are molecular mass of that isotope
* $A$ and $B$ is the number of repeats of these elements (2 and 1 in our example).

Try it for $H_2O$ and you'll see that the probabilities will turn out just right as they are multiplied, while the $\alpha_n$ powers will be added - which represents the sum of masses of isotopes in a molecule.