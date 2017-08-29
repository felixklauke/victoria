package de.d3adspace.victoria;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.repository.Repository;
import com.google.gson.Gson;
import de.d3adspace.victoria.conversion.ConversionInterceptor;
import de.d3adspace.victoria.converter.GSONEntityConverter;
import de.d3adspace.victoria.dao.DAO;
import de.d3adspace.victoria.injector.ConverterInjector;
import de.d3adspace.victoria.proxy.ListProxy;
import de.d3adspace.victoria.proxy.MapProxy;
import de.d3adspace.victoria.proxy.QueueProxy;
import de.d3adspace.victoria.query.CouchbaseN1qlProxy;

/**
 * Basic de.d3adspace.victoria interface containing all main functions.
 * <p>
 * We want to make it easy to persist arbitrary interfaces in a couchbase backend.
 * <p>
 * Victoria's main functionality should be to make the database access of couchbase's more clean and easier for the
 * developer mainly by enhancing its default implementations of entity repositories by using gson and furthermore
 * providing interfaces to create custom database access objects aka DAO {@link DAO}. You can
 * handle entity persistence with a model class and some de.d3adspace.victoria annotations:
 * <p>
 * <pre>
 *     <code>
 *          {@literal @}{@link de.d3adspace.victoria.annotation.EntityTTL}(20)
 *          {@literal @}{@link de.d3adspace.victoria.annotation.EntityWatcher}(Watcher.class)
 *          {@literal @}{@link de.d3adspace.victoria.annotation.EntityBucket}("high")
 *          public class Example {
 *              {@literal @}{@link de.d3adspace.victoria.annotation.EntityId}(prefix = "test:")
 *              private UUID uniqueId;
 *          }
 *     </code>
 * </pre>
 * <p>
 * It is possible to listen for entity conversion using an {@link ConversionInterceptor}
 * <p>
 * All Daos will record their stats in a VictoriaAnalytics instance provided by
 * {@link CouchbaseN1qlProxy#getVictoriaAnalytics()}
 * <p>
 * Remember: DAOs will also use an internal instance of a gson modified couchbase repository.
 * <p>
 * The secondary goal is to hide the couchbase database access layer behind the proxy pattern and take default java
 * data structures (List, Set, Map, Queue, Stack) and add a backend handler that will persist them in a couchbase
 * backend:
 * <p>
 * <ul>
 * <li>{@link ListProxy} - Proxy to persist List's</li>
 * <li>{@link QueueProxy} - Proxy to persist Queue's</li>
 * <li>{@link MapProxy} - Proxy to persist Maps</li>
 * </ul>
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
public interface Victoria {

    /**
     * Create a new repository using a default gson instance.
     *
     * @param bucket The bucket.
     * @return The repo.
     */
    Repository createRepository(Bucket bucket);

    /**
     * Create a basic couchbase repository like described in <a href="http://docs.couchbase.com/sdk-api/couchbase-java-client-2.2.8/com/couchbase/client/java/repository/package-summary.html">Couchbase Repository</a>
     * but with a modified entity converter that uses GSON serialization instead of plain old java reflections based
     * property container data for entity serialization. You can read more about the default implementation at
     * <a href="https://github.com/couchbase/couchbase-java-client/tree/master/src/main/java/com/couchbase/client/java/repository"></a>.
     * <p>
     * To achieve that de.d3adspace.victoria has to create a custom EntityConverter {@link com.couchbase.client.java.repository.mapping.EntityConverter}
     * that uses GSON. This implementation is located at {@link GSONEntityConverter}.
     * <p>
     * As Couchbase wraps around their own async bucket impl to achieve synchronous database access our
     * {@link ConverterInjector} has to take reflection access on the async repository
     * implementation located at {@link com.couchbase.client.java.repository.AsyncRepository} and replace rge default
     * entity converter {@link com.couchbase.client.java.repository.mapping.DefaultEntityConverter}.
     *
     * @param bucket The bucket you want to create a repository on.
     * @param gson   The gson instance of the converter.
     * @return The repository.
     */
    Repository createRepository(Bucket bucket, Gson gson);

    /**
     * Create a repository with all its necessary data.
     *
     * @param bucket                The bucket.
     * @param gson                  The gson instance.
     * @param conversionInterceptor The conversion interceptor.
     * @return The repository.
     */
    Repository createRepository(Bucket bucket, Gson gson, ConversionInterceptor conversionInterceptor);

    /**
     * Create a database access object that pulls out the basic crud operations out off a repository.
     *
     * @param elementClazz  The class of the entity you're persisting.
     * @param bucket        The bucket to create a repository from.
     * @param <ElementType> The type of the entity you're persisting.
     * @return The DAO.
     */
    <ElementType> DAO<ElementType> createDAO(Class<ElementType> elementClazz, Bucket bucket);

    /**
     * Create a dao by its base class, the bucket and an interceptor using a default gson instance.
     *
     * @param elementClazz          The class of the element you're persisting.
     * @param bucket                The bucket the entities are persisted in.
     * @param conversionInterceptor The conversion interceptor.
     * @param <ElementType>         The type of the entity you're persisting.
     * @return The DAO.
     */
    <ElementType> DAO<ElementType> createDAO(Class<ElementType> elementClazz, Bucket bucket, ConversionInterceptor conversionInterceptor);

    /**
     * Create a dao by all its necessary data.
     *
     * @param elementClazz  The class of the element you're persisting.
     * @param bucket        The bucket the entities are persisted in.
     * @param gson          The gson instance.
     * @param <ElementType> The type of the entity you're persisting.
     * @return The DAO.
     */
    <ElementType> DAO<ElementType> createDAO(Class<ElementType> elementClazz, Bucket bucket, Gson gson);

    /**
     * Create a DAO by all its necessary data.
     *
     * @param elementClazz          The class of the element you're persisting.
     * @param bucket                The bucket the entities are persisted in.
     * @param gson                  The gson instance.
     * @param conversionInterceptor The conversion interceptor.
     * @param <ElementType>         The type of the entity you're persisting.
     * @return The DAO.
     */
    <ElementType> DAO<ElementType> createDAO(Class<ElementType> elementClazz, Bucket bucket, Gson gson, ConversionInterceptor conversionInterceptor);

    /**
     * Create a database access object based on a couchbase cluster.
     *
     * @param elementClazz     The class of the entity you're persisting.
     * @param couchbaseCluster The cluster whose bucket you want to use.
     * @param <ElementType>    The type of the entity you're persisting.
     * @return The DAO.
     */
    <ElementType> DAO<ElementType> createDAO(Class<ElementType> elementClazz, CouchbaseCluster couchbaseCluster);

    /**
     * Create a DAO by all its data except of the gson instance, a default instance will be used instead.
     *
     * @param elementClazz          The class of the entity you're persisting.
     * @param couchbaseCluster      The cluster whose bucket you want to use.
     * @param conversionInterceptor The conversion interceptor.
     * @param <ElementType>         The type of the entity you're persisting.
     * @return The DAO.
     */
    <ElementType> DAO<ElementType> createDAO(Class<ElementType> elementClazz, CouchbaseCluster couchbaseCluster, ConversionInterceptor conversionInterceptor);

    /**
     * Create a database access object based on a couchbase cluster.
     *
     * @param elementClazz     The class of the entity you're persisting.
     * @param couchbaseCluster The cluster whose bucket you want to use.
     * @param <ElementType>    The type of the entity you're persisting.
     * @return The DAO.
     */
    <ElementType> DAO<ElementType> createDAO(Class<ElementType> elementClazz, CouchbaseCluster couchbaseCluster, Gson gson);

    /**
     * Create a DAO by all its data but a couchbase cluster the bucket will be taken from.
     *
     * @param elementClazz          The class of the entity you're persisting.
     * @param couchbaseCluster      The cluster whose bucket you want to use.
     * @param gson                  The gson instance.
     * @param conversionInterceptor The conversion interceptor.
     * @param <ElementType>         The type of the entity you're persisting.
     * @return The DAO.
     */
    <ElementType> DAO<ElementType> createDAO(Class<ElementType> elementClazz, CouchbaseCluster couchbaseCluster, Gson gson, ConversionInterceptor conversionInterceptor);
}
