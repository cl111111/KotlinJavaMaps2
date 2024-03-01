package maps

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

abstract class LockedMap<K, V>(private val targetMap: CustomMutableMap<K, V>) : CustomMutableMap<K, V> {
    private val lock = ReentrantLock()

    override val entries: Iterable<Entry<K, V>>
        get() = lock.withLock { targetMap.entries.toList() }

    override val keys: Iterable<K>
        get() = lock.withLock { targetMap.keys.toList() }

    override val values: Iterable<V>
        get() = lock.withLock { targetMap.values.toList() }

    override fun contains(key: K): Boolean {
        return lock.withLock { targetMap.contains(key) }
    }

    override fun remove(key: K): V? {
        return lock.withLock { targetMap.remove(key) }
    }

    override fun put(entry: Entry<K, V>): V? {
        return put(entry.key, entry.value)
    }

    override fun put(key: K, value: V): V? {
        return lock.withLock { targetMap.put(key, value) }
    }

    override fun set(key: K, value: V): V? {
        return put(key, value)
    }

    override fun get(key: K): V? {
        return lock.withLock { targetMap[key] }
    }
}
