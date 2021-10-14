import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LRUCacheTest {


    @Test
    void oneObject() {
        LRUCache<String, String> cache = new LRUCache<>(3);
        cache.put("key1", "value1");
        assertEquals("value1", cache.get("key1"));
        assertNull(cache.get("key2"));
    }

    @Test
    void threeObjects() {
        LRUCache<String, String> cache = new LRUCache<>(3);
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");
        assertEquals("value1", cache.get("key1"));
        assertEquals("value3", cache.get("key3"));
        assertEquals("value3", cache.get("key3"));
        assertEquals("value2", cache.get("key2"));
        assertNull(cache.get("key4"));
    }

    @Test
    void overflowByOne() {
        LRUCache<String, String> cache = new LRUCache<>(2);
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");
        assertNull(cache.get("key1"));
        assertEquals("value2", cache.get("key2"));
        assertEquals("value3", cache.get("key3"));
    }

    @Test
    void getBringsToFirstPosition() {
        LRUCache<String, String> cache = new LRUCache<>(3);
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");
        assertEquals("value1", cache.get("key1"));
        cache.put("key4", "value4");
        assertNull(cache.get("key2"));
        assertEquals("value1", cache.get("key1"));
        assertEquals("value3", cache.get("key3"));
        assertEquals("value4", cache.get("key4"));
        cache.put("key5", "value5");
        assertNull(cache.get("key1"));
    }

    @Test
    void overflowByMoreThanOne() {
        LRUCache<String, String> cache = new LRUCache<>(2);
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");
        cache.put("key4", "value4");
        cache.put("key5", "value5");
        assertNull(cache.get("key1"));
        assertNull(cache.get("key2"));
        assertNull(cache.get("key3"));
        assertEquals("value4", cache.get("key4"));
        assertEquals("value5", cache.get("key5"));
        cache.put("key1", "value1");
        assertEquals("value5", cache.get("key5"));
        assertEquals("value1", cache.get("key1"));
        assertNull(cache.get("key2"));
        assertNull(cache.get("key3"));
        assertNull(cache.get("key4"));
    }

}
