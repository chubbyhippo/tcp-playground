package com.example.demo;

import io.netty.handler.codec.serialization.ObjectEncoder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;

import java.util.List;
import java.util.Objects;
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

        connection.outbound()
//                .sendByteArray(Mono.just(value.getBytes()))
                .sendByteArray(Mono.just(Objects.requireNonNull(SerializationUtils.serialize(value))))
                .then()
                .subscribe();
    }

    public List<String> getStringList() {
        return repository.getStringList();
    }

    @Async
    public CompletableFuture<String> sendAndGet(String value) {
        send(value);
        while (true) {
            var result = repository.getStringList()
                    .stream().filter(s -> s.equals(value))
                    .findFirst();


            if (result.isPresent()) {
                repository.getStringList().removeIf(s -> s.equals(value));
                return CompletableFuture.completedFuture(result.get());
            }

        }

    }

}
