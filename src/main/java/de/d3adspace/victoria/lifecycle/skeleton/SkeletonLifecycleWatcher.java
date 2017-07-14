package de.d3adspace.victoria.lifecycle.skeleton;

import com.couchbase.client.java.document.EntityDocument;
import com.couchbase.client.java.query.N1qlQueryRow;
import de.d3adspace.victoria.lifecycle.LifecycleWatcher;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class SkeletonLifecycleWatcher<ElementType> implements LifecycleWatcher<ElementType> {

    @Override
    public void prePersist(ElementType element, EntityDocument<ElementType> entityDocument) {

    }

    @Override
    public void postLoad(ElementType element, EntityDocument<ElementType> entityDocument) {

    }

    @Override
    public void postLoad(ElementType element, N1qlQueryRow row) {

    }

    @Override
    public void postPersist(ElementType element, EntityDocument<ElementType> entityDocument) {

    }
}
