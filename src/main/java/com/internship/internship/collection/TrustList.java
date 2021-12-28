package com.internship.internship.collection;

import java.util.*;
import java.util.function.Consumer;

public class TrustList<T> implements java.util.List<T> {

    private static final Object[] DEFAULT_CAPACITY_EMPTY_ELEMENT_DATA = {};

    private int size;

    Object[] elementData;

    public TrustList() {
        this.elementData = DEFAULT_CAPACITY_EMPTY_ELEMENT_DATA;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size <= 0;
    }

    @Override
    public boolean contains(Object o) {

        for (int i = 0; i < size; i++) {
            if (elementData[i] == o)
                return true;
        }
        for (int i = 0; i < size; i++) {
            if (elementData[i].equals(o))
                return true;
        }

        return false;
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
        if (a.length < size)
            return (T1[]) Arrays.copyOf(elementData, size, a.getClass());
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

    @Override
    public boolean add(T t) {

        if (size == elementData.length)
            elementData = grow();
        elementData[size] = t;
        size++;
        return true;
    }

    private Object[] grow() {
        elementData = Arrays.copyOf(elementData, size + 10);
        return elementData;
    }

    @Override
    public boolean remove(Object o) {
        int i;

        for (i = 0; i < size; i++) {
            if (o.equals(elementData[i])) {
                size -= 1;
                elementData[size] = null;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object e : c)
            if (!contains(e))
                return false;
        return true;
    }


    @Override
    public boolean addAll(Collection<? extends T> c) {
        Object[] a = c.toArray();
        int numNew = a.length;
        if (numNew == 0)
            return false;
        if (numNew > (elementData).length - (size))
            elementData = grow();
        System.arraycopy(a, 0, elementData, size, numNew);
        size += numNew;
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        if (index < 0 || index > this.size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);

        int cSize = c.size();

        if (cSize == 0) return false;

        Object[] a = c.toArray();

        int numNew = a.length;
        if (numNew == 0)
            return false;

        if (numNew > (elementData).length - (size))
            elementData = grow();

        int numMoved = size - index;
        if (numMoved > 0)
            System.arraycopy(elementData, index,
                    elementData, index + numNew,
                    numMoved);
        System.arraycopy(a, 0, elementData, index, numNew);
        size = size + numNew;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return batchRemove(c, false, 0, size);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return batchRemove(c, true, 0, size);
    }

    boolean batchRemove(Collection<?> c, boolean complement,
                        final int from, final int end) {
        Objects.requireNonNull(c);

        int r;
        // Optimize for initial run of survivors
        for (r = from; ; r++) {
            if (r == end)
                return false;
            if (c.contains(elementData[r]) != complement)
                break;
        }
        int w = r++;
        try {
            for (; r < end; r++) {
                if (c.contains(elementData[r]) == complement)
                    elementData[w++] = elementData[r];
            }
        } finally {
            System.arraycopy(elementData, r, elementData, w, end - r);
            w += end - r;
            shiftTailOverGap(w, end);
        }
        return true;
    }

    private void shiftTailOverGap(int lo, int hi) {
        System.arraycopy(elementData, hi, elementData, lo, size - hi);
        for (int to = size, i = (size -= hi - lo); i < to; i++)
            elementData[i] = null;
    }

    @Override
    public void clear() {
        for (int to = size, i = size = 0; i < to; i++)
            elementData[i] = null;
    }

    @Override
    public T get(int index) {
        Objects.checkIndex(index, size);
        return (T) elementData[index];
    }

    @Override
    public T set(int index, T element) {
        elementData[index] = element;
        return element;
    }

    @Override
    public void add(int index, T element) {

        if (index > size || index < 0)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);

        if (size == elementData.length)
            elementData = grow();

        System.arraycopy(elementData, index,
                elementData, index + 1,
                size - index);

        elementData[index] = element;
        size = size + 1;
    }

    @Override
    public T remove(int index) {
        Objects.checkIndex(index, size);

        T oldValue = (T) elementData[index];

        final int newSize;
        if ((newSize = size - 1) > index)
            System.arraycopy(elementData, index + 1, elementData, index, newSize - index);

        size = newSize;
        elementData[size] = null;

        return oldValue;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elementData[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elementData[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (elementData[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(elementData[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public ListIterator<T> listIterator() {
        return null;
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return null;
    }

    @Override
    public java.util.List<T> subList(int fromIndex, int toIndex) {
        return new TrustList<>();
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
            if (i >= size)
                throw new NoSuchElementException();
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i + 1;
            lastRet = i;
            return (T) elementData[lastRet];
        }

        @Override
        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();

            try {
                TrustList.this.remove(lastRet);
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
                if (i >= es.length)
                    throw new ConcurrentModificationException();
                for (; i < size; i++)
                    action.accept(elementAt(es, i));
                // update once at end to reduce heap write traffic
                cursor = i;
                lastRet = i - 1;
            }
        }
    }

    static <E> E elementAt(Object[] es, int index) {
        return (E) es[index];
    }
}