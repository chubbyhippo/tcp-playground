package com.example.demo;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class DemoRepository {

    private final List<byte[]> bytes = new CopyOnWriteArrayList<>();

    public void save(byte[] value) {
        bytes.add(value);
    }

    public List<byte[]> getBytes() {
        return bytes;
    }
}
