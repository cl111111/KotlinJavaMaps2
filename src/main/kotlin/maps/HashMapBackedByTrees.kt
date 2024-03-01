package maps

class HashMapBackedByTrees<K, V>(private val keyComparator: Comparator<K>) : GenericHashMap<K, V>({ TreeBasedMap(keyComparator) })
