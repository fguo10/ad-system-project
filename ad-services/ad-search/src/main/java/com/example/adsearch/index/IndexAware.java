package com.example.adsearch.index;

// use Generic type
// implement the CRUD for index
// not all the table and not all attributes need to create index
public interface IndexAware<K, V> {
    V get(K key);

    void add(K key, V val);

    void update(K key, V val);

    void delete(K key, V val);
}
