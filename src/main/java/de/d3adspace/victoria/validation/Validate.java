package de.d3adspace.victoria.validation;

/**
 * UtilityClass that will validate data formats and states to ensure a successful API usage.
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
public class Validate {

    /**
     * Prevent instantiation.
     */
    private Validate() {
        throw new AssertionError("Can not instantiate Validate.");
    }

    /**
     * Check if an object is null and throw an exception otherwise.
     *
     * @param object  The object.
     * @param message The message.
     */
    public static void checkNotNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Check if an b is what you expect and throw an exception otherwise.
     *
     * @param b The state to check.
     * @param b1 The expected state.
     * @param message the message.
     */
    public static void checkState(boolean b, boolean b1, String message) {
        if (b != b1) {
            throw new IllegalStateException(message);
        }
    }

    /**
     * Check if a given clazz has a given annotation or throw an exception otherwise.
     *
     * @param sourceClazz The class to check.
     * @param annotationClazz The annotation to check for.
     * @param message the message.
     */
    public static void checkAnnotation(Class sourceClazz, Class annotationClazz, String message) {
        if (!sourceClazz.isAnnotationPresent(annotationClazz)) {
            throw new IllegalArgumentException(message);
        }
    }
}
