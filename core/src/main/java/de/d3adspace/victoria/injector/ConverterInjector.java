package de.d3adspace.victoria.injector;

import com.couchbase.client.java.repository.Repository;
import com.couchbase.client.java.repository.mapping.EntityConverter;

/**
 * Basic interface for the injector.
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
public interface ConverterInjector {

    /**
     * Inject the given converter into the given repository.
     *
     * @param repository      The repository.
     * @param entityConverter The converter.
     */
    void injectConverter(Repository repository, EntityConverter entityConverter);
}
