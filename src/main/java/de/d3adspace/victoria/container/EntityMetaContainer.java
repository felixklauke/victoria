package de.d3adspace.victoria.container;

/**
 * Container that provides meta for all entities.
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
public interface EntityMetaContainer {

    /**
     * Get the ttl for an element that will be going to be persisted.
     *
     * @param element The element.
     * @return The ttl in seconds.
     */
    int getEntityTTL(Object element);

    /**
     * Get the ID of an element.
     *
     * @param element The element.
     * @return The id.
     */
    String extractId(Object element);

    String getIdPrefix(Class elementCLazz);

    /**
     * Preload entity meta data.
     *
     * @param elementClazz The class of the element.
     */
    void preloadMeta(Class elementClazz);
}
