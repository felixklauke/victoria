package de.d3adspace.victoria.injector;

import com.couchbase.client.java.repository.Repository;
import com.couchbase.client.java.repository.mapping.EntityConverter;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public interface ConverterInjector {

    void injectConverter(Repository repository, EntityConverter entityConverter);
}
