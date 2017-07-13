package de.d3adspace.victoria.dao;

import com.couchbase.client.java.repository.Repository;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class DAOFactory {

    public static <ElementType> DAO<ElementType> createDAO(Class<ElementType> elementClazz, Repository repository) {
        return new RepositoryDAO<>(elementClazz, repository);
    }
}
