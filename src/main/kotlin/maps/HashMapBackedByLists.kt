package maps

class HashMapBackedByLists<K, V>(bucketFactory: BucketFactory<K, V>) : GenericHashMap<K, V>(bucketFactory)
