package de.d3adspace.victoria.proxy;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.google.gson.Gson;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class MapProxy<ValueType> extends AbstractMap<String, ValueType> {

    private static final Gson GSON = new Gson();
    private final String mapName;
    private final Bucket bucket;

    private Map<String, ValueType> wrappedHandle;

    public MapProxy(String mapName, Bucket bucket) {
        this.mapName = mapName;
        this.bucket = bucket;
        this.wrappedHandle = new HashMap<>();

        JsonDocument jsonDocument = bucket.get(mapName);

        if (jsonDocument == null) {
            jsonDocument = JsonDocument.create(mapName, JsonObject.create());
        }

        wrappedHandle = (Map<String, ValueType>) jsonDocument.content().toMap();
    }

    @Override
    public ValueType put(String key, ValueType value) {
        ValueType result = this.wrappedHandle.put(key, value);

        if (result != null) {
            this.bucket.mapAdd(this.mapName, key, value);
        }

        return result;
    }

    @Override
    public Set<Entry<String, ValueType>> entrySet() {
        return this.wrappedHandle.entrySet();
    }

    @Override
    public ValueType putIfAbsent(String key, ValueType value) {
        ValueType result = this.wrappedHandle.putIfAbsent(key, value);

        if (result != null) {
            this.bucket.mapAdd(this.mapName, key, value);
        }

        return result;
    }

    @Override
    public boolean remove(Object key, Object value) {
        return false;
    }

    @Override
    public ValueType replace(String key, ValueType value) {
        return null;
    }
}
