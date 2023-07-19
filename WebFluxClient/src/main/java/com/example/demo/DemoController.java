package com.example.demo;

import io.netty.channel.ChannelOption;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.tcp.TcpClient;

@RestController
public class DemoController {

    private Connection connection;
    private Flux<String> stringFlux;
    private Mono<String> stringMono;

    @GetMapping("/echo")
    public Mono<String> echo(@RequestParam String value) {
        connection.outbound()
//                .sendString(Mono.just(value))
                .sendByteArray(Mono.just(value.getBytes()))
                .then()
                .subscribe();

        return connection.inbound().receive().asString().cache(0).as(Mono::from);


    }

    @GetMapping("/search")
    public Mono<String> search(@RequestParam String value) {


        return stringFlux.filter(s -> {

            return s.contains(value);
        }).next();

    }

    @GetMapping("/send")
    public void send(@RequestParam String value) {
        connection.outbound()
                .sendString(Mono.just(value))
//                .sendByteArray(Mono.just(value.getBytes()))
                .then()
                .subscribe();
    }

    @GetMapping("/flux")
    public Flux<String> flux() {
        return stringFlux;
    }

    @PostConstruct
    private void init() {
        connection = TcpClient.create()
                .host("localhost")
                .port(9876)
                .wiretap(true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .connectNow();
    }
}
