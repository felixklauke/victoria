import de.d3adspace.victoria.annotation.*;

import java.util.Map;
import java.util.UUID;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
@EntityTTL(20)
@EntityBucket("high")
@EntityType("testModel")
@EntityWatcher(TestModelWatcher.class)
public class TestModel {

    @EntityId(prefix = "test:")
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
