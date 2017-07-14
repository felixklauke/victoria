package de.d3adspace.victoria.dao;

import com.couchbase.client.java.document.EntityDocument;
import com.couchbase.client.java.repository.Repository;
import de.d3adspace.victoria.annotation.EntityTTL;
import de.d3adspace.victoria.lifecycle.LifecycleWatcher;
import de.d3adspace.victoria.lifecycle.skeleton.SkeletonLifecycleWatcher;
import de.d3adspace.victoria.meta.EntityMetaContainer;
import de.d3adspace.victoria.meta.EntityMetaContainerFactory;
import de.d3adspace.victoria.validation.Validate;

/**
 * Basic DAO implementation.
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
public class RepositoryDAO<ElementType> implements DAO<ElementType> {

    /**
     * Meta storage
     */
    private static final EntityMetaContainer entityMetaContainer = EntityMetaContainerFactory.createEntityMetaContainer();

    /**
     * Class of the elements to handle.
     */
    private final Class<ElementType> elementClazz;
    /**
     * Underlying repository instance.
     */
    private final Repository repository;

    /**
     * Life cycle processing.
     */
    private LifecycleWatcher<ElementType> lifecycleWatcher;

    /**
     * Create a new repository dao instance.
     *
     * @param elementClazz The class of the element to handle.
     * @param repository   The underlying repository.
     */
    RepositoryDAO(Class<ElementType> elementClazz, Repository repository) {
        this.elementClazz = elementClazz;
        this.repository = repository;
        this.lifecycleWatcher = new SkeletonLifecycleWatcher<>();

        entityMetaContainer.preloadMeta(elementClazz);
    }

    @Override
    public void saveElement(ElementType element) {
        Validate.checkNotNull(element, "element cannot be null");

        String entityId = entityMetaContainer.extractId(element);
        EntityTTL entityTTL = entityMetaContainer.getEntityTTL(element);
        int expiry = entityTTL == null ? 0 : entityTTL.value();

        EntityDocument<ElementType> entityDocument = EntityDocument.create(entityId, expiry, element);

        this.lifecycleWatcher.prePersist(element, entityDocument);
        this.repository.upsert(entityDocument);
        this.lifecycleWatcher.postPersist(element, entityDocument);
    }

    @Override
    public ElementType getElement(String id) {
        Validate.checkNotNull(id, "id cannot be null");

        EntityDocument<ElementType> entityDocument = repository.get(id, elementClazz);
        ElementType element = entityDocument.content();

        this.lifecycleWatcher.postLoad(element, entityDocument);

        return element;
    }

    @Override
    public void removeElement(String id) {
        Validate.checkNotNull(id, "id cannot be null");

        repository.remove(id, elementClazz);
    }

    @Override
    public void removeElement(ElementType element) {
        Validate.checkNotNull(element, "element cannot be null");

        String entityId = entityMetaContainer.extractId(element);

        EntityDocument<ElementType> entityDocument = EntityDocument.create(entityId, element);
        this.repository.remove(entityDocument);
    }

    @Override
    public boolean exists(String id) {
        Validate.checkNotNull(id, "element cannot be null");

        return this.repository.exists(id);
    }

    @Override
    public boolean exists(ElementType element) {
        Validate.checkNotNull(element, "element cannot be null");

        String entityId = entityMetaContainer.extractId(element);

        EntityDocument<ElementType> entityDocument = EntityDocument.create(entityId, element);
        return this.repository.exists(entityDocument);
    }

    void setLifecycleWatcher(LifecycleWatcher<ElementType> lifecycleWatcher) {
        this.lifecycleWatcher = lifecycleWatcher;
    }
}
