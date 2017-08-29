package de.d3adspace.victoria.injector;

/**
 * Factory for all injector instances.
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
public class ConverterInjectorFactory {

    /**
     * Create a new injector instance.
     *
     * @return The converter instance.
     */
    public static ConverterInjector createConverterInjector() {
        return new SimpleConverterInjector();
    }
}
