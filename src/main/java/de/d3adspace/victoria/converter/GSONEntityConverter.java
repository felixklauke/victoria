package de.d3adspace.victoria.converter;

import com.couchbase.client.java.document.EntityDocument;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.repository.mapping.EntityConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class GSONEntityConverter implements EntityConverter<JsonDocument> {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public JsonDocument fromEntity(EntityDocument<Object> entityDocument) {
        Object documentContent = entityDocument.content();

        JsonObject jsonObject = JsonObject.fromJson(this.gson.toJson(documentContent));

        return JsonDocument.create(entityDocument.id(), entityDocument.expiry(), jsonObject, entityDocument.cas());
    }

    @Override
    public <T> EntityDocument<T> toEntity(JsonDocument jsonDocument, Class<T> entityClass) {
        T documentContent = this.gson.fromJson(jsonDocument.content().toString(), entityClass);

        return EntityDocument.create(jsonDocument.id(), jsonDocument.expiry(), documentContent, jsonDocument.cas());
    }
}
