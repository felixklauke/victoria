package de.d3adspace.victoria.meta;

import de.d3adspace.victoria.annotation.EntityId;
import de.d3adspace.victoria.annotation.EntityTTL;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class SimpleEntityMetaContainer implements EntityMetaContainer {

    private final Map<Class, EntityTTL> entityTTLs;
    private final Map<Class, Field> entityIds;

    SimpleEntityMetaContainer() {
        this.entityTTLs = new HashMap<>();
        this.entityIds = new HashMap<>();
    }

    @Override
    public EntityTTL getEntityTTL(Object element) {
        return this.entityTTLs.get(element.getClass());
    }

    @Override
    public String extractId(Object element) {
        try {
            return String.valueOf(this.entityIds.get(element.getClass()).get(element));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return "ERROR";
    }

    @Override
    public void preloadMeta(Class elementClazz) {
        EntityTTL entityTTL = (EntityTTL) elementClazz.getAnnotation(EntityTTL.class);

        if (entityTTL != null) {
            this.entityTTLs.put(elementClazz, entityTTL);
        }

        for (Field field : elementClazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(EntityId.class)) {
                field.setAccessible(true);
                this.entityIds.put(elementClazz, field);
            }
        }
    }
}
