package maps

interface Node<T> {
    var next: ValueNode<T>?
}

class RootNode<T>(override var next: ValueNode<T>? = null) : Node<T>

class ValueNode<T>(override var next: ValueNode<T>?, val value: T) : Node<T>
