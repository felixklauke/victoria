package de.d3adspace.victoria.proxy;

import com.couchbase.client.java.Bucket;

import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class ProxiedDatastructures {

    public static <EntityType> List<EntityType> createList(String databaseDocumentName, Bucket bucket) {
        return new ListProxy<>(databaseDocumentName, bucket);
    }

    public static <EntityType> Map<String, EntityType> createMap(String databaseDocumentName, Bucket bucket) {
        return new MapProxy<>(databaseDocumentName, bucket);
    }

    public static <EntityType> Queue<EntityType> createQueue(String databaseDocumentName, Bucket bucket) {
        return new QueueProxy<>(databaseDocumentName, bucket);
    }
}
