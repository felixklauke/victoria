package de.d3adspace.victoria.lifecycle;

import com.couchbase.client.java.document.EntityDocument;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public interface LifecycleWatcher<ElementType> {

    void prePersist(ElementType element, EntityDocument<ElementType> entityDocument);

    void postLoad(ElementType element, EntityDocument<ElementType> entityDocument);
}
