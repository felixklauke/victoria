package de.d3adspace.victoria;

import de.d3adspace.victoria.injector.ConverterInjector;
import de.d3adspace.victoria.injector.ConverterInjectorFactory;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class VictoriaFactory {

    private static Victoria createVictoria(ConverterInjector converterInjector) {
        return new SimpleVictoria(converterInjector);
    }

    public static Victoria createVictoria() {
        return createVictoria(ConverterInjectorFactory.createConverterInjector());
    }
}
