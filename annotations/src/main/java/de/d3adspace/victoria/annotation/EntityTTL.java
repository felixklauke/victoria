package de.d3adspace.victoria.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation giving a ttl for a couchbase document.
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EntityTTL {

    /**
     * Get the time to live in seconds.
     *
     * @return The time to live.
     */
    int value() default 0;
}
