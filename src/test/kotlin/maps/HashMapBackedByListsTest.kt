package maps

class HashMapBackedByListsTest : CustomMutableMapTest() {
    override fun emptyCustomMutableMapStringInt(): CustomMutableMap<String, Int> = HashMapBackedByLists { ListBasedMap() }

    override fun emptyCustomMutableMapCollidingStringInt(): CustomMutableMap<CollidingString, Int> = HashMapBackedByLists { ListBasedMap() }
}
