import java.util.*;

public class LRUCache<S, T> {

    private final LinkedList<T> list;
    private final Map<S, Iterator<T>> map;
    private final int maxSize;

    public LRUCache(int maxSize) {
        list = new LinkedList<>();
        map = new HashMap<>();
        this.maxSize = maxSize;
    }

    public T get(S key) {
        T result = getAndRemove(key);
        putToFirstPosition(key, result);
        return result;
    }

    private T getAndRemove(S key) {
        if (!map.containsKey(key)) return null;
        Iterator<T> oldPosition = map.get(key);
        T result = oldPosition.next();
        oldPosition.remove();
        return result;
    }

    private void putToFirstPosition(S key, T value) {
        list.addFirst(value);
        Iterator<T> newPosition = list.iterator();
        map.put(key, newPosition);
    }

    public void put(S key, T value) {
        getAndRemove(key);
        if (list.size() == maxSize) {
            list.pollLast();
        }
        putToFirstPosition(key, value);
    }
}
