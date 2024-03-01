package maps

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

abstract class StripedGenericHashMap<K, V>(bucketFactory: BucketFactory<K, V>) : GenericHashMap<K, V>(bucketFactory) {
    private val locks: List<ReentrantLock> = List(initialSize) { ReentrantLock() }

    override fun resize() {
        locks.forEach { it.lock() }
        try {
            super.resize()
        } finally {
            locks.forEach { it.unlock() }
        }
    }

    override fun contains(key: K): Boolean {
        locks[getIndex(key) % initialSize].withLock {
            return super.contains(key)
        }
    }

    override fun remove(key: K): V? {
        locks[getIndex(key) % initialSize].withLock {
            return super.remove(key)
        }
    }

    override fun put(key: K, value: V): V? {
        locks[getIndex(key) % initialSize].withLock {
            return super.put(key, value)
        }
    }

    override fun put(entry: Entry<K, V>): V? {
        return put(entry.key, entry.value)
    }

    override val entries: Iterable<Entry<K, V>>
        get() {
            locks.forEach { it.lock() }
            try {
                return super.entries
            } finally {
                locks.forEach { it.unlock() }
            }
        }

    override val keys: Iterable<K>
        get() {
            locks.forEach { it.lock() }
            try {
                return super.keys
            } finally {
                locks.forEach { it.unlock() }
            }
        }

    override val values: Iterable<V>
        get() {
            locks.forEach { it.lock() }
            try {
                return super.values
            } finally {
                locks.forEach { it.unlock() }
            }
        }
}
