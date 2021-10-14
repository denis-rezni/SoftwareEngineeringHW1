import java.util.*;

public class LRUCache<S, T> {

    private final MyLinkedList<S, T> list;
    private final Map<S, Node<S, T>> map;
    private final int maxSize;

    public LRUCache(int maxSize) {
        list = new MyLinkedList<>();
        map = new HashMap<>();
        this.maxSize = maxSize;
    }

    public T get(S key) {
        T result = getAndRemove(key);
        if (result != null) putToFirstPosition(key, result);
        return result;
    }

    public void put(S key, T value) {
        getAndRemove(key);
        putToFirstPosition(key, value);
    }

    private T getAndRemove(S key) {
        if (!map.containsKey(key)) return null;
        Node<S, T> oldNode = map.get(key);
        T result = oldNode.value;
        list.remove(oldNode);
        map.remove(key);
        return result;
    }

    private void putToFirstPosition(S key, T value) {
        Node<S, T> node = list.addFirst(key, value);
        if (list.size == maxSize + 1) {
            Node<S, T> removed = list.removeLast();
            map.remove(removed.key);
        }
        map.put(key, node);
    }

    static class Node<S, T> {
        S key;
        T value;
        Node<S, T> next;
        Node<S, T> prev;

        Node(S key, T value) {
            this.key = key;
            this.value = value;
        }
    }

    static class MyLinkedList<S, T> {
        Node<S, T> head;
        Node<S, T> tail;
        private int size;

        private final Node<S, T> EMPTY = new Node<>(null, null);

        MyLinkedList() {
            this.size = 0;
            this.head = EMPTY;
            this.tail = EMPTY;
        }

        Node<S, T> addFirst(S key, T value) {
            Node<S, T> node = new Node<>(key, value);
            size++;
            if (head == EMPTY) {
                EMPTY.next = node;
                node.prev = EMPTY;
                head = node;
                tail = node;
                return node;
            }
            Node<S, T> leftNode = head.prev;
            Node<S, T> rightNode = head;
            leftNode.next = node;
            rightNode.prev = node;
            node.prev = leftNode;
            node.next = rightNode;
            head = node;
            return node;
        }

        Node<S, T> removeLast() {
            if (tail == EMPTY) throw new RuntimeException("remove on empty list");
            size--;
            if (tail == head) {
                head = head.prev;
            }
            Node<S, T> removed = tail;
            tail = tail.prev;
            tail.next = null;
            return removed;
        }

        void remove(Node<S, T> node) {
            if (node == null) throw new RuntimeException("removing null");
            if (node == EMPTY) throw new RuntimeException("trying to remove empty element");
            Node<S, T> prev = node.prev;
            Node<S, T> next = node.next;
            if (node == tail) tail = node.prev;
            if (node == head) head = (node.next == null) ? EMPTY : node.next;
            prev.next = next;
            if (next != null) next.prev = prev;
            size--;
        }
    }
}
