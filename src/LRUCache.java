import com.sun.istack.internal.NotNull;

import java.util.*;

public class LRUCache<S, T> {

    private final MyLinkedList<S, T> list;
    private final Map<S, Node<S, T>> map;
    private final int maxSize;

    public LRUCache(int maxSize) {
        if (maxSize < 1) throw new RuntimeException("maxSize less than one");
        list = new MyLinkedList<>();
        map = new HashMap<>();
        this.maxSize = maxSize;
    }

    public T get(@NotNull S key) {
        assert size() <= maxSize;
        int sizeBefore = size();
        T result = getAndRemove(key);
        assert size() <= sizeBefore;
        if (result != null) putToFirstPosition(key, result);
        assert size() == sizeBefore;
        assert size() <= maxSize;
        assert result == null || list.head.key == key;
        return result;
    }

    public void put(@NotNull S key, @NotNull T value) {
        assert size() <= maxSize;
        int sizeBefore = size();
        getAndRemove(key);
        assert size() <= sizeBefore;
        putToFirstPosition(key, value);
        assert sizeBefore + 1 == size();
        assert size() <= maxSize;
        assert list.head.key == key && list.head.value == value;
        assert map.get(key) == value;
        assert map.get(key).key == key;
    }

    public int size() {
        assert size() <= maxSize;
        return list.size;
    }

    private T getAndRemove(S key) {
        assert size() <= maxSize;
        int sizeBefore = size();
        if (!map.containsKey(key)) return null;
        Node<S, T> oldNode = map.get(key);
        T result = oldNode.value;
        list.remove(oldNode);
        map.remove(key);
        assert size() <= sizeBefore;
        assert size() <= maxSize;
        return result;
    }

    private void putToFirstPosition(S key, T value) {
        Node<S, T> node = list.addFirst(key, value);
        if (list.size == maxSize + 1) {
            Node<S, T> removed = list.removeLast();
            map.remove(removed.key);
        }
        map.put(key, node);
        assert node == list.head;
        assert size() <= maxSize;
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
            int sizeBefore = size;
            Node<S, T> node = new Node<>(key, value);
            size++;
            if (head == EMPTY) {
                EMPTY.next = node;
                node.prev = EMPTY;
                head = node;
                tail = node;
                assert sizeBefore + 1 == size;
                return node;
            }
            Node<S, T> leftNode = head.prev;
            Node<S, T> rightNode = head;
            leftNode.next = node;
            rightNode.prev = node;
            node.prev = leftNode;
            node.next = rightNode;
            head = node;
            assert sizeBefore + 1 == size;
            assert head != tail;
            return node;
        }

        Node<S, T> removeLast() {
            int sizeBefore = size;
            Node<S, T> tailBefore = tail;
            if (tail == EMPTY) throw new RuntimeException("remove on empty list");
            size--;
            if (tail == head) {
                head = head.prev;
            }
            Node<S, T> removed = tail;
            tail = tail.prev;
            tail.next = null;
            assert sizeBefore - 1 == size;
            assert tailBefore.prev == tail;
            return removed;
        }

        void remove(Node<S, T> node) {
            int sizeBefore = size;
            if (node == null) throw new RuntimeException("removing null");
            if (node == EMPTY) throw new RuntimeException("trying to remove empty element");
            size--;
            Node<S, T> prev = node.prev;
            Node<S, T> next = node.next;
            if (node == tail) tail = node.prev;
            if (node == head) head = (node.next == null) ? EMPTY : node.next;
            prev.next = next;
            if (next != null) next.prev = prev;
            assert sizeBefore - 1 == size;
            assert node != tail;
            assert node != head;
        }
    }
}
