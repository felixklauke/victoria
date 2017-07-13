package de.d3adspace.victoria.dao;

import com.couchbase.client.java.repository.Repository;
import de.d3adspace.victoria.annotation.EntityWatcher;
import de.d3adspace.victoria.lifecycle.LifecycleWatcher;

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
        RepositoryDAO<ElementType> dao = new RepositoryDAO<>(elementClazz, repository);

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
