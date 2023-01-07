package io.elsci.multinomial;

import org.junit.Test;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.config.IsotopeFactory;
import org.openscience.cdk.config.Isotopes;
import org.openscience.cdk.interfaces.IIsotope;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import java.io.IOException;
import java.util.*;

public class FromCdk {

    public static Iterator<Word> isotopeIterator(String formula) {
        IMolecularFormula molecularFormula = MolecularFormulaManipulator.getMajorIsotopeMolecularFormula(formula, DefaultChemObjectBuilder.getInstance());
        HashMap<Alphabet, Integer> hashMap = new HashMap<>();
        for (IIsotope isos : molecularFormula.isotopes()) {
            String elementSymbol = isos.getSymbol();
            IsotopeFactory isoFactory;
            try {
                isoFactory = Isotopes.getInstance();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }

            IIsotope[] isotopes = isoFactory.getIsotopes(elementSymbol);
            List<Double> probabilities = new ArrayList<>();
            for (IIsotope isotope : isotopes) {
                Double abundance = isotope.getNaturalAbundance();
                if (!(abundance.isNaN() || abundance.isInfinite() || abundance < 1e-9))
                    probabilities.add(abundance * 0.01);
            }
            hashMap.put(new Alphabet(elementSymbol, toSortedArray(probabilities)), molecularFormula.getIsotopeCount(isos));
        }
        return WordIteratorFactory.create(new WordSpec(hashMap));
    }
    private static double[] toSortedArray(List<Double> probabilities) {
        probabilities.sort(Comparator.comparingDouble((d)->(double)d).reversed());
        double[] probArray = new double[probabilities.size()];
        for (int i = 0; i < probabilities.size(); i++)
            probArray[i] = probabilities.get(i);
        return probArray;
    }

    @Test
    public void returnsIsotopeIterator() {
        Iterator<Word> it = FromCdk.isotopeIterator("C257H383N65O77S6");
        double maxProb = it.next().probability;
        double minIntensity = 0.01 * maxProb;
        int i = 0;
        while (it.hasNext()) {
            i++;
            if (it.next().probability < minIntensity)
                break;
        }
        System.out.println(i);
    }
}
