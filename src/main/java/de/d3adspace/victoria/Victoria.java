package de.d3adspace.victoria;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.repository.Repository;

/**
 * Basic victoria interface containing all main functions.
 *
 * We want to make it easy to persist arbitrary interfaces in a couchbase backend.
 *
 * Victoria's main functionality should be to make the database access of couchbase's more clean and easier for the
 * developer mainly by enhancing its default implementations of entity repositories by using gson and furthermore
 * providing interfaces to create custom database access objects aka DAO {@link de.d3adspace.victoria.dao.DAO}.
 *
 * The secondary goal is to hide the couchbase database access layer behind the proxy pattern and take default java
 * data structures (List, Set, Map, Queue, Stack) and add a backend handler that will persist them in a couchbase
 * backend:
 *
 * <ul>
 *     <li>{@link de.d3adspace.victoria.proxy.ListProxy} - Proxy to persist List's</li>
 *     <li>{@link de.d3adspace.victoria.proxy.QueueProxy} - Proxy to persist Queue's</li>
 * </ul>
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
public interface Victoria {

    /**
     * Create a basic couchbase repository like described in <a href="http://docs.couchbase.com/sdk-api/couchbase-java-client-2.2.8/com/couchbase/client/java/repository/package-summary.html">Couchbase Repository</a>
     * but with a modified entity converter that uses GSON serialization instead of plain old java reflections based
     * property meta data for entity serialization. You can read more about the default implementation at
     * <a href="https://github.com/couchbase/couchbase-java-client/tree/master/src/main/java/com/couchbase/client/java/repository"></a>.
     * <p>
     * To achieve that victoria has to create a custom EntityConverter {@link com.couchbase.client.java.repository.mapping.EntityConverter}
     * that uses GSON. This implementation is located at {@link de.d3adspace.victoria.converter.GSONEntityConverter}.
     * <p>
     * As Couchbase wraps around their own async bucket impl to achieve synchronous database access our
     * {@link de.d3adspace.victoria.injector.ConverterInjector} has to take reflection access on the async repository
     * implementation located at {@link com.couchbase.client.java.repository.AsyncRepository} and replace rge default
     * entity converter {@link com.couchbase.client.java.repository.mapping.DefaultEntityConverter}.
     *
     * @param bucket The bucket you want to create a repository on.
     * @return The repository.
     */
    Repository createRepository(Bucket bucket);
}
