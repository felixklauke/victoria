package de.d3adspace.victoria;

import de.d3adspace.victoria.injector.ConverterInjector;
import de.d3adspace.victoria.injector.ConverterInjectorFactory;

/**
 * Factory for all victoria instances.
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
public class VictoriaFactory {

    /**
     * Create a new victoria instance based on a custom converter injector.
     *
     * @param converterInjector The injector.
     * @return The victoria instance.
     */
    private static Victoria createVictoria(ConverterInjector converterInjector) {
        return new SimpleVictoria(converterInjector);
    }

    /**
     * Create a new instance of victoria.
     *
     * @return The victoria instance.
     */
    public static Victoria createVictoria() {
        return createVictoria(ConverterInjectorFactory.createConverterInjector());
    }
}
