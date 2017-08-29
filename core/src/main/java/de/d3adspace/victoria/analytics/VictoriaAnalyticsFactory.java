package de.d3adspace.victoria.analytics;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class VictoriaAnalyticsFactory {

    public static VictoriaAnalytics createVictoriaAnalytics() {
        return new SimpleVictoriaAnalytics();
    }
}
