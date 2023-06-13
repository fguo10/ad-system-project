package com.example.adsearch.utils;

import java.util.Map;
import java.util.function.Supplier;

public class CommonUtils {
    public static <K, V> V getorCreate(K key, Map<K, V> map, Supplier<V> factory) {
        /* safely retrieving values from a map and computing new values only when necessary
        If key is not present in map:
            compute a new value using factory function (factory.get())
            associates it with the key in the map
        */
        return map.computeIfAbsent(key, k -> factory.get());
    }



}
