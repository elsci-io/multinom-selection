package io.elsci.multinomialselection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class LazyList<T> {
    private final Iterator<T> it;
    private final List<T> cache = new ArrayList<>();

    LazyList(Iterator<T> it) {
        this.it = it;
    }

    public T get(int idx) {
        if (idx >= cache.size())
            iterateToOrThrow(idx);
        return cache.get(idx);
    }
    public boolean has(int idx) {
        return iterateTo(idx);
    }

    private boolean iterateTo(int idx) {
        for (int i = cache.size(); i <= idx; i++) {
            if (!it.hasNext())
                return false;
            cache.add(it.next());
        }
        return true;
    }
    private void iterateToOrThrow(int idx) {
        if (!iterateTo(idx))
            throw new IndexOutOfBoundsException(
                    "There are only " + cache.size() + " elements in the list, can't retrieve #" + idx);
    }
}
