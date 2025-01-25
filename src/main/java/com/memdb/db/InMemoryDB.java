package com.memdb.db;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryDB implements MemDB {

    private final Map<String, String> store;

    public InMemoryDB() {
        store = new HashMap<>();
    }

    public String set(final String key, final String value) {
        store.put(key, value);
        return value;
    }

    public Optional<String> get(final String key) {
        return Optional.ofNullable(store.get(key));
    }
}
