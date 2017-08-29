package de.d3adspace.victoria;

import de.d3adspace.victoria.injector.ConverterInjector;
import de.d3adspace.victoria.injector.ConverterInjectorFactory;
import de.d3adspace.victoria.validation.Validate;

/**
 * Factory for all de.d3adspace.victoria instances.
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
public class VictoriaFactory {

    /**
     * Create a new de.d3adspace.victoria instance based on a custom converter injector.
     *
     * @param converterInjector The injector.
     * @return The de.d3adspace.victoria instance.
     */
    private static Victoria createVictoria(ConverterInjector converterInjector) {
        Validate.checkNotNull(converterInjector, "injector cannot be null");

        return new SimpleVictoria(converterInjector);
    }

    /**
     * Create a new instance of de.d3adspace.victoria.
     *
     * @return The de.d3adspace.victoria instance.
     */
    public static Victoria createVictoria() {
        return createVictoria(ConverterInjectorFactory.createConverterInjector());
    }
}
