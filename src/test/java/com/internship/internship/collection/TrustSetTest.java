package com.internship.internship.collection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class TrustSetTest {
    private final Set<Integer> set = new TreeSet<>();

    @Test
    void size() {
        set.clear();
        set.addAll(Arrays.asList(1, 2, 2, 3));
        assertEquals(3, set.size());
    }

    @Test
    void isEmpty() {
        set.add(1);
        set.clear();
        assertEquals(true, set.isEmpty());
    }

    @Test
    void contains() {
        set.add(17);
        assertEquals(true, set.contains(17));
    }

    @Test
    void iterator() {
        Iterator<Integer> iterator = set.iterator();
        assertNotNull(iterator);
    }

    @Test
    void toArray() {
        set.clear();
        set.add(2);
        Object[] array = set.toArray();
        assertEquals(2, array[0]);
    }

    @Test
    void add() {
        set.add(100);
        assertTrue(set.contains(100));
    }

    @Test
    void remove() {
        set.add(39);
        set.remove(39);
        assertFalse(set.contains(39));
    }

    @Test
    void containsAll() {
        set.addAll(Arrays.asList(1, 2, 2, 3));
        assertTrue(set.containsAll(Arrays.asList(2, 3)));
    }

    @Test
    void addAll() {
        set.clear();
        set.addAll(Arrays.asList(1, 2, 2, 3, 5));
        assertEquals(4, set.size());
    }

    @Test
    void retainAll() {
        set.clear();
        set.addAll(Arrays.asList(1, 2, 3, 4, 5));
        set.retainAll(Arrays.asList(2, 3));
        assertEquals(2, set.size());
    }

    @Test
    void removeAll() {
        set.clear();
        set.addAll(Arrays.asList(1, 2, 3, 4, 5));
        set.removeAll(Arrays.asList(2, 3));
        assertEquals(3, set.size());
    }

    @Test
    void clear() {
        set.addAll(Arrays.asList(1, 2, 3, 4, 5));
        set.clear();
        assertTrue(set.isEmpty());
    }
}