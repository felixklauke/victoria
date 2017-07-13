package de.d3adspace.victoria.proxy;

import com.couchbase.client.java.Bucket;

import java.util.*;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class ListProxy<ElementType> extends AbstractList<ElementType> {

    private final List<ElementType> wrappedHandle;
    private final Bucket bucket;
    private final String listName;

    public ListProxy(String listName, Bucket bucket) {
        this.listName = listName;
        this.wrappedHandle = new ArrayList<>();
        this.bucket = bucket;
    }

    @Override
    public ElementType get(int index) {
        return wrappedHandle.get(index);
    }

    @Override
    public int size() {
        return this.wrappedHandle.size();
    }

    @Override
    public boolean add(ElementType element) {
        boolean success = this.wrappedHandle.add(element);

        if (success) {
            this.bucket.listAppend(this.listName, element);
        }

        return success;
    }

    @Override
    public boolean addAll(Collection<? extends ElementType> elementCollection) {
        elementCollection.forEach(this::add);
        return true;
    }

    @Override
    public void add(int index, ElementType element) {
        this.bucket.listSet(this.listName, index, element);

        this.wrappedHandle.add(index, element);
    }

    @Override
    public Iterator<ElementType> iterator() {
        return super.iterator();
    }
}
