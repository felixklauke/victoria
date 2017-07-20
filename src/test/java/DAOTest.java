import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.EntityDocument;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.query.N1qlQueryRow;
import de.d3adspace.victoria.Victoria;
import de.d3adspace.victoria.VictoriaFactory;
import de.d3adspace.victoria.annotation.*;
import de.d3adspace.victoria.conversion.ConversionInterceptor;
import de.d3adspace.victoria.dao.DAO;
import de.d3adspace.victoria.lifecycle.LifecycleWatcher;
import de.d3adspace.victoria.query.CouchbaseN1qlProxy;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class DAOTest {

    public static void main(String[] args) {
        Victoria victoria = VictoriaFactory.createVictoria();
        CouchbaseCluster couchbaseCluster = CouchbaseCluster.create("couchdev.umnw.tech");
        ConversionInterceptor conversionInterceptor = new TestConversionInterceptor();

        DAO<MinecraftUser> minecraftUserDAO = victoria.createDAO(MinecraftUser.class, couchbaseCluster, conversionInterceptor);

        UUID uniqueId = UUID.randomUUID();
        MinecraftUser minecraftUser = new MinecraftUser(uniqueId, "SasukeKawaii", "testPassword", 100);

        minecraftUserDAO.saveElement(minecraftUser);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        minecraftUserDAO.getAllElements(minecraftUsers -> {
            System.out.println("Got results: " + minecraftUsers);

            System.out.println(CouchbaseN1qlProxy.getVictoriaAnalytics().getQueryHistory()
                    .stream()
                    .map(n1qlQuery1 -> n1qlQuery1.n1ql())
                    .collect(Collectors.toList()));
        });
    }

    @EntityType("minecraftUser")
    @EntityBucket("high")
    @EntityTTL(10)
    @EntityWatcher(MinecraftUserWatcher.class)
    public static class MinecraftUser {

        @EntityId(prefix = "alpha:minecraft:user:")
        private UUID uniqueId;
        private String userName;
        private String password;
        private int coins;

        public MinecraftUser(UUID uniqueId, String userName, String password, int coins) {
            this.uniqueId = uniqueId;
            this.userName = userName;
            this.password = password;
            this.coins = coins;
        }

        public UUID getUniqueId() {
            return uniqueId;
        }

        public String getUserName() {
            return userName;
        }

        public String getPassword() {
            return password;
        }

        public int getCoins() {
            return coins;
        }

        @Override
        public String toString() {
            return "MinecraftUser{" +
                    "uniqueId=" + uniqueId +
                    ", userName='" + userName + '\'' +
                    ", password='" + password + '\'' +
                    ", coins=" + coins +
                    '}';
        }
    }

    public static class MinecraftUserWatcher implements LifecycleWatcher<MinecraftUser> {

        @Override
        public void prePersist(MinecraftUser element, EntityDocument<MinecraftUser> entityDocument) {
            System.out.println("I am going to persist " + element + " as " + entityDocument);
        }

        @Override
        public void postLoad(MinecraftUser element, EntityDocument<MinecraftUser> entityDocument) {
            System.out.println("I loaded " + element + " from " + entityDocument);
        }

        @Override
        public void postLoad(MinecraftUser element, N1qlQueryRow row) {
            System.out.println("I loaded " + element + " from " + row);
        }

        @Override
        public void postPersist(MinecraftUser element, EntityDocument<MinecraftUser> entityDocument) {
            System.out.println("I persisted " + element + " as " + entityDocument);
        }
    }

    public static class TestConversionInterceptor implements ConversionInterceptor {

        @Override
        public void onEntityDeserialization(EntityDocument<Object> entityDocument) {
            System.out.println("Deserializing object from " + entityDocument);
        }

        @Override
        public void onEntitySerialization(JsonDocument jsonDocument, Class entityClass) {
            System.out.println("Serializing an entity: " + jsonDocument + " of " + entityClass);
        }
    }
}
