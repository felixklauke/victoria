package de.d3adspace.victoria.meta;

import de.d3adspace.victoria.annotation.EntityTTL;

import java.lang.reflect.Field;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class SimpleEntityMeta implements EntityMeta {

    private final EntityTTL entityTTL;
    private final Field idField;

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
