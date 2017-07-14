# Victoria

Experimental replacement for couchbase's default entity converter embedded repository implementation using GSON instead
couchbase's dumb reflection based property meta data implementation.

# Example

_Model:_
```java
import de.d3adspace.victoria.annotation.EntityId;
import de.d3adspace.victoria.annotation.EntityTTL;
import de.d3adspace.victoria.annotation.EntityWatcher;

import java.util.Map;
import java.util.UUID;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
@EntityTTL(20)
@EntityWatcher(TestModelWatcher.class)
public class TestModel {

    @EntityId
    private final UUID uniqueId;
    private final Map<String, String> metaDataContainer;

    public TestModel(UUID uniqueId, Map<String, String> metaDataContainer) {
        this.uniqueId = uniqueId;
        this.metaDataContainer = metaDataContainer;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public Map<String, String> getMetaDataContainer() {
        return metaDataContainer;
    }

    @Override
    public String toString() {
        return "TestModel{" +
                "uniqueId=" + uniqueId +
                ", metaDataContainer=" + metaDataContainer +
                '}';
    }
}
```

