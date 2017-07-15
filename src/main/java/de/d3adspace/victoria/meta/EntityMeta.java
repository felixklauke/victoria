package de.d3adspace.victoria.meta;

import java.lang.reflect.Field;

/**
 * Meta data of the entity used in the couchbase environment.
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
public interface EntityMeta {

    /**
     * Get the time to live in seconds.
     *
     * @return The ttl.
     */
    int getTTL();

    /**
     * Get the field an entity holds its id in.
     *
     * @return The field.
     */
    Field getIdField();
}
