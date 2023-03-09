package com.example.demo;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class DemoRepository {

    private final List<String> stringList = new CopyOnWriteArrayList<>();

    public void save(String value) {
        stringList.add(value);
    }

    public List<String> getStringList() {
        return stringList;
    }
}
