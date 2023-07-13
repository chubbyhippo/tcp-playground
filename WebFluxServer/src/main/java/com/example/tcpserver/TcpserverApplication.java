package com.example.tcpserver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelOption;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.netty.tcp.TcpClient;
import reactor.netty.tcp.TcpServer;

import java.util.HexFormat;

@SpringBootApplication
public class TcpserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(TcpserverApplication.class, args);
    }

    @PostConstruct
    private void init() {
        var res = HexFormat.of().parseHex("00363132333435363738393031323334353637383930313233343536373839304d31303030303130865add9abb9e9fac62f70b20fcbc405c");
        TcpServer.create()
                .host("localhost")
                .port(9876)
                .wiretap(true)
                .handle((nettyInbound, nettyOutbound) -> {

                    return nettyOutbound.sendByteArray(
                    nettyInbound.receive()
                            .asByteArray()
                            .map(bytes -> res));

                })
                .bindNow()
                .onDispose()
                .block();
    }

}
