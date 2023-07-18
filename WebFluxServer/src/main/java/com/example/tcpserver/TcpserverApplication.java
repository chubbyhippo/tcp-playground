package com.example.tcpserver;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.netty.tcp.TcpServer;

@SpringBootApplication
public class TcpserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(TcpserverApplication.class, args);
    }

    @PostConstruct
    private void init() {
        TcpServer.create()
                .port(9876)
                .wiretap(true)
                .handle((nettyInbound, nettyOutbound) -> nettyOutbound.sendByteArray(
                        nettyInbound.receive()
                                .asByteArray()))
                .bindNow()
                .onDispose()
                .block();
    }

}
