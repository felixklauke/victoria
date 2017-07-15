package de.d3adspace.victoria.container;

/**
 * Factory to create new meta containers.
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
public class EntityMetaContainerFactory {

    /**
     * Create a new container.
     *
     * @return The container.
     */
    public static EntityMetaContainer createEntityMetaContainer() {
        return new SimpleEntityMetaContainer();
    }
}
