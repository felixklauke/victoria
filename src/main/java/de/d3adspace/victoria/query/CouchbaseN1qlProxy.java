package de.d3adspace.victoria.query;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class CouchbaseN1qlProxy {
    
    private static final String GET_ALL_DOCUMENTS_BY_ID_PREFIX = "SELECT * FROM $bucket WHERE Meta($bucket).id LIKE $prefix%";
    private static final String GET_ALL_DOCUMENTS_BY_TYPE = "SELECT * FROM $bucket WHERE type=$entityType";

    public static N1qlQueryResult getAllElementsByIdPrefix(Bucket bucket, String prefix) {
        JsonObject params = JsonObject.create().put("bucket", bucket.name()).put("prefix", prefix);
        N1qlQuery query = N1qlQuery.parameterized(GET_ALL_DOCUMENTS_BY_ID_PREFIX, params);
        return getAllElementsByQuery(bucket, query);
    }

    public static N1qlQueryResult getAllElementsByType(Bucket bucket, String type) {
        JsonObject params = JsonObject.create().put("bucket", bucket.name()).put("entityType", type);
        N1qlQuery query = N1qlQuery.parameterized(GET_ALL_DOCUMENTS_BY_TYPE, params);
        return getAllElementsByQuery(bucket, query);
    }


    public static N1qlQueryResult getAllElementsByQuery(Bucket bucket, N1qlQuery n1qlQuery) {
        return bucket.query(n1qlQuery);
    }
}
