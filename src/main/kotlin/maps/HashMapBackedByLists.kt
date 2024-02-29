package maps

class HashMapBackedByLists<K, V>(
    initialSize: Int,
    loadFactor: Double,
    bucketFactory: BucketFactory<K, V>,
) : GenericHashMap<K, V>(
    initialSize,
    loadFactor,
    bucketFactory,
)
