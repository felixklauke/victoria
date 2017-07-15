package de.d3adspace.victoria.meta;

import de.d3adspace.victoria.annotation.EntityTTL;

import java.lang.reflect.Field;

/**
 * Basic meta data holder implementation.
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
public class SimpleEntityMeta implements EntityMeta {

    /**
     * The time to live provider.
     */
    private final EntityTTL entityTTL;

    /**
     * The field an entity holds its id in.
     */
    private final Field idField;

    /**
     * The prefix to prepend before all ids.
     */
    private final String idPrefix;

    /**
     * Create a new meta data holder by all its data.
     * @param entityTTL The ttl.
     * @param idField   The field.
     * @param idPrefix The prefix og the id.
     */
    SimpleEntityMeta(EntityTTL entityTTL, Field idField, String idPrefix) {
        this.entityTTL = entityTTL;
        this.idField = idField;
        this.idPrefix = idPrefix;
    }

    @Override
    public int getTTL() {
        return entityTTL == null ? 0 : entityTTL.value();
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public String getIdPrefix() {
        return idPrefix;
    }
}
