package io.elsci.multinomial;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SymbolSet {
    private final Map<Symbol, Integer> set = new HashMap<>();

    SymbolSet() {}
    SymbolSet(Symbol[] word) {
        for (Symbol next : word)
            add(next);
    }

    public void add(Symbol s) {
        Integer cnt = set.getOrDefault(s, 0);
        set.put(s, cnt+1);
    }
    public int getUniqueSymbolCount() {
        return set.size();
    }
    public int getWordLength() {
        int result = 0;
        for (Integer v : set.values())
            result += v;
        return result;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SymbolSet symbolSet = (SymbolSet) o;
        return set.equals(symbolSet.set);
    }
    @Override public int hashCode() {
        return Objects.hash(set);
    }
    @Override public String toString() {
        return set.toString();
    }
}
