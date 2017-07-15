package de.d3adspace.victoria.meta;

import de.d3adspace.victoria.annotation.EntityId;
import de.d3adspace.victoria.annotation.EntityTTL;

import java.lang.reflect.Field;

/**
 * The factory to load the meta data of an entity.
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
public class EntityMetaFactory {

    /**
     * Create the meta data by the class of the entity.
     *
     * @param elementClazz The class.
     * @return The meta.
     */
    public static EntityMeta createEntityMeta(Class elementClazz) {
        EntityTTL entityTTL = (EntityTTL) elementClazz.getAnnotation(EntityTTL.class);

        Field idField = null;

        for (Field field : elementClazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(EntityId.class)) {
                field.setAccessible(true);
                idField = field;
            }
        }

        return new SimpleEntityMeta(entityTTL, idField, idPrefix);
    }
}
