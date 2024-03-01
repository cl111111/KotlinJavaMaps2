package maps

class ListBasedMap<K, V>() : CustomMutableMap<K, V> {
    override val entries: CustomLinkedList<Entry<K, V>>
        get() = contents
    override val keys: Iterable<K>
        get() = entries.map(Entry<K, V>::key)
    override val values: Iterable<V>
        get() = entries.map(Entry<K, V>::value)

    override fun contains(key: K): Boolean {
        return get(key) != null
    }

    override fun put(entry: Entry<K, V>): V? {
        return put(entry.key, entry.value)
    }

    override fun set(key: K, value: V): V? {
        return put(key, value)
    }

    private val contents: CustomLinkedList<Entry<K, V>> = CustomLinkedList()

    override fun get(key: K): V? {
        return entries.firstOrNull { it.key == key }?.value
    }

    override fun put(key: K, value: V): V? {
        val prev: V? = get(key)
        remove(key)
        contents.add(Entry(key, value))
        return prev
    }

    override fun remove(key: K): V? {
        val matchKey: (Entry<K, V>) -> Boolean = { e: Entry<K, V> -> e.key == key }
        return contents.remove(matchKey)?.value
    }
}
