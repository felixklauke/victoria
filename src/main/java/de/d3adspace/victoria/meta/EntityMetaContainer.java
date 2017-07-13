package de.d3adspace.victoria.meta;

import de.d3adspace.victoria.annotation.EntityTTL;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public interface EntityMetaContainer {

    EntityTTL getEntityTTL(Object element);

    String extractId(Object element);

    void preloadMeta(Class elementClazz);
}
