package de.d3adspace.victoria.container;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public interface EntityMetaContainer {

    int getEntityTTL(Object element);

    String extractId(Object element);

    void preloadMeta(Class elementClazz);
}
