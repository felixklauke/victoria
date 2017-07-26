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
import de.d3adspace.victoria.exception.VictoriaException;
import de.d3adspace.victoria.executor.VictoriaThreadFactory;
import de.d3adspace.victoria.lifecycle.LifecycleWatcher;
import de.d3adspace.victoria.proxy.ProxiedDatastructures;
import de.d3adspace.victoria.query.CouchbaseN1qlProxy;
import de.d3adspace.victoria.skeleton.SkeletonLifecycleWatcher;
import de.d3adspace.victoria.validation.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
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
     * The executor for async operations
     */
    private final ExecutorService executorService;

    /**
     * Life cycle processing.
     */
    private LifecycleWatcher<ElementType> lifecycleWatcher;

    /**
     * Create a new repository dao instance.
     *
     * @param elementClazz The class of the element to handle.
     * @param bucket       The underlying bucket
     * @param repository   The underlying repository.
     */
    RepositoryDAO(Class<ElementType> elementClazz, Bucket bucket, Repository repository, Gson gson) {
        this.elementClazz = elementClazz;
        this.bucket = bucket;
        this.repository = repository;
        this.gson = gson;
        this.lifecycleWatcher = new SkeletonLifecycleWatcher<>();
        this.executorService = Executors.newFixedThreadPool(4, new VictoriaThreadFactory());

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

        this.executorService.execute(() -> {
            this.lifecycleWatcher.prePersist(element, entityDocument);
            this.repository.upsert(entityDocument);
            this.lifecycleWatcher.postPersist(element, entityDocument);
        });
    }

    @Override
    public ElementType getElement(String id) {
        Validate.checkNotNull(id, "id cannot be null");

        EntityDocument<ElementType> entityDocument = repository.get(id, elementClazz);

        ElementType element = entityDocument == null ? null : entityDocument.content();

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

        N1qlQueryResult result = CouchbaseN1qlProxy.executeQuery(this.bucket, n1qlQuery);
        return this.getElementsFromQueryResult(result);
    }

    @Override
    public List<ElementType> getAllElements() {
        N1qlQueryResult result = null;

        String type = entityMetaContainer.getEntityType(this.elementClazz);
        if (type != null) {
            result = CouchbaseN1qlProxy.getAllElementsByType(this.bucket, type);
        } else {
            String prefix = entityMetaContainer.getIdPrefix(this.elementClazz);
            result = CouchbaseN1qlProxy.getAllElementsByIdPrefix(this.bucket, prefix);
        }

        if (result == null) {
            throw new VictoriaException("Could not fetch all elements. You should check for an @EntityType annotation or a valid prefix.");
        }

        return getElementsFromQueryResult(result);
    }

    @Override
    public void getElement(String id, Consumer<ElementType> consumer) {
        Validate.checkNotNull(id, "id cannot be null");
        Validate.checkNotNull(consumer, "consumer cannot be null");

        this.executorService.execute(() -> consumer.accept(this.getElement(id)));
    }

    @Override
    public void getElement(N1qlQuery query, Consumer<ElementType> consumer) {
        Validate.checkNotNull(query, "query cannot be null");
        Validate.checkNotNull(consumer, "consumer cannot be null");

        this.executorService.execute(() -> consumer.accept(this.getElement(query)));
    }

    @Override
    public void getElements(N1qlQuery n1qlQuery, Consumer<List<ElementType>> consumer) {
        Validate.checkNotNull(n1qlQuery, "query cannot be null");
        Validate.checkNotNull(consumer, "consumer cannot be null");

        this.executorService.execute(() -> consumer.accept(this.getElements(n1qlQuery)));
    }

    @Override
    public void getAllElements(Consumer<List<ElementType>> consumer) {
        Validate.checkNotNull(consumer, "consumer cannot be null");

        this.executorService.execute(() -> consumer.accept(this.getAllElements()));
    }

    @Override
    public void removeElement(String id) {
        Validate.checkNotNull(id, "id cannot be null");

        this.executorService.execute(() -> repository.remove(id, elementClazz));
    }

    @Override
    public void removeElement(ElementType element) {
        Validate.checkNotNull(element, "element cannot be null");

        String entityId = entityMetaContainer.extractId(element);
        EntityDocument<ElementType> entityDocument = EntityDocument.create(entityId, element);

        this.executorService.execute(() -> this.repository.remove(entityDocument));
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
        Validate.checkNotNull(lifecycleWatcher, "lifecycleWatcher cannot be null");

        this.lifecycleWatcher = lifecycleWatcher;
    }

    @Override
    public List<ElementType> getListProxy(String listDocumentName) {
        Validate.checkNotNull(listDocumentName, "listDocumentName cannot be null");

        return ProxiedDatastructures.createList(listDocumentName, this.bucket);
    }

    @Override
    public int count() {
        return this.getAllElements().size();
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
