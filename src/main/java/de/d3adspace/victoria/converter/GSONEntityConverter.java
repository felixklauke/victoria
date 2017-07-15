package de.d3adspace.victoria.converter;

import com.couchbase.client.java.document.EntityDocument;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.repository.mapping.EntityConverter;
import com.google.gson.Gson;
import de.d3adspace.victoria.annotation.EntityType;
import de.d3adspace.victoria.exception.VictoriaException;

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

        if (documentContent == null) {
            throw new VictoriaException("Could not retrieve valid document content.");
        }

        JsonObject jsonObject = JsonObject.fromJson(this.gson.toJson(documentContent));

        EntityType entityType = documentContent.getClass().getAnnotation(EntityType.class);
        if (entityType != null) {
            jsonObject.put("type", entityType.value());
        }


        return JsonDocument.create(entityDocument.id(), entityDocument.expiry(), jsonObject, entityDocument.cas());
    }

    @Override
    public <T> EntityDocument<T> toEntity(JsonDocument jsonDocument, Class<T> entityClass) {
        T documentContent = this.gson.fromJson(jsonDocument.content().toString(), entityClass);

        if (documentContent == null) {
            throw new VictoriaException("Could not create entity document from json document.");
        }

        return EntityDocument.create(jsonDocument.id(), jsonDocument.expiry(), documentContent, jsonDocument.cas());
    }
}
