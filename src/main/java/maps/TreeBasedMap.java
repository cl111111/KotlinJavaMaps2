package maps;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TreeBasedMap<K, V> implements CustomMutableMap<K, V> {
    private TreeMapNode<K, V> root;
    private final Comparator<K> keyComparator;

    public TreeBasedMap(Comparator<K> keyComparator) {
        this.keyComparator = keyComparator;
        this.root = null;
    }

    public V get(K key) {
        TreeMapNode<K, V> current = root;
        while (current != null) {
            int comparator = keyComparator.compare(key, current.getKey());
            if (comparator < 0) {
                current = current.getLeft();
            } else if (comparator > 0) {
                current = current.getRight();
            } else {
                return current.getValue();
            }
        }
        return null;
    }

    public V put(Entry<K, V> entry) {
        return put(entry.getKey(), entry.getValue());
    }

    public V put(K key, V value) {
        if (root == null) {
            root = new TreeMapNode<>(key, value);
            return null;
        }
        TreeMapNode<K, V> current = root;
        TreeMapNode<K, V> parent = null;
        int comparator = 0;
        while (current != null) {
            comparator = keyComparator.compare(key, current.getKey());
            if (comparator < 0) {
                parent = current;
                current = current.getLeft();
            } else if (comparator > 0) {
                parent = current;
                current = current.getRight();
            } else {
                V oldValue = current.getValue();
                current.setValue(value);
                return oldValue;
            }
        }
        TreeMapNode<K, V> newNode = new TreeMapNode<>(key, value);
        if (comparator < 0) {
            parent.setLeft(newNode);
        } else {
            parent.setRight(newNode);
        }
        return null;
    }


    public V remove(K key) {
        if (root == null) {
            return null;
        }
        TreeMapNode<K, V> current = root;
        TreeMapNode<K, V> parent = null;
        int comparator;
        boolean isLeftChild = true;
        while (current != null) {
            comparator = keyComparator.compare(key, current.getKey());
            if (comparator < 0) {
                parent = current;
                current = current.getLeft();
                isLeftChild = true;
            } else if (comparator > 0) {
                parent = current;
                current = current.getRight();
                isLeftChild = false;
            } else {
                if (current.getLeft() == null && current.getRight() == null) {
                    V removedValue = current.getValue();
                    deleteNode(current, parent, isLeftChild);
                    return removedValue;
                } else {
                    TreeMapNode<K, V> successor = findSuccessor(current);
                    TreeMapNode<K, V> successorParent = findSuccessorParent(current);
                    V oldValue = current.getValue();
                    current.setKey(successor.getKey());
                    current.setValue(successor.getValue());
                    isLeftChild = !(current.getRight() == successor);
                    deleteNode(successor, successorParent, isLeftChild);
                    return oldValue;
                }
            }
        }
        return null;
    }

    private void deleteNode(TreeMapNode<K, V> current, TreeMapNode<K, V> parent, boolean isLeftChild) {
        if (parent == null) {
            if (current.getLeft() == null && current.getRight() == null) {
                root = null;
            } else if (current.getLeft() == null) {
                root = current.getRight();
            } else {
                root = current.getLeft();
            }
        }

        assert parent != null;
        if (current.getLeft() == null && current.getRight() == null) {
            if (isLeftChild) {
                parent.setLeft(null);
            } else {
                parent.setRight(null);
            }
        } else if (current.getLeft() == null) {
            TreeMapNode<K, V> child = current.getRight();
            if (isLeftChild) {
                parent.setLeft(child);
            } else {
                parent.setRight(child);
            }
        } else {
            TreeMapNode<K, V> child = current.getLeft();
            if (isLeftChild) {
                parent.setLeft(child);
            } else {
                parent.setRight(child);
            }
        }
    }

    private TreeMapNode<K, V> findSuccessor(TreeMapNode<K, V> node) {
        node = node.getRight();
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }
    private TreeMapNode<K, V> findSuccessorParent(TreeMapNode<K, V> node) {
        TreeMapNode<K, V> parent = node;
        node = node.getRight();
        while (node.getLeft()!= null) {
            parent = node;
            node = node.getLeft();
        }
        return parent;
    }


    public V set(K key, V value) {
        return put(key, value);
    }

    public boolean contains(K key) {
        return get(key) != null;
    }

    private class EntriesIterator implements Iterator<Entry<K, V>> {
        private final Deque<TreeMapNode<K, V>> stack = new LinkedList<>();

        public EntriesIterator(TreeMapNode<K, V> root) {
            pushLeftNodes(root);
        }

        public boolean hasNext() {
            return !stack.isEmpty();
        }

        public Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There are no more items to iterate over.");
            }
            TreeMapNode<K, V> current = stack.pop();
            pushLeftNodes(current.getRight());
            return new Entry<>(current.getKey(), current.getValue());
        }
        private void pushLeftNodes(TreeMapNode<K, V> node) {
            while (node != null) {
                stack.push(node);
                node = node.getLeft();
            }
        }
    }

    @NotNull
    public Iterable<Entry<K, V>> getEntries() {
        return () -> new EntriesIterator(root);
    }

    private class KeysIterator implements Iterator<K> {
        private final Iterator<Entry<K, V>> entriesIterator = new EntriesIterator(root);

        public boolean hasNext() {
            return entriesIterator.hasNext();
        }

        public K next() {
            return entriesIterator.next().getKey();
        }
    }

    @NotNull
    public Iterable<K> getKeys() {
        return KeysIterator::new;
    }

    private class ValuesIterator implements Iterator<V> {
        private final Iterator<Entry<K, V>> entriesIterator = new EntriesIterator(root);

        public boolean hasNext() {
            return entriesIterator.hasNext();
        }

        public V next() {
            return entriesIterator.next().getValue();
        }
    }
    @NotNull
    public Iterable<V> getValues() {
        return ValuesIterator::new;
    }
}
