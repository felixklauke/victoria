package de.d3adspace.victoria.dao;

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

    ElementType getElement(String id);

    void removeElement(String id);

    void removeElement(ElementType element);

    boolean exists(String id);

    boolean exists(ElementType element);
}
