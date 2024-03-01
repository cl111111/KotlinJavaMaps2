package maps

class StripedHashMapBackedByTrees<K, V>(private val keyComparator: Comparator<K>) : StripedGenericHashMap<K, V> ({ TreeBasedMap(keyComparator) })
