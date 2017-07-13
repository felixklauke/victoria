package de.d3adspace.victoria.injector;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class ConverterInjectorFactory {

    public static ConverterInjector createConverterInjector() {
        return new SimpleConverterInjector();
    }
}
