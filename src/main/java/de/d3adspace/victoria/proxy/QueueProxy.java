package de.d3adspace.victoria.proxy;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonArrayDocument;
import com.couchbase.client.java.document.json.JsonArray;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Proxied Queue connected with a Couchbase bucket with backed CRUD operations.
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
public class QueueProxy<ElementType> extends AbstractQueue<ElementType> {

    /**
     * Underlying backed queue.
     */
    private final Queue<ElementType> wrappedHandle;

    /**
     * ID if the JSONArrayDocument in the database.
     */
    private final String queueName;

    /**
     * Bucket containing the needed data.
     */
    private final Bucket bucket;

    /**
     * Create a new proxy for a queue.
     *
     * @param queueName The name of the queue in the database.
     * @param bucket    The bucket.
     */
    public QueueProxy(String queueName, Bucket bucket) {
        this.queueName = queueName;
        this.bucket = bucket;

        JsonArrayDocument jsonArrayDocument = bucket.get(queueName, JsonArrayDocument.class);
        if (jsonArrayDocument == null) {
            jsonArrayDocument = JsonArrayDocument.create(queueName, JsonArray.create());
            bucket.upsert(jsonArrayDocument);
        }

        JsonArray jsonArray = jsonArrayDocument.content();
        wrappedHandle = new LinkedBlockingQueue<>((List<ElementType>) jsonArray.toList());
    }

    @Override
    public Iterator<ElementType> iterator() {
        return this.wrappedHandle.iterator();
    }

    @Override
    public int size() {
        return this.wrappedHandle.size();
    }

    @Override
    public boolean offer(ElementType element) {
        boolean success = this.wrappedHandle.offer(element);

        if (success) {
            this.bucket.mutateIn(this.queueName).arrayPrepend("", element, false).execute();
        }

        return success;
    }

    @Override
    public ElementType poll() {
        this.bucket.mutateIn(this.queueName).remove("[-1]").execute();
        return this.wrappedHandle.poll();
    }

    @Override
    public ElementType peek() {
        return this.wrappedHandle.peek();
    }
}
