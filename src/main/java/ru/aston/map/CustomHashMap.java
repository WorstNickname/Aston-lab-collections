package ru.aston.map;

import java.util.Objects;

public class CustomHashMap<K, V> implements CustomMap<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final float RESIZE_COEFFICIENT = 1.5f;

    private Entry<K, V>[] table;
    private int capacity;
    private final float loadFactor;
    private int size;
    private int threshold;

    public CustomHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public CustomHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public CustomHashMap(int initialCapacity, float loadFactor) {
        this.capacity = initialCapacity;
        this.loadFactor = loadFactor;
        this.table = new Entry[initialCapacity];
        threshold = (int) (initialCapacity * loadFactor);
    }

    private static class Entry<K, V> {

        private final int hash;
        private final K key;
        private V value;
        private Entry<K, V> next;

        public Entry(K key, V value, int hash, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public int getHash() {
            return hash;
        }

        public Entry<K, V> getNext() {
            return next;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public void setNext(Entry<K, V> next) {
            this.next = next;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entry<?, ?> entry = (Entry<?, ?>) o;
            return Objects.equals(key, entry.key) && Objects.equals(value, entry.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }

    public static int hash(Object key) {
        return key == null ? 0 : key.hashCode();
    }

    @Override
    public V put(K key, V value) {
        if (key == null) {
            return putForNullKey(value);
        }

        int hash = hash(key);
        int index = findIndex(hash, table.length);
        Entry<K, V> entry = table[index];

        while (entry != null) {
            if (isEntryKeyEqualToKey(entry, key, hash)) {
                return replaceEntry(entry, value);
            }
            entry = entry.getNext();
        }

        return addEntry(key, value, hash, index);
    }

    @Override
    public V get(K key) {
        int hash = hash(key);
        int index = findIndex(hash, table.length);
        Entry<K, V> entry = table[index];

        while (entry != null) {
            if (isEntryKeyEqualToKey(entry, key, hash)) {
                return entry.getValue();
            }
            entry = entry.getNext();
        }

        return null;
    }

    @Override
    public int size() {
        return size;
    }

    private V putForNullKey(V value) {
        Entry<K, V> entry = table[0];

        while (entry != null) {
            if (entry.getKey() == null) {
                return replaceEntry(entry, value);
            }
            entry = entry.getNext();
        }

        return addEntry(null, value, 0, 0);
    }

    private V addEntry(K key, V value, int hash, int index) {
        Entry<K, V> entry = table[index];
        table[index] = new Entry<>(key, value, hash, entry);
        size++;
        ensureCapacity();
        return entry == null ? null : entry.getValue();
    }

    private V replaceEntry(Entry<K, V> entryToReplace, V newValue) {
        V oldValue = entryToReplace.getValue();
        entryToReplace.setValue(newValue);
        return oldValue;
    }

    private int findIndex(int hash, int tableLength) {
        return hash & (tableLength - 1);
    }

    private boolean isEntryKeyEqualToKey(Entry<K, V> entry, K key, int keyHash) {
        return entry.getHash() == keyHash && (entry.getKey() == key || entry.getKey().equals(key));
    }

    private void ensureCapacity() {
        if (size == threshold) {
            capacity = getNewCapacity();
            resize(capacity);
        }
    }

    private int getNewCapacity() {
        return (int) (capacity * RESIZE_COEFFICIENT);
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        Entry<K, V>[] oldTable = table;
        table = new Entry[newCapacity];
        transfer(oldTable, table);
        threshold = (int) (newCapacity * loadFactor);
    }

    private void transfer(Entry<K, V>[] oldTable, Entry<K, V>[] newTable) {
        for (Entry<K, V> entry : oldTable) {
            Entry<K, V> currentEntry = entry;
            while (currentEntry != null) {
                int hash = currentEntry.getHash();
                int newIndex = findIndex(hash, newTable.length);

                Entry<K, V> entryAtIndex = table[newIndex];
                table[newIndex] = currentEntry;

                Entry<K, V> temp = currentEntry.getNext();
                currentEntry.setNext(entryAtIndex);
                currentEntry = temp;
            }
        }
    }

}
