package de.d3adspace.victoria.exception;

/**
 * Mother of all internal exceptions.
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
public class VictoriaException extends RuntimeException {

    /**
     * Create a new exception.
     *
     * @param message The message.
     */
    public VictoriaException(String message) {
        super(message);
    }
}
