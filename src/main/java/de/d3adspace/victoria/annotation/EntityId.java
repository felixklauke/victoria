package de.d3adspace.victoria.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation will indicate that the annotated field will be used as an id of a couchbase document.
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EntityId {
}
