import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.EntityDocument;
import com.couchbase.client.java.repository.Repository;
import de.d3adspace.victoria.Victoria;
import de.d3adspace.victoria.VictoriaFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class RepositoryTest {

    public static void main(String[] args) {
        CouchbaseCluster couchbaseCluster = CouchbaseCluster.create("couchdev.umnw.tech");
        Bucket bucket = couchbaseCluster.openBucket("high");

        Repository repository = VictoriaFactory.createVictoria().createRepository(bucket);

        /*Map<String, String> stringMap = new HashMap<>();
        stringMap.put("Test", "doof");
        stringMap.put("Rebirth", "Spast");

        TestModel testModel = new TestModel(UUID.randomUUID(), stringMap);

        EntityDocument<TestModel> entityDocument = EntityDocument.create("victoria:test", testModel);
        repository.upsert(entityDocument);*/

        System.out.println(repository.get("victoria:test", TestModel.class));
    }
}
