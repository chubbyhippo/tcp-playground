package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
class DemoController {

    private final DemoService service;

    DemoController(DemoService service) {
        this.service = service;
    }


    @GetMapping("/echo")
    public byte[] echo(@RequestParam String value) throws ExecutionException, InterruptedException {
        byte[] bytes = service
                .sendAndGet(value)
                .get();
        return bytes;
    }

    @GetMapping("/list")
    public List<byte[]> getList() {
        return service.getBytes();
    }

}
