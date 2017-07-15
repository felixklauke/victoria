package de.d3adspace.victoria.query;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import de.d3adspace.victoria.analytics.VictoriaAnalytics;
import de.d3adspace.victoria.analytics.VictoriaAnalyticsFactory;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class CouchbaseN1qlProxy {

    private static final VictoriaAnalytics victoriaAnalytics = VictoriaAnalyticsFactory.createVictoriaAnalytics();
    private static final String GET_ALL_DOCUMENTS_BY_ID_PREFIX = "SELECT * FROM $bucket WHERE Meta($bucket).id LIKE $prefix%";
    private static final String GET_ALL_DOCUMENTS_BY_TYPE = "SELECT * FROM $bucket WHERE type=$entityType";

    public static N1qlQueryResult getAllElementsByIdPrefix(Bucket bucket, String prefix) {
        JsonObject params = JsonObject.create().put("bucket", bucket.name()).put("prefix", prefix);
        N1qlQuery query = N1qlQuery.parameterized(GET_ALL_DOCUMENTS_BY_ID_PREFIX, params);
        return executeQuery(bucket, query);
    }

    public static N1qlQueryResult getAllElementsByType(Bucket bucket, String type) {
        JsonObject params = JsonObject.create().put("bucket", bucket.name()).put("entityType", type);
        N1qlQuery query = N1qlQuery.parameterized(GET_ALL_DOCUMENTS_BY_TYPE, params);
        return executeQuery(bucket, query);
    }


    public static N1qlQueryResult executeQuery(Bucket bucket, N1qlQuery n1qlQuery) {
        victoriaAnalytics.recordN1qlQuery(n1qlQuery);

        return bucket.query(n1qlQuery);
    }

    public static VictoriaAnalytics getVictoriaAnalytics() {
        return victoriaAnalytics;
    }
}
