package de.d3adspace.victoria.proxy;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonArrayDocument;
import com.couchbase.client.java.document.json.JsonArray;
import de.d3adspace.victoria.proxy.iterator.IteratorProxy;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class ListProxy<ElementType> extends AbstractList<ElementType> {

    private final List<ElementType> wrappedHandle;
    private final Bucket bucket;
    private final String listName;

    public ListProxy(String listName, Bucket bucket) {
        this.listName = listName;
        this.bucket = bucket;

        JsonArrayDocument jsonArrayDocument = bucket.get(listName, JsonArrayDocument.class);
        if (jsonArrayDocument == null) {
            jsonArrayDocument = JsonArrayDocument.create(listName, JsonArray.create());
            bucket.upsert(jsonArrayDocument);
        }

        JsonArray jsonArray = jsonArrayDocument.content();
        wrappedHandle = (List<ElementType>) jsonArray.toList();
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
        return new IteratorProxy<>(this.wrappedHandle.iterator(), this::remove);
    }
}
