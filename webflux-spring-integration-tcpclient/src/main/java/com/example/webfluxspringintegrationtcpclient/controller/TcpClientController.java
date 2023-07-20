package com.example.webfluxspringintegrationtcpclient.controller;

import com.example.webfluxspringintegrationtcpclient.gateway.TcpClientGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
import org.springframework.integration.ip.tcp.serializer.ByteArraySingleTerminatorSerializer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
public class TcpClientController {

    private final TcpClientGateway client;
    private final AbstractClientConnectionFactory clientConnectionFactory;

    @PostMapping("/echo")
    Mono<String> echo(@RequestParam String value) {
//        clientConnectionFactory.setSoTimeout(10);
        clientConnectionFactory.setDeserializer(new ByteArraySingleTerminatorSerializer(value.getBytes()[value.getBytes().length - 1]));
        clientConnectionFactory.setSoTimeout(10);
        byte[] send = client.send(value.getBytes());
        return Mono.just(send)
                .delayElement(Duration.ofMillis(100L))
                .map(String::new);
    }
}
