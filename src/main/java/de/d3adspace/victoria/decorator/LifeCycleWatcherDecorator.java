package de.d3adspace.victoria.decorator;

import com.couchbase.client.java.document.EntityDocument;
import com.couchbase.client.java.query.N1qlQueryRow;
import de.d3adspace.victoria.lifecycle.LifecycleWatcher;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class LifeCycleWatcherDecorator<ElementType> implements LifecycleWatcher<ElementType> {

    private final LifecycleWatcher<ElementType> lifecycleWatcher;

    public LifeCycleWatcherDecorator(LifecycleWatcher<ElementType> lifecycleWatcher) {
        this.lifecycleWatcher = lifecycleWatcher;
    }

    @Override
    public void prePersist(ElementType element, EntityDocument<ElementType> entityDocument) {
        lifecycleWatcher.prePersist(element, entityDocument);
    }

    @Override
    public void postLoad(ElementType element, EntityDocument<ElementType> entityDocument) {
        lifecycleWatcher.postLoad(element, entityDocument);
    }

    @Override
    public void postLoad(ElementType element, N1qlQueryRow row) {
        lifecycleWatcher.postLoad(element, row);
    }

    @Override
    public void postPersist(ElementType element, EntityDocument<ElementType> entityDocument) {
        lifecycleWatcher.postPersist(element, entityDocument);
    }
}
