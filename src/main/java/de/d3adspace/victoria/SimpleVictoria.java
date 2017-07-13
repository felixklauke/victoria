package de.d3adspace.victoria;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.repository.CouchbaseRepository;
import com.couchbase.client.java.repository.Repository;
import de.d3adspace.victoria.converter.GSONEntityConverter;
import de.d3adspace.victoria.injector.ConverterInjector;

/**
 * Basic victoria implementation.
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
public class SimpleVictoria implements Victoria {

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
        Repository repository = new CouchbaseRepository(bucket, bucket.environment());
        this.converterInjector.injectConverter(repository, new GSONEntityConverter());
        return repository;
    }
}
