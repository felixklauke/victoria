package de.d3adspace.victoria.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate an arbitrary entity class with this annotation to give the name of a couchbase bucket de.d3adspace.victoria will
 * persist the entities of this class to.
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EntityBucket {

    /**
     * Get the name of the couchbase bucket.
     *
     * @return The name of the bucket.
     */
    String value() default "high";
}
