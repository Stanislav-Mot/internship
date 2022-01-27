package com.internship.internship.collection;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CompareWithRealList {
    private final List<Integer> trustList = new TrustList<>();
    private final List<Integer> realList = new ArrayList<>();

    @Test
    void realAddTest() {
        for (int i = 0; i < 100000; i++) {
            realList.add(i);
        }
    }

    @Test
    void trustAddTest() {
        for (int i = 0; i < 100000; i++) {
            trustList.add(i);
        }
    }

    @Test
    void realRemoveTest() {

        for (int i = 0; i < 100000; i++) {
            realList.add(i);
        }
        for (int i = 0; i < 10000; i++) {
            realList.remove(i);
        }
    }

    @Test
    void trustRemoveTest() {

        for (int i = 0; i < 100000; i++) {
            trustList.add(i);
        }
        for (int i = 0; i < 10000; i++) {
            trustList.remove(i);
        }
    }
}
