package de.d3adspace.victoria.dao;

import com.couchbase.client.java.query.N1qlQuery;
import de.d3adspace.victoria.lifecycle.LifecycleWatcher;

import java.util.List;

/**
 * Database access object managing basic CRUD operations on a couchbase repository.
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
public interface DAO<ElementType> {

    /**
     * Save an element.
     *
     * @param element The element.
     */
    void saveElement(ElementType element);

    /**
     * Persist an entity.
     *
     * @param element    The element.
     * @param timeToLive The time an entity has.
     */
    void saveElement(ElementType element, int timeToLive);

    /**
     * Get an element by its id.
     *
     * @param id The entity id.
     * @return The element.
     */
    ElementType getElement(String id);

    /**
     * Get a single element by a n1ql query.
     *
     * @param query The query.
     * @return The lement.
     */
    ElementType getElement(N1qlQuery query);

    /**
     * Get a collection of elements by a n1ql query.
     *
     * @param n1qlQuery The query.
     * @return The list of elements.
     */
    List<ElementType> getElements(N1qlQuery n1qlQuery);

    /**
     * Get all the available elements.
     *
     * @return The list of elements.
     */
    List<ElementType> getAllElements();

    /**
     * Remove an element by its id.
     *
     * @param id The id.
     */
    void removeElement(String id);

    /**
     * Remove an element by its instance.
     *
     * @param element The element.
     */
    void removeElement(ElementType element);

    /**
     * Check if an entity with an id exists.
     *
     * @param id The entity id.
     * @return If the entity exists.
     */
    boolean exists(String id);

    /**
     * Check if there is a database entry of the given element.
     *
     * @param element The element.
     * @return If there is a database entry.
     */
    boolean exists(ElementType element);

    /**
     * Get the watcher to handle dao operations.
     *
     * @return The watcher.
     */
    LifecycleWatcher<ElementType> getLifecycleWatcher();

    /**
     * Set the watcher. Tip: Use an {@link de.d3adspace.victoria.decorator.LifeCycleWatcherDecorator}
     *
     * @param lifecycleWatcher The watcher.
     */
    void setLifecycleWatcher(LifecycleWatcher<ElementType> lifecycleWatcher);

    /**
     * Get a list proxy {@link de.d3adspace.victoria.proxy.ListProxy}
     *
     * @param listDocumentName The name of the list in the database.
     * @return The list proxy.
     */
    List<ElementType> getListProxy(String listDocumentName);

    /**
     * Count how many arguments are available.
     *
     * @return The count of entities.
     */
    int count();
}
