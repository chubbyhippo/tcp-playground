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
    public String echo(@RequestParam String value) throws ExecutionException, InterruptedException {
        return service.sendAndGet(value).get();
    }

    @GetMapping("/list")
    public List<String> getList() {
        return service.getStringList();
    }

}
