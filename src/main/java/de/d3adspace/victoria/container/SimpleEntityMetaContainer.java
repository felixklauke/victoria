package de.d3adspace.victoria.container;

import de.d3adspace.victoria.meta.EntityMeta;
import de.d3adspace.victoria.meta.EntityMetaFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Basic implementation for the entity meta container.
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
public class SimpleEntityMetaContainer implements EntityMetaContainer {

    /**
     * Underlying map.
     */
    private final Map<Class, EntityMeta> entityMeta;

    /**
     * Basic constructor.
     */
    SimpleEntityMetaContainer() {
        this.entityMeta = new HashMap<>();
    }

    @Override
    public int getEntityTTL(Object element) {
        return this.entityMeta.get(element.getClass()).getTTL();
    }

    @Override
    public String extractId(Object element) {
        try {
            return String.valueOf(this.entityMeta.get(element.getClass()).getIdField().get(element));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return "ERROR";
    }

    @Override
    public void preloadMeta(Class elementClazz) {
        EntityMeta entityMeta = EntityMetaFactory.createEntityMeta(elementClazz);
        this.entityMeta.put(elementClazz, entityMeta);
    }
}
