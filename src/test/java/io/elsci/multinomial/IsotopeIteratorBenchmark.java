package io.elsci.multinomial;

import org.openjdk.jmh.annotations.*;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.formula.IsotopePattern;
import org.openscience.cdk.formula.IsotopePatternGenerator;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import java.util.Iterator;

public class IsotopeIteratorBenchmark {
    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(new String[]{IsotopeIteratorBenchmark.class.getSimpleName()});
    }

    @Benchmark @Fork(value = 1, warmups = 0)
    public void quick(Data data) {
        Iterator<Word> wordIterator = FromCdk.isotopeIterator(data.formula);
        double minProb = data.minIntensity * wordIterator.next().probability;
        while(wordIterator.hasNext()) {
            if (wordIterator.next().probability< minProb)
                break;
        }
    }
    @Benchmark @Fork(value = 1, warmups = 0)
    public void cdk(Data data) {
        data.ISOTOPE_PATTERN_GENERATOR.getIsotopes(data.molecularFormula);
    }

    @State(Scope.Benchmark)
    public static class Data {
        double minIntensity = 0.01;
        IsotopePatternGenerator ISOTOPE_PATTERN_GENERATOR = new IsotopePatternGenerator(minIntensity);
        IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
        String formula = "C33H38GdN3Na5O14P";
        IMolecularFormula molecularFormula = MolecularFormulaManipulator.getMolecularFormula(formula, builder);

    }
}
