package com.tiket.pool;

public interface Pool<T> {
    T take();

    void put(T item);

    void fill();

    void drain();
}
