package de.d3adspace.victoria.dao;

import com.couchbase.client.java.query.N1qlQuery;

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
}
