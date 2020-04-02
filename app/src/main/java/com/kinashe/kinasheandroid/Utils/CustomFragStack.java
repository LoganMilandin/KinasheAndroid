package com.kinashe.kinasheandroid.Utils;

import java.util.LinkedList;

public class CustomFragStack<E> extends LinkedList<E> {
    public E pop() {
        return super.remove(size() - 1);
    }

    public void push(E item) {
        super.add(item);
    }
    public E peek() {
        return super.get(super.size() - 1);
    }
}
