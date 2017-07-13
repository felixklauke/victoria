package de.d3adspace.victoria.annotation;

import de.d3adspace.victoria.lifecycle.LifecycleWatcher;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EntityWatcher {

    Class<? extends LifecycleWatcher> value();
}
