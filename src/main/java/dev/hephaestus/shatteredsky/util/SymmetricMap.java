package dev.hephaestus.shatteredsky.util;

import java.util.HashMap;

public class SymmetricMap<T> extends HashMap<T, T> {
    @Override
    public T put(T key, T value) {
        super.put(key, value);
        return super.put(value, key);
    }
}
