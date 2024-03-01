package maps

typealias BucketFactory<K, V> = () -> CustomMutableMap<K, V>

abstract class GenericHashMap<K, V>(
    initialSize: Int,
    private val loadFactor: Double,
    private val bucketFactory: BucketFactory<K, V>,
) : CustomMutableMap<K, V> {

    private var buckets: Array<CustomMutableMap<K, V>> = Array(initialSize) { bucketFactory() }

    private var size: Int = 0

    override val entries: Iterable<Entry<K, V>>
        get() = buckets.flatMap { it.entries }

    override val keys: Iterable<K>
        get() = entries.map { it.key }

    override val values: Iterable<V>
        get() = entries.map { it.value }

    override fun contains(key: K): Boolean {
        val index = getIndex(key)
        return buckets[index].contains(key)
    }

    override fun remove(key: K): V? {
        val index: Int = getIndex(key)
        val removedValue: V? = buckets[index].remove(key)
        if (removedValue != null) {
            size--
        }
        return removedValue
    }

    override fun put(entry: Entry<K, V>): V? {
        return put(entry.key, entry.value)
    }

    override fun put(key: K, value: V): V? {
        resize()
        val index: Int = getIndex(key)
        val oldValue: V? = buckets[index][key]
        buckets[index][key] = value
        if (oldValue == null) {
            size++
        }
        return oldValue
    }

    override fun set(key: K, value: V): V? {
        return put(key, value)
    }

    override fun get(key: K): V? {
        val index: Int = getIndex(key)
        return buckets[index][key]
    }

    private fun getIndex(key: K): Int {
        if (buckets.size and (buckets.size - 1) == 0) {
            return key.hashCode() and (buckets.size - 1)
        } else {
            return key.hashCode() % buckets.size
        }
    }

    private fun resize() {
        if (size >= buckets.size * loadFactor) {
            val newSize: Int = size * 2
            val newBuckets: Array<CustomMutableMap<K, V>> = Array(newSize) { bucketFactory() }
            entries.forEach { entry ->
                val index: Int = getIndex(entry.key)
                newBuckets[index][entry.key] = entry.value
            }
        }
    }
}
