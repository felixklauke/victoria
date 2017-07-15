package de.d3adspace.victoria.container;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class EntityMetaContainerFactory {

    public static EntityMetaContainer createEntityMetaContainer() {
        return new SimpleEntityMetaContainer();
    }
}
