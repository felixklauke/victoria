package de.d3adspace.victoria.injector;

import com.couchbase.client.java.repository.AsyncRepository;
import com.couchbase.client.java.repository.CouchbaseAsyncRepository;
import com.couchbase.client.java.repository.CouchbaseRepository;
import com.couchbase.client.java.repository.Repository;
import com.couchbase.client.java.repository.mapping.EntityConverter;
import de.d3adspace.victoria.converter.GSONEntityConverter;

import java.lang.reflect.Field;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class SimpleConverterInjector implements ConverterInjector {

    private Field ASYNC_REPO_FIELD;
    private Field CONVERTER_FIELD;

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
            CONVERTER_FIELD.set(asyncRepository, new GSONEntityConverter());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
