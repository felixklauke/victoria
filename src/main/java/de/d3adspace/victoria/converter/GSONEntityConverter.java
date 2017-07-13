package de.d3adspace.victoria.converter;

import com.couchbase.client.java.document.EntityDocument;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.repository.mapping.EntityConverter;
import com.google.gson.Gson;

/**
 * You have reached the first goal of victoria: the replacement of couchbase's default entity converter.
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
public class GSONEntityConverter implements EntityConverter<JsonDocument> {

    /**
     * Underlying gson instance.
     */
    private final Gson gson;

    /**
     * Create a new converter instance.
     *
     * @param gson The underlying gson instance.
     */
    public GSONEntityConverter(Gson gson) {
        this.gson = gson;
    }

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
