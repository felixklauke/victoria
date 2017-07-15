package de.d3adspace.victoria.dao;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.EntityDocument;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.couchbase.client.java.repository.Repository;
import com.google.gson.Gson;
import de.d3adspace.victoria.container.EntityMetaContainer;
import de.d3adspace.victoria.container.EntityMetaContainerFactory;
import de.d3adspace.victoria.lifecycle.LifecycleWatcher;
import de.d3adspace.victoria.lifecycle.skeleton.SkeletonLifecycleWatcher;
import de.d3adspace.victoria.query.CouchbaseN1qlProxy;
import de.d3adspace.victoria.validation.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
     * Underlying bucket instance.
     */
    private final Bucket bucket;

    /**
     * Underlying repository instance.
     */
    private final Repository repository;

    /**
     * Serialization via gson.
     */
    private final Gson gson;

    /**
     * Life cycle processing.
     */
    private LifecycleWatcher<ElementType> lifecycleWatcher;

    /**
     * Create a new repository dao instance.
     *
     * @param elementClazz The class of the element to handle.
     * @param bucket The underlying bucket
     * @param repository   The underlying repository.
     */
    RepositoryDAO(Class<ElementType> elementClazz, Bucket bucket, Repository repository, Gson gson) {
        this.elementClazz = elementClazz;
        this.bucket = bucket;
        this.repository = repository;
        this.gson = gson;
        this.lifecycleWatcher = new SkeletonLifecycleWatcher<>();

        entityMetaContainer.preloadMeta(elementClazz);
    }

    @Override
    public void saveElement(ElementType element) {
        Validate.checkNotNull(element, "element cannot be null");

        int expiry = entityMetaContainer.getEntityTTL(element);
        this.saveElement(element, expiry);
    }

    @Override
    public void saveElement(ElementType element, int expiry) {
        Validate.checkNotNull(element, "element cannot be null");
        Validate.checkNotNull(expiry, "expiry cannot be null");

        String entityId = entityMetaContainer.extractId(element);
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
    public ElementType getElement(N1qlQuery query) {
        Validate.checkNotNull(query, "query cannot be null");

        List<ElementType> elements = this.getElements(query);

        if (elements.size() == 0) {
            return null;
        }

        return elements.get(0);
    }

    @Override
    public List<ElementType> getElements(N1qlQuery n1qlQuery) {
        Validate.checkNotNull(n1qlQuery, "n1qlQuery cannot be null");

        N1qlQueryResult result = CouchbaseN1qlProxy.getAllElementsByQuery(this.bucket, n1qlQuery);
        return this.getElementsFromQueryResult(result);
    }

    @Override
    public List<ElementType> getAllElements() {
        String prefix = entityMetaContainer.getIdPrefix(this.elementClazz);
        N1qlQueryResult result = CouchbaseN1qlProxy.getAllElementsByIdPrefix(this.bucket, prefix);
        return getElementsFromQueryResult(result);
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

    @Override
    public LifecycleWatcher<ElementType> getLifecycleWatcher() {
        return lifecycleWatcher;
    }

    @Override
    public void setLifecycleWatcher(LifecycleWatcher<ElementType> lifecycleWatcher) {
        this.lifecycleWatcher = lifecycleWatcher;
    }

    private List<ElementType> getElementsFromQueryResult(N1qlQueryResult result) {
        List<N1qlQueryRow> rows = result.allRows();
        if (rows.size() == 0) return new ArrayList<>();

        Map<N1qlQueryRow, ElementType> elements = rows.stream()
                .collect(Collectors.toMap(p -> p, p -> gson.fromJson(p.value().getObject(this.bucket.name()).toString(), this.elementClazz)));

        elements.forEach((queryRow, element) -> this.lifecycleWatcher.postLoad(element, queryRow));
        return new ArrayList<>(elements.values());
    }
}
