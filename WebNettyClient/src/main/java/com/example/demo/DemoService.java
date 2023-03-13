package com.example.demo;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class DemoService {

    private final Connection connection;
    private final DemoRepository repository;

    public DemoService(Connection connection, DemoRepository repository) {
        this.connection = connection;
        this.repository = repository;
    }

    public void send(String value) {

        byte[] bytes = value.getBytes();

        connection.outbound()
                .sendByteArray(Mono.just(bytes))
                .then()
                .subscribe();
    }

    public List<byte[]> getBytes() {
        return repository.getBytes();
    }

    @Async
    public CompletableFuture<byte[]> sendAndGet(String value) {
        send(value);
        while (true) {
            var result = repository.getBytes()
//                    .stream().filter(s -> s.equals(value))
                    .stream()
                    .findFirst();


            if (result.isPresent()) {
//                repository.getStringList().removeIf(s -> s.equals(value));
                return CompletableFuture.completedFuture(result.get());
            }

        }

    }

}
