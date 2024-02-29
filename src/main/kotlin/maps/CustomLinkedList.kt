package maps

class CustomLinkedList<T> : MutableIterable<T> {
    private val rootNode: RootNode<T> = RootNode()
    var size: Int = 0
        private set

    val isEmpty: Boolean
        get() = head == null

    private var head: ValueNode<T>?
        get() = rootNode.next
        set(value) {
            rootNode.next = value
        }

    fun peek(): T? = head?.value

    fun add(value: T) {
        head = ValueNode(head, value)
        size++
    }

    fun remove(): T? {
        if (isEmpty) {
            return null
        }
        val nextNode: ValueNode<T> = head!!
        head = nextNode.next
        size--
        return nextNode.value
    }

    fun remove(pred: (T) -> Boolean): T? {
        val iterator: MutableIterator<T> = iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (pred(item)) {
                iterator.remove()
                return item
            }
        }
        return null
    }

    override fun iterator(): MutableIterator<T> = object : MutableIterator<T> {
        private var currentNode: Node<T> = rootNode
        private var previousNode: Node<T>? = null
        private var canRemove: Boolean = false

        override fun hasNext(): Boolean {
            return currentNode.next != null
        }

        override fun next(): T {
            if (!hasNext()) {
                throw NoSuchElementException("There are no nodes in the linked list.")
            }
            previousNode = currentNode
            currentNode = currentNode.next as ValueNode<T>
            canRemove = true
            return (currentNode as ValueNode<T>).value
        }

        override fun remove() {
            if (isEmpty) {
                throw IllegalArgumentException("The linked list is empty.")
            }
            if (!canRemove) {
                throw IllegalStateException("Cannot remove node without calling next.")
            } else {
                canRemove = false
                val newCurrentNode: Node<T> = previousNode!!
                newCurrentNode.next = currentNode.next
                currentNode = newCurrentNode
                size--
            }
        }
    }
}
