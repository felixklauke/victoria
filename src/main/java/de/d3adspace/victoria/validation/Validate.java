package de.d3adspace.victoria.validation;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class Validate {

    public static void checkNotNull(Object object, String message) {
        if (object == null) throw new IllegalArgumentException(message);
    }

    public static void checkState(boolean b, boolean b1, String message) {
        if (b != b1) throw new IllegalStateException(message);
    }
}
