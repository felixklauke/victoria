package de.d3adspace.victoria;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.repository.CouchbaseRepository;
import com.couchbase.client.java.repository.Repository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.d3adspace.victoria.annotation.EntityBucket;
import de.d3adspace.victoria.converter.GSONEntityConverter;
import de.d3adspace.victoria.dao.DAO;
import de.d3adspace.victoria.dao.DAOFactory;
import de.d3adspace.victoria.injector.ConverterInjector;
import de.d3adspace.victoria.validation.Validate;

/**
 * Basic victoria implementation.
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
public class SimpleVictoria implements Victoria {

    /**
     * Default Gson instance to use if you dont want to provide your own gson instance.
     */
    private static final Gson DEFAULT_GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * The injector that will replace couchbase's default entity converter.
     */
    private final ConverterInjector converterInjector;

    /**
     * Create a new victoria instance.
     *
     * @param converterInjector The injector.
     */
    SimpleVictoria(ConverterInjector converterInjector) {
        this.converterInjector = converterInjector;
    }

    @Override
    public Repository createRepository(Bucket bucket) {
        Validate.checkNotNull(bucket, "bucket can not be null.");

        return createRepository(bucket, DEFAULT_GSON);
    }

    @Override
    public Repository createRepository(Bucket bucket, Gson gson) {
        Validate.checkNotNull(bucket, "bucket can not be null.");
        Validate.checkNotNull(gson, "gson can not be null.");

        Repository repository = new CouchbaseRepository(bucket, bucket.environment());
        this.converterInjector.injectConverter(repository, new GSONEntityConverter(gson));
        return repository;
    }

    @Override
    public <ElementType> DAO<ElementType> createDAO(Class<ElementType> elementClazz, Bucket bucket) {
        return createDAO(elementClazz, bucket, DEFAULT_GSON);
    }

    @Override
    public <ElementType> DAO<ElementType> createDAO(Class<ElementType> elementClazz, Bucket bucket, Gson gson) {
        Validate.checkNotNull(elementClazz, "Element clazz can not be null.");
        Validate.checkNotNull(bucket, "Bucket cluster can not be null.");
        Validate.checkNotNull(gson, "gson can not be null.");

        return DAOFactory.createDAO(elementClazz, createRepository(bucket), bucket, gson);
    }

    @Override
    public <ElementType> DAO<ElementType> createDAO(Class<ElementType> elementClazz, CouchbaseCluster couchbaseCluster) {
        Validate.checkNotNull(elementClazz, "Element clazz can not be null.");
        Validate.checkNotNull(couchbaseCluster, "Couchbase cluster can not be null.");
        Validate.checkAnnotation(elementClazz, EntityBucket.class, elementClazz + " needs @EntityBucket annotation.");

        return createDAO(elementClazz, couchbaseCluster, DEFAULT_GSON);
    }

    @Override
    public <ElementType> DAO<ElementType> createDAO(Class<ElementType> elementClazz, CouchbaseCluster couchbaseCluster, Gson gson) {
        Validate.checkNotNull(elementClazz, "Element clazz can not be null.");
        Validate.checkNotNull(couchbaseCluster, "Couchbase cluster can not be null.");
        Validate.checkNotNull(gson, "gson cluster can not be null.");
        Validate.checkAnnotation(elementClazz, EntityBucket.class, elementClazz + " needs @EntityBucket annotation.");

        EntityBucket entityBucket = elementClazz.getAnnotation(EntityBucket.class);
        return createDAO(elementClazz, couchbaseCluster.openBucket(entityBucket.value()), gson);
    }
}
