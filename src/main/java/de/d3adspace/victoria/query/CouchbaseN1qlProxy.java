package de.d3adspace.victoria.query;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import de.d3adspace.victoria.analytics.VictoriaAnalytics;
import de.d3adspace.victoria.analytics.VictoriaAnalyticsFactory;

import java.text.MessageFormat;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class CouchbaseN1qlProxy {

    private static final VictoriaAnalytics victoriaAnalytics = VictoriaAnalyticsFactory.createVictoriaAnalytics();
    private static final String GET_ALL_DOCUMENTS_BY_ID_PREFIX = "SELECT * FROM {0} WHERE Meta($bucket).id LIKE $prefix%";
    private static final String GET_ALL_DOCUMENTS_BY_TYPE = "SELECT * FROM {0} WHERE type = $type";

    public static N1qlQueryResult getAllElementsByIdPrefix(Bucket bucket, String prefix) {
        JsonObject params = JsonObject.create().put("bucket", bucket.name()).put("prefix", prefix);
        N1qlQuery query = N1qlQuery.parameterized(MessageFormat.format(GET_ALL_DOCUMENTS_BY_ID_PREFIX, bucket.name()), params);
        return executeQuery(bucket, query);
    }

    public static N1qlQueryResult getAllElementsByType(Bucket bucket, String type) {
        JsonObject params = JsonObject.create().put("bucket", bucket.name()).put("type", type);
        N1qlQuery query = N1qlQuery.parameterized(MessageFormat.format(GET_ALL_DOCUMENTS_BY_TYPE, bucket.name()), params);
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
