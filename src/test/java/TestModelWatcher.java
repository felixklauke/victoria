import com.couchbase.client.java.document.EntityDocument;
import com.couchbase.client.java.query.N1qlQueryRow;
import de.d3adspace.victoria.lifecycle.LifecycleWatcher;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class TestModelWatcher implements LifecycleWatcher<TestModel> {

    @Override
    public void prePersist(TestModel element, EntityDocument<TestModel> entityDocument) {
        System.out.println("Persisting " + element + " as " + entityDocument);
    }

    @Override
    public void postLoad(TestModel element, EntityDocument<TestModel> entityDocument) {
        System.out.println("Loaded " + element + " as " + entityDocument);
    }

    @Override
    public void postLoad(TestModel element, N1qlQueryRow row) {
        System.out.println("Loaded " + element + " as " + row);
    }

    @Override
    public void postPersist(TestModel element, EntityDocument<TestModel> entityDocument) {
        System.out.println("Persisted " + element + " as " + entityDocument);
    }
}
