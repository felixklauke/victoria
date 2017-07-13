package de.d3adspace.victoria;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.repository.CouchbaseRepository;
import com.couchbase.client.java.repository.Repository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.d3adspace.victoria.converter.GSONEntityConverter;
import de.d3adspace.victoria.dao.DAO;
import de.d3adspace.victoria.dao.DAOFactory;
import de.d3adspace.victoria.injector.ConverterInjector;

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
        return createRepository(bucket, DEFAULT_GSON);
    }

    @Override
    public Repository createRepository(Bucket bucket, Gson gson) {
        Repository repository = new CouchbaseRepository(bucket, bucket.environment());
        this.converterInjector.injectConverter(repository, new GSONEntityConverter(gson));
        return repository;
    }

    @Override
    public <ElementType> DAO<ElementType> createDAO(Class<ElementType> elementClazz, Bucket bucket) {
        return DAOFactory.createDAO(elementClazz, createRepository(bucket));
    }
}
