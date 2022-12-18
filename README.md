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

# Inputs & Outputs

## Alphabets

To remove chemistry specifics from this problem and make it more generic, we can abstract away from chemistry and just say that each Element (e.g. $O$) is an alphabet of isotopes. Which means that if we have a formula like $H_2O$ we need 2 alphabets. Though in practice we'll fill all possible isotopes for all elements during some set up step.

There are couple of ways we can set up the alphabets:

```java
Alphabets alphabets = new Alphabets(new double[][]{
    {.99762, .002}, // Oxygen isotopes and their probabilities
    {.99985, .00015} // Hydrogen isotopes and their probabilities
    // when building alphabets for chemistry we want to specify all elements 
    // from the periodic table and all the isotopes we're interested in 
});
```
Or this way, with an additional `Alphabet` class:

```java
Alphabets alphabets = new Alphabets(
    new Alphabet(.99762, .002), 
    new Alphabet(.99985, .00015)
);
```

## Words

Now we want to generate words out of the alphabets. But first we need to describe a specification that tells us which alphabets (elements) we should use in the word and how many letters of each alphabet there will be:

```java
WordSpec spec = new WordSpec(Map.of(
         // Reference the alphabet and the number of elements that we want to sample from that alphabet
         0, 2, // H2   
         1, 1  // O
));
Iterator<Word> words = new WordGenerator(alphabets).generate(spec);
```

In mathematical terms all possible combinations and their probabilities can be generated using a multinomial $(a_1+a_2+...+a_n)^A + (b_1+b_2+...+b_n)^B$ where $a1$, $a2$ are probabilities of 1st and 2nd isotope of first element (basically $p(^1H)$, $p(^2H)$ in our example at the top) and $b_1$, $b_2$ are isotopes of 2nd element (it's $p(^{16}O)$ and $p(^{18}O)$ probabilities). And $A$ and $B$ is the number of repeats of these elements (2 and 1 in our example).

## Isotopologues

Chemistry and isotopologues is just one of the applications. For each such application we will create a wrapper that would call the core logic, but it will expose domain-specific classes and methods. And we can also use application-specific libs (like CDK for chemistry) to set up the Alphabets at the beginning. So the client code of such wrapper would look like this:  

```java
Mf mf = new Mf("H2O");
Iterator<Isotopologue> isotopologues = new IsotopologueGenerator(isotopesAbandances).generate(mf);
```