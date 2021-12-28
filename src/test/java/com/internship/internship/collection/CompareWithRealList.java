package com.internship.internship.collection;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CompareWithRealList {
    private List<Integer> trustList = new TrustList<>();
    private List<Integer> realList = new ArrayList<>();

    @Test
    void realAddTest(){
//      realList = IntStream.generate(() -> new Random().nextInt(90)).limit(100000).boxed().collect(Collectors.toList());

        for (int i = 0; i < 100000; i++) {
            realList.add(i);
        }
    }

    @Test
    void trustAddTest(){
//        trustList = IntStream.generate(() -> new Random().nextInt(90)).limit(100000).boxed().collect(Collectors.toList());
        for (int i = 0; i < 100000; i++) {
            trustList.add(i);
        }
    }

    @Test
    void realRemoveTest(){
//      realList = IntStream.generate(() -> new Random().nextInt(90)).limit(100000).boxed().collect(Collectors.toList());

        for (int i = 0; i < 100000; i++) {
            realList.add(i);
        }
        for (int i = 0; i < 10000; i++) {
            realList.remove(i);
        }
    }

    @Test
    void trustRemoveTest(){
//        trustList = IntStream.generate(() -> new Random().nextInt(90)).limit(100000).boxed().collect(Collectors.toList());
        for (int i = 0; i < 100000; i++) {
            trustList.add(i);
        }
        for (int i = 0; i < 10000; i++) {
            trustList.remove(i);
        }
    }
}
