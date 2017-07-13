package de.d3adspace.victoria.dao;

import com.couchbase.client.java.document.EntityDocument;
import com.couchbase.client.java.repository.Repository;

/**
 * Basic DAO implementation.
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
public class RepositoryDAO<ElementType> implements DAO<ElementType> {

    /**
     * Class of the elements to handle.
     */
    private final Class<ElementType> elementClazz;

    /**
     * Underlying repository instance.
     */
    private final Repository repository;

    /**
     * Create a new repository dao instance.
     *
     * @param elementClazz The class of the element to handle.
     * @param repository   The underlying repository.
     */
    RepositoryDAO(Class<ElementType> elementClazz, Repository repository) {
        this.elementClazz = elementClazz;
        this.repository = repository;
    }

    @Override
    public void saveElement(ElementType element) {

        //TODO: IDs
        EntityDocument<ElementType> entityDocument = EntityDocument.create(element);

        this.repository.upsert(entityDocument);
    }

    @Override
    public ElementType getElement(String id) {
        return repository.get(id, elementClazz).content();
    }

    @Override
    public void removeElement(String id) {
        repository.remove(id, elementClazz);
    }

    @Override
    public void removeElement(ElementType element) {

        //TODO: IDs
        EntityDocument<ElementType> entityDocument = EntityDocument.create(element);
        this.repository.remove(entityDocument);
    }

    @Override
    public boolean exists(String id) {
        return this.repository.exists(id);
    }

    @Override
    public boolean exists(ElementType element) {
        //TODO: IDs
        EntityDocument<ElementType> entityDocument = EntityDocument.create(element);

        return this.repository.exists(entityDocument);
    }
}
