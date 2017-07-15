import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.repository.Repository;
import de.d3adspace.victoria.VictoriaFactory;
import de.d3adspace.victoria.dao.DAO;

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

        DAO<TestModel> testModelDAO = VictoriaFactory.createVictoria().createDAO(TestModel.class, bucket);
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("Test", "doof");
        stringMap.put("Rebirth", "Spast1");

        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
        TestModel testModel = new TestModel(uuid, stringMap);

        testModelDAO.saveElement(testModel);

        System.out.println(testModelDAO.getElement(uuid.toString()));

        //System.out.println(bucket.get("9627c87d-ca91-4bb6-a953-d00ac9043913"));
    }
}
