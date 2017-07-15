package de.d3adspace.victoria.skeleton;

import com.couchbase.client.java.document.EntityDocument;
import com.couchbase.client.java.document.JsonDocument;
import de.d3adspace.victoria.conversion.ConversionInterceptor;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class SkeletonConversionInterceptor implements ConversionInterceptor {

    @Override
    public void onEntityDeserialization(EntityDocument<Object> entityDocument) {

    }

    @Override
    public void onEntitySerialization(JsonDocument jsonDocument, Class entityClass) {

    }
}
