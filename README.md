# Victoria

Experimental replacement for couchbase's default entity converter embedded repository implementation using GSON instead
couchbase's dumb reflection based property meta data implementation. Furthermore vitoria provides
a highly advanced database access object layer which you can use to simplify couchbase's java driver 
API to a human understandable layer with a lot of useful features like:
- Custom entity conversion
- Entity Lifecycle processing
- Managing arbitrary database entities via annotations
- Conversion interception to monitor entity serialization
- Proxied instances of javas main default data structures that will synchronize themselves with a document in your database:
    - Queue
    - List
    - Map
    - (Iterator)
- Simple analytics to enhance your database analysis
- DAOs with a bunch of features to manage your data fast and easy

# Basic Usage

### Plain old repository
```java
Victoria victoria = VictoriaFactory.createVictoria();
        
// Create a repository by a couchbase bucket
victoria.createRepository(bucket);
        
// You want to use your own gson instance?
victoria.createRepository(bucket, gson);
        
// Intercept entity conversion
victoria.createRepository(bucket, gson, conversionInterceptor);
```

### DAO
```java
Victoria victoria = VictoriaFactory.createVictoria();
        
// Create a basic dao
victoria.createDAO(elementClazz, bucket);
        
// Create a basic dao but provide the bucket via cluster and entity annotation
victoria.createDAO(elementClazz, couchbaseCluster);
        
// Create a basic dao but provide your own gson instance
victoria.createDAO(elementClazz, bucket, gson);
        
// Create a basic dao and intercept entity conversion
victoria.createDAO(elementClazz, bucket, gson, conversionInterceptor);
```

# Annotations for DAO Entities

### EntityBucket
```java 
@EntityBucket( "bucketName" ) 
public class PlayerModel {
```

You can provide the bucket of an entity via the `@EntityBucket` annotation.

### EntityType
```java 
@EntityType( "player" ) 
public class PlayerModel {
```

When you work with an indexed bucket you should ensure youre using an index on the `type` field. 
The given type will be serialized in the JSON Version of your entity but won't be visible in your entity.

### EntityId
```java 
@EntityId( prefix = "player::" )
private final UUID uniqueId;
```

One field of your entity should provide the id of the future document in the database. It is recommended
that you prepend a prefix to mark the source application of your entity.

### EntityTTL
```java 
@EntityTTL( 20 )
public class PlayerModel {
```

You can give your database document a time to live in seconds via this annotation. Remember that this
will take the ttl in seconds when your TTL is under 30 days. You will have to use an unix timestamp if
your ttl should be higher than 30 days. 

### EntityWatcher
```java 
@EntityWatcher( BankAccountWatcher.class )
public class BankAccount {
```

You can provide the class of a lifecycle watcher. Take a look at lifecycle processing.

# Lifecycle processing
You can implement the LifecycleWatcher<ElementType> to watch your entities lifecycle. Remember there
are two ways and entity can be loaded: Via ID earch or via N1ql. Take care of handle both. 
```java
public interface LifecycleWatcher<ElementType> {

    /**
     * Called before the entityDocument will be persisted in a bucket.
     */
    void prePersist(ElementType element, EntityDocument<ElementType> entityDocument);

    /**
     * Called after an element was loaded by its primary ID.
     */
    void postLoad(ElementType element, EntityDocument<ElementType> entityDocument);

    /**
     * Called after an element was loaded with a n1ql query.
     */
    void postLoad(ElementType element, N1qlQueryRow row);

    /**
     * Called after an entity was persisted in a bucket.
     */
    void postPersist(ElementType element, EntityDocument<ElementType> entityDocument);
}
```

# Conversion Interception
If you want to change the behavior of the EntityConverter without writing your own
converter use a conversion interceptor that will be called whenever an entity is 
serialized or deserialized: 
```java
public interface ConversionInterceptor {

    void onEntityDeserialization(EntityDocument<Object> entityDocument);

    void onEntitySerialization(JsonDocument jsonDocument, Class entityClass);
}
```

# Proxied Datastructures
You want to manage your data in a java data structure like a list or a queue but you dont want
to handle the persistence of the data on your own? Use proxies: 

### List
```java
List<Model> list = ProxiedDatastructures.createList(databaseDocumentName, bucket);
```

### Map
Sadly we only can support Maps with a String as key as couchbase sucks.
```java
Map<String, Model> map = ProxiedDatastructures.createMap(databaseDocumentName, bucket);
```

### Queue
```java
Queue<TestModel> queue = ProxiedDatastructures.createQueue("players", bucket);
```

CAUTION: We currently dont hold a lock on proxied documents in a database, so dont modify a document from other sources.