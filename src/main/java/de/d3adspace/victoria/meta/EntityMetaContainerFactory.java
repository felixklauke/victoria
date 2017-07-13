package de.d3adspace.victoria.meta;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class EntityMetaContainerFactory {

    public static EntityMetaContainer createEntityMetaContainer() {
        return new SimpleEntityMetaContainer();
    }
}
