package de.d3adspace.victoria.dao;

import com.couchbase.client.java.repository.Repository;

/**
 * Factory for basic dao implementation.
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
public class DAOFactory {

    /**
     * Create a new dao instance.
     *
     * @param elementClazz  The class of the elements to handle.
     * @param repository    The underlying repository.
     * @param <ElementType> The type of the element to handle.
     * @return The dao instance.
     */
    public static <ElementType> DAO<ElementType> createDAO(Class<ElementType> elementClazz, Repository repository) {
        return new RepositoryDAO<>(elementClazz, repository);
    }
}
