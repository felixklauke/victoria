package de.d3adspace.victoria.lifecycle;

import com.couchbase.client.java.document.EntityDocument;
import com.couchbase.client.java.query.N1qlQueryRow;

/**
 * A watcher interface that will be notified about all important options of DAO
 * {@link de.d3adspace.victoria.dao.DAO}.
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
public interface LifecycleWatcher<ElementType> {

    /**
     * Called before the entityDocument will be persisted in a bucket.
     *
     * @param element        The element.
     * @param entityDocument The future document of an element.
     */
    void prePersist(ElementType element, EntityDocument<ElementType> entityDocument);

    /**
     * Called after an element was loaded by its primary ID.
     *
     * @param element The element.
     * @param entityDocument The document the entity was created from.
     */
    void postLoad(ElementType element, EntityDocument<ElementType> entityDocument);

    /**
     * Called after an element was loaded with a n1ql query.
     *
     * @param element The element.
     * @param row The row the element was created from.
     */
    void postLoad(ElementType element, N1qlQueryRow row);

    /**
     * Called after an entity was persisted in a bucket.
     *
     * @param element The element.
     * @param entityDocument The document.
     */
    void postPersist(ElementType element, EntityDocument<ElementType> entityDocument);
}
