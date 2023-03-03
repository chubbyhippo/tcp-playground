package com.example.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelOption;
import io.netty.handler.codec.string.StringEncoder;
import jakarta.annotation.PostConstruct;
import org.reactivestreams.Publisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.tcp.TcpClient;

import java.time.Duration;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
public class DemoController {

    private Connection connection;
    private Flux<String> stringFlux;

    @GetMapping("/echo")
    public Mono<String> echo(@RequestParam String value) {
        connection.outbound()
//                .sendString(Mono.just(value))
                .sendByteArray(Mono.just(value.getBytes()))
                .then()
                .subscribe();

        return stringFlux.as(Mono::from);

    }

    @GetMapping("/search")
    public Mono<String> search(@RequestParam String value) {


        return stringFlux.next();

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
                .handle((nettyInbound, nettyOutbound) -> {
                    stringFlux = nettyInbound.receive().asString().cache(0);
                    return nettyOutbound.neverComplete();
                })
                .connectNow();
    }
}
