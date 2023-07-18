package com.example.adsearch.index;

// todo: 如果广告数据太多，JVM内存无法存储所有的索引数据，需要将索引迁移到Redis.
// use Generic type implement the CRUD for index
// not all the table and not all attributes need to create index
public interface IndexAware<K, V> {
    V get(K key);

    void add(K key, V val);

    void update(K key, V val);

    void delete(K key, V val);
}
