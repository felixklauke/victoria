package de.d3adspace.victoria.injector;

import com.couchbase.client.java.repository.AsyncRepository;
import com.couchbase.client.java.repository.CouchbaseAsyncRepository;
import com.couchbase.client.java.repository.CouchbaseRepository;
import com.couchbase.client.java.repository.Repository;
import com.couchbase.client.java.repository.mapping.EntityConverter;

import java.lang.reflect.Field;

/**
 * Injector that will replace couchbases's default entity converter.
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
public class SimpleConverterInjector implements ConverterInjector {

    /**
     * Field containing the underlying async repo of a repo.
     */
    private Field ASYNC_REPO_FIELD;

    /**
     * Field containing the entity converter of an async repo.
     */
    private Field CONVERTER_FIELD;

    /**
     * Create a new injector.
     */
    SimpleConverterInjector() {
        try {
            ASYNC_REPO_FIELD = CouchbaseRepository.class.getDeclaredField("asyncRepository");
            ASYNC_REPO_FIELD.setAccessible(true);

            CONVERTER_FIELD = CouchbaseAsyncRepository.class.getDeclaredField("converter");
            CONVERTER_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void injectConverter(Repository repository, EntityConverter entityConverter) {
        try {
            AsyncRepository asyncRepository = (AsyncRepository) ASYNC_REPO_FIELD.get(repository);
            CONVERTER_FIELD.set(asyncRepository, entityConverter);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
