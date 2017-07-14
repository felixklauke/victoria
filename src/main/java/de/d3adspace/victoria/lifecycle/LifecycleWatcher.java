package de.d3adspace.victoria.lifecycle;

import com.couchbase.client.java.document.EntityDocument;
import com.couchbase.client.java.query.N1qlQueryRow;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public interface LifecycleWatcher<ElementType> {

    void prePersist(ElementType element, EntityDocument<ElementType> entityDocument);

    void postLoad(ElementType element, EntityDocument<ElementType> entityDocument);

    void postLoad(ElementType element, N1qlQueryRow row);

    void postPersist(ElementType element, EntityDocument<ElementType> entityDocument);
}
