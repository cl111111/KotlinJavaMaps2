package maps;

import java.util.Comparator;
import java.util.Iterator;

public class TreeBasedMap<K, V> implements CustomMutableMap<K, V> {
    private TreeMapNode<K, V> root;
    private Comparator<K> keyComparator;

    public TreebasedMap(Comparator<K> keyComparator) {
        this.keyComparator = keyComparator;
        this.root = null;
    }

    /*
    public V get(K key) {
        return getValueFromNode(root, key);
    }
    private V getValueFromNode(TreeMapNode<K, V> node, K key) {
        if (node == null) {
            return null;
        }
        int comparator = keyComparator.compare(key, node.getKey());
        if (comparator < 0) {
            return getValueFromNode(node.getLeft(), key);
        } else if (comparator > 0) {
            return getValueFromNode(node.getRight(), key);
        } else {
            return node.getValue();
        }
    }
     */

    /*
    public V put(K key, V value) {
        if (root == null) {
            root = new TreeMapNode<>(key, value);
            return null;
        } else {
            return insert(root, key, value);
        }
    }
    private V insert(TreeMapNode<K, V> node, K key, V value) {
        int comparator = keyComparator.compare(key, node.getKey());
        if (comparator < 0) {
            if (node.getLeft() == null) {
                node.setLeft(new TreeMapNode<>(key, value));
                return null;
            } else {
                return insert(node.getLeft(), key, value);
            }
        } else if (comparator > 0) {
            if (node.getRight() == null) {
                node.setRight(new TreeMapNode<>(key, value));
                return null;
            } else {
                return insert(node.getRight(), key, value);
            }
        } else {
            V oldValue = node.getValue();
            node.setValue(value);
            return oldValue;
        }
    }
    */

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

    public V put(K key, V value) {
        if (root == null) {
            root = new TreeMapNode<>(key, value);
            return null;
        }
        TreeMapNode<K, V> current = root;
        TreeMapNode<K, V> parent = null;
        int comparator;
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
        while (current != null) {
            comparator = keyComparator.compare(key, current.getKey());
            if (comparator < 0) {
                parent = current;
                current = current.getLeft();
            } else if (comparator > 0) {
                parent = current;
                current = current.getRight();
            } else {
                V removedValue = current.getValue();
                deleteNode(current, parent);
                return removedValue;
            }
        }
        return null;
    }

    private void deleteNode(TreeMapNode<K, V> current, TreeMapNode<K, V> parent) {
        if (current.getLeft() == null && current.getRight() == null) {
            // Some code here
        }
    }


    private class EntriesIterator implements Iterator<Entry<K, V>> {

    }

    public Iterable<Entry<K, V>> getEntries() {
        return null;
    }
    public Iterable<K> getKeys() {

    }

    public Iterable<V> getValues() {

    }
}
