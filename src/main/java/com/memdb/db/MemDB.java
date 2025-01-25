package com.memdb.db;

import java.util.Optional;

public interface MemDB {

    Optional<String> get(String key);

    String set(String key, String value);
}
