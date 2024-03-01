package maps

typealias BucketFactory<K, V> = () -> CustomMutableMap<K, V>

abstract class GenericHashMap<K, V>(private val bucketFactory: BucketFactory<K, V>) : CustomMutableMap<K, V> {
    protected val initialSize: Int = 32
    protected val loadFactor: Double = 0.75
    protected var buckets: Array<CustomMutableMap<K, V>> = Array(initialSize) { bucketFactory() }

    protected var size: Int = 0
    protected var numBuckets = initialSize

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

    protected fun getIndex(key: K): Int {
        return if (numBuckets and (numBuckets - 1) == 0) {
            key.hashCode() and (numBuckets - 1)
        } else {
            key.hashCode() % numBuckets
        }
    }

    protected open fun resize() {
        if (size >= numBuckets * loadFactor) {
            numBuckets *= 2
            val newBuckets: Array<CustomMutableMap<K, V>> = Array(numBuckets) { bucketFactory() }
            entries.forEach { entry ->
                val index: Int = getIndex(entry.key)
                newBuckets[index][entry.key] = entry.value
            }
            buckets = newBuckets
        }
    }
}
