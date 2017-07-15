package de.d3adspace.victoria.analytics;

import com.couchbase.client.java.query.N1qlQuery;

import java.util.List;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public interface VictoriaAnalytics {

    void recordN1qlQuery(N1qlQuery query);

    List<N1qlQuery> getQueryHistory();

    int getQueryCount();
}
