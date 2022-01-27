package com.internship.internship.collection;

import java.util.*;
import java.util.function.Consumer;

public class TrustSet<T> implements java.util.Set<T> {

    public static final int SOFT_MAX_ARRAY_LENGTH = Integer.MAX_VALUE - 8;
    private static final Object[] EMPTY_CAPACITY = {};
    /**
     * Default initial capacity.
     */
    private static final int DEFAULT_CAPACITY = 10;
    private Integer size = 0;

    private Object[] elementData;

    public TrustSet() {
        this.elementData = EMPTY_CAPACITY;
    }

    public TrustSet(Integer size) {
        this.elementData = new Object[size];
        this.size = size;
    }

    public TrustSet(Collection<? extends T> c) {

        if (c.size() == 0) {
            return;
        }

        Object[] collection = c.toArray();

        for (int i = 0; i < collection.length; i++) {
            add((T) collection[i]);
        }
    }

    static <E> E elementAt(Object[] es, int index) {
        return (E) es[index];
    }

    public static int newLength(int oldLength, int minGrowth, int prefGrowth) {

        int prefLength = oldLength + Math.max(minGrowth, prefGrowth);
        if (0 < prefLength && prefLength <= SOFT_MAX_ARRAY_LENGTH) {
            return prefLength;
        } else {
            return hugeLength(oldLength, minGrowth);
        }
    }

    private static int hugeLength(int oldLength, int minGrowth) {
        int minLength = oldLength + minGrowth;
        if (minLength < 0) { // overflow
            throw new OutOfMemoryError(
                    "Required array length " + oldLength + " + " + minGrowth + " is too large");
        } else if (minLength <= SOFT_MAX_ARRAY_LENGTH) {
            return SOFT_MAX_ARRAY_LENGTH;
        } else {
            return minLength;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(T t) {
        if (size == 0 || size == elementData.length) {
            elementData = grow(size + 1);
        }

        if (size != 0) {
            Object o = Arrays.stream(elementData, 0, size).filter(s -> s.equals(t)).findFirst().orElse(null);
            if (o != null) {
                return false;
            }
        }

        elementData[size] = t;
        size++;
        return true;
    }

    private Object[] grow(int minCapacity) {
        int oldCapacity = elementData.length;
        if (oldCapacity > 0) {
            int newCapacity = newLength(oldCapacity, minCapacity - oldCapacity, oldCapacity >> 1);
            return elementData = Arrays.copyOf(elementData, newCapacity);
        } else {
            return elementData = new Object[Math.max(DEFAULT_CAPACITY, minCapacity)];
        }
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (c.size() == 0) {
            return false;
        }

        Object[] collection = c.toArray();

        for (int i = 0; i < collection.length; i++) {
            add((T) collection[i]);
        }
        return true;
    }

    @Override
    public boolean contains(Object o) {
        Object answer = Arrays.stream(elementData, 0, size)
                .filter(s -> s.equals(o)).findAny().orElse(null);
        return answer != null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if (c.size() == 0) {
            return false;
        }

        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(elementData[i])) {
                removeId(i);
                return true;
            }
        }
        return false;
    }

    public T removeId(int index) {
        Objects.checkIndex(index, size);
        final Object[] es = elementData;

        @SuppressWarnings("unchecked") T oldValue = (T) es[index];
        fastRemove(es, index);

        return oldValue;
    }


    private void fastRemove(Object[] es, int i) {
        final int newSize;
        if ((newSize = size - 1) > i)
            System.arraycopy(es, i + 1, es, i, newSize - i);
        es[size = newSize] = null;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (c.size() == 0)
            return false;
        for (Object o : c) {
            remove(o);
        }
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        return new Itr();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOfRange(elementData, 0, size);
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        if (a.length < size) {
            return (T1[]) Arrays.copyOf(elementData, size, a.getClass());
        }
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c.size() == 0) {
            return false;
        }
        final Object[] obj = new Object[size];
        System.arraycopy(elementData, 0, obj, 0, size);

        for (Object o : obj) {
            if (!c.contains(o)) {
                remove(o);
            }
        }
        return true;
    }

    @Override
    public void clear() {
        for (int to = size, i = size = 0; i < to; i++) {
            elementData[i] = null;
        }
    }

    private class Itr implements Iterator<T> {
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such

        // prevent creating a synthetic constructor
        Itr() {
        }

        public boolean hasNext() {
            return cursor != size;
        }

        @SuppressWarnings("unchecked")
        public T next() {
            int i = cursor;
            if (i >= size) {
                throw new NoSuchElementException();
            }
            if (i >= elementData.length) {
                throw new ConcurrentModificationException();
            }
            cursor = i + 1;
            lastRet = i;
            return (T) elementData[lastRet];
        }

        @Override
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }

            try {
                TrustSet.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            Objects.requireNonNull(action);
            int i = cursor;
            if (i < size) {
                final Object[] es = elementData;
                if (i >= es.length) {
                    throw new ConcurrentModificationException();
                }
                for (; i < size; i++) {
                    action.accept(elementAt(es, i));
                }
                // update once at end to reduce heap write traffic
                cursor = i;
                lastRet = i - 1;
            }
        }
    }
}
