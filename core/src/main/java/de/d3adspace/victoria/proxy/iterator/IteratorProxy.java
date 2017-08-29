package de.d3adspace.victoria.proxy.iterator;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class IteratorProxy<ElementType> implements Iterator<ElementType> {

    private final Iterator<ElementType> wrappedIterator;
    private final Consumer<ElementType> removalConsumer;
    private ElementType currentElement;

    public IteratorProxy(Iterator<ElementType> wrappedIterator, Consumer<ElementType> removalConsumer) {
        this.wrappedIterator = wrappedIterator;
        this.removalConsumer = removalConsumer;
    }

    @Override
    public boolean hasNext() {
        return wrappedIterator.hasNext();
    }

    @Override
    public ElementType next() {
        return (currentElement = wrappedIterator.next());
    }

    @Override
    public void remove() {
        removalConsumer.accept(currentElement);
        wrappedIterator.remove();
        currentElement = null;
    }
}
