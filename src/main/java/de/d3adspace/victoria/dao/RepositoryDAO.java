package de.d3adspace.victoria.dao;

import com.couchbase.client.java.document.EntityDocument;
import com.couchbase.client.java.repository.Repository;

/**
 *
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
public class RepositoryDAO<ElementType> implements DAO<ElementType> {

    private final Class<ElementType> elementClazz;
    private final Repository repository;

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
