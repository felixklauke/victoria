package de.d3adspace.victoria.analytics;

import com.couchbase.client.java.query.N1qlQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class SimpleVictoriaAnalytics implements VictoriaAnalytics {

    private final List<N1qlQuery> queryHistory;

    SimpleVictoriaAnalytics() {
        this.queryHistory = new ArrayList<>();
    }

    @Override
    public void recordN1qlQuery(N1qlQuery query) {
        this.queryHistory.add(query);
    }

    @Override
    public List<N1qlQuery> getQueryHistory() {
        return queryHistory;
    }

    @Override
    public int getQueryCount() {
        return this.queryHistory.size();
    }
}
