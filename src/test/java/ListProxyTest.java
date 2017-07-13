import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import de.d3adspace.victoria.proxy.ListProxy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class ListProxyTest {

    public static void main(String[] args) {
        CouchbaseCluster couchbaseCluster = CouchbaseCluster.create("couchdev.umnw.tech");
        Bucket bucket = couchbaseCluster.openBucket("high");

        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("Test", "doof");
        stringMap.put("Rebirth", "Spast");

        TestModel testModel = new TestModel(UUID.randomUUID(), stringMap);

        List<TestModel> list = new ListProxy<>("aaaa:test:list", bucket);
    }
}
