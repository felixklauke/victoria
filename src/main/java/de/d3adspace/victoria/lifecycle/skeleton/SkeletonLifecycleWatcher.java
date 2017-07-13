package de.d3adspace.victoria.lifecycle.skeleton;

import com.couchbase.client.java.document.EntityDocument;
import de.d3adspace.victoria.lifecycle.LifecycleWatcher;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class SkeletonLifecycleWatcher implements LifecycleWatcher {

    @Override
    public void prePersist(Object element, EntityDocument entityDocument) {

    }

    @Override
    public void postLoad(Object element, EntityDocument entityDocument) {

    }
}
