package de.d3adspace.victoria.conversion;

import com.couchbase.client.java.document.EntityDocument;
import com.couchbase.client.java.document.JsonDocument;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public interface ConversionInterceptor {

    void onEntityDeserialization(EntityDocument<Object> entityDocument);

    void onEntitySerialization(JsonDocument jsonDocument, Class entityClass);
}
