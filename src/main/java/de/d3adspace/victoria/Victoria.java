package de.d3adspace.victoria;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.repository.Repository;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public interface Victoria {

    Repository createRepository(Bucket bucket);
}
