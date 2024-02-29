package maps

class ListBasedMap<K, V>() : CustomMutableMap<K, V> {
    override val entries: CustomLinkedList<Entry<K, V>>
        get() = contents

    private val contents: CustomLinkedList<Entry<K, V>> = CustomLinkedList<Entry<K, V>>()

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
