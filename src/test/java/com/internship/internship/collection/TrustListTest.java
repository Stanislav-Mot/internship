package com.internship.internship.collection;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TrustListTest {
    private final List<Integer> list = new TrustList<>();

    @Test
    void size() {
        list.add(1);
        assertEquals(1, list.size());
    }

    @Test
    void isEmpty() {
        list.clear();
        assertEquals(true, list.isEmpty());
        list.add(1);
        assertEquals(false, list.isEmpty());
    }

    @Test
    void contains() {
        list.add(93);
        assertEquals(true, list.contains(93));
    }

    @Test
    void iterator() {
        Iterator<Integer> iterator = list.iterator();
        assertNotNull(iterator);
    }

    @Test
    void toArray() {
        list.add(0, 17);
        Object[] array = list.toArray();
        assertEquals(17, array[0]);
    }

    @Test
    void testToArray() {
        list.add(0, 37);
        Integer[] listToArray = list.toArray(new Integer[0]);
        assertEquals(37, listToArray[0]);
    }

    @Test
    void addAndGetAndSet() {
        list.add(-87);
        assertEquals(true, list.contains(-87));
        list.add(0, -87);
        assertEquals(-87, list.get(0));
        list.set(0, 8);
        assertEquals(8, list.get(0));
    }

    @Test
    void remove() {
        list.add(0, -337);
        assertEquals(true, list.contains(-337));

        list.remove(0);
        assertEquals(false, list.contains(-337));

    }

    @Test
    void containsAll() {
        List<Integer> forContains = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 8, 9, 10));
        for (int i = 0; i < 11; i++) {
            list.add(i);
        }
        assertEquals(true, list.containsAll(forContains));
    }

    @Test
    void addAll() {
        List<Integer> forContains = new ArrayList<>(Arrays.asList(33, 34));
        list.addAll(forContains);

        boolean forCheck = (list.contains(33) & list.contains(34));
        assertEquals(true, forCheck);
    }

    @Test
    void removeAll() {
        List<Integer> forContains = new ArrayList<>(Arrays.asList(44, 45));
        list.addAll(forContains);

        list.removeAll(forContains);
        boolean forCheck = (list.contains(33) & list.contains(34));
        assertEquals(false, forCheck);
    }

    @Test
    void retainAll() {
        List<Integer> forContains = new ArrayList<>(Arrays.asList(44, 45));

        for (int i = 0; i < 50; i++) {
            list.add(i);
        }
        list.retainAll(forContains);

        assertEquals(2, list.size());
    }

    @Test
    void clear() {
        list.clear();
        assertEquals(true, list.isEmpty());
    }

    @Test
    void indexOf() {
        list.clear();
        List<Integer> addList = new ArrayList<>(Arrays.asList(44, 45, 46, 46));
        list.addAll(addList);
        assertEquals(2, list.indexOf(46));
    }

    @Test
    void lastIndexOf() {
        list.clear();
        List<Integer> addList = new ArrayList<>(Arrays.asList(44, 45, 46, 46));
        list.addAll(addList);
        assertEquals(3, list.lastIndexOf(46));
    }
}