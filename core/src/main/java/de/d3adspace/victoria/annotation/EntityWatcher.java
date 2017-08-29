package de.d3adspace.victoria.annotation;

import de.d3adspace.victoria.LifecycleWatcher;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Give the class of a lifecycle watcher that will be notified on all persisting or loading actions an a data type.
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EntityWatcher {

    /**
     * Get the class of a watcher.
     *
     * @return The class of the watcher.
     */
    Class<? extends LifecycleWatcher> value();
}
