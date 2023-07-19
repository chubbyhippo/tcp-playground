package com.example.webfluxspringintegrationtcpclient.controller;

import com.example.webfluxspringintegrationtcpclient.gateway.TcpClientGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class TcpClientController {

    private final TcpClientGateway client;

    @GetMapping("/echo")
    Mono<String> echo(@RequestParam String value) {
        byte[] send = client.send(value.getBytes());
        return Mono.just(send)
                .map(String::new);
    }
}
