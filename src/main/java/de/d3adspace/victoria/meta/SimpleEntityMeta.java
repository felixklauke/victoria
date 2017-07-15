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
     * Create a new meta data holder by all its data.
     *
     * @param entityTTL The ttl.
     * @param idField   The field.
     */
    SimpleEntityMeta(EntityTTL entityTTL, Field idField) {
        this.entityTTL = entityTTL;
        this.idField = idField;
    }

    @Override
    public int getTTL() {
        return entityTTL == null ? 0 : entityTTL.value();
    }

    @Override
    public Field getIdField() {
        return idField;
    }
}
