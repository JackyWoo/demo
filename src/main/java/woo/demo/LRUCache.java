package woo.demo;

import java.util.HashMap;

/**
 * A thread-safe LRU cache.
 *
 * Created by wujianchao on 2020/7/18.
 */
public class LRUCache<K, V> {


    public static void main(String[] args) {

        // simple test

        LRUCache<String, Integer> lruCache = new LRUCache<>(3);
        lruCache.put("1", 1);
        lruCache.put("2", 2);

        System.out.println(lruCache.get("1") == 1);

        lruCache.put("3", 3);
        lruCache.put("4", 4);

        System.out.println(lruCache.get("1") == 1);
        System.out.println(lruCache.get("2") == null);
        System.out.println(lruCache.get("3") == 3);

        lruCache.put("4", 5);
        System.out.println(lruCache.get("4") == 5);
    }

    class Node {
        private final K k;
        private V v;

        Node pre = null;
        Node next = null;

        Node(K k, V v) {
            this.k = k;
            this.v = v;
        }
    }

    class LRUList {
        private final Node head = new Node(null, null);
        private final Node tail = new Node(null, null);

        LRUList() {
            head.next = tail;
            tail.pre = head;
        }
    }

    /**
     * current size
     */
    private int size = 0;
    private final int capacity;

    private final HashMap<K, Node> data = new HashMap<>();

    private final LRUList lru = new LRUList();

    public LRUCache(int capacity) {
        this.capacity = capacity;
    }

    // Because "get" will modify "LRUList",
    // so there is no need to use ReadWriteLock.
    public synchronized V get(K key) {
        Node n = find(key);
        if (n == null) {
            return null;
        }
        // move to head
        moveToHead(n);
        return n.v;
    }

    public synchronized void put(K key, V value) {
        if(key == null || value == null){
            throw new IllegalArgumentException("key, value can not be null");
        }
        Node n = find(key);
        if (n != null) {
            n.v = value;
            // move to head
            moveToHead(n);
        } else {
            n = new Node(key, value);
            data.put(key, n);
            // add to head
            addToHead(n);
            // reach capacity
            removeTail();
        }
    }

    private Node find(K key) {
        return data.get(key);
    }

    private void removeTail() {
        if (size > capacity) {
            Node toRemove = lru.tail.pre;
            toRemove.pre.next = lru.tail;

            lru.tail.pre = toRemove.pre;
            data.remove(toRemove.k);

            size--;
        }
    }

    private void addToHead(Node n) {
        Node next = lru.head.next;
        lru.head.next = n;

        n.pre = lru.head;
        n.next = next;
        next.pre = n;

        size++;
    }

    private void moveToHead(Node n) {
        Node pre = n.pre;
        Node next = n.next;

        // n is already head
        if (n.pre == lru.head) {
            return;
        }

        pre.next = next;
        next.pre = pre;

        Node headNext = lru.head.next;
        lru.head.next = n;

        n.pre = lru.head;
        n.next = headNext;
        headNext.pre = n;
    }
}
