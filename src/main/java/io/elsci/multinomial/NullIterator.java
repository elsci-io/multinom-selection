package io.elsci.multinomial;

import java.util.Iterator;

class NullIterator<T> implements Iterator<T> {
    @Override public boolean hasNext() {
        return true;
    }
    @Override public T next() {
        return null;
    }
}
