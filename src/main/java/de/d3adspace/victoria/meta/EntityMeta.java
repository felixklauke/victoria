package de.d3adspace.victoria.meta;

import java.lang.reflect.Field;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public interface EntityMeta {

    int getTTL();

    Field getIdField();
}
