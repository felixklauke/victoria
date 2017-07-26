package de.d3adspace.victoria.dao;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.repository.Repository;
import com.google.gson.Gson;
import de.d3adspace.victoria.annotation.EntityWatcher;
import de.d3adspace.victoria.lifecycle.LifecycleWatcher;
import de.d3adspace.victoria.validation.Validate;

/**
 * Factory for basic dao implementation.
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
public class DAOFactory {

    /**
     * Create a new dao instance.
     *
     * @param <ElementType> The type of the element to handle.
     * @param elementClazz  The class of the elements to handle.
     * @param repository    The underlying repository.
     * @param bucket        The bucket the dao will work with.
     * @param gson          The gson instance.
     * @return The dao instance.
     */
    public static <ElementType> DAO<ElementType> createDAO(Class<ElementType> elementClazz, Repository repository, Bucket bucket, Gson gson) {
        Validate.checkNotNull(elementClazz, "elementClazz cannot be null");
        Validate.checkNotNull(repository, "repository cannot be null");
        Validate.checkNotNull(bucket, "bucket cannot be null");
        Validate.checkNotNull(gson, "gson cannot be null");

        RepositoryDAO<ElementType> dao = new RepositoryDAO<>(elementClazz, bucket, repository, gson);

        if (elementClazz.isAnnotationPresent(EntityWatcher.class)) {
            try {
                LifecycleWatcher<ElementType> lifecycleWatcher = elementClazz.getAnnotation(EntityWatcher.class).value().newInstance();
                dao.setLifecycleWatcher(lifecycleWatcher);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return dao;
    }
}
