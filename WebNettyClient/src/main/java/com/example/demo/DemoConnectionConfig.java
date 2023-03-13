package com.example.demo;

import io.netty.channel.ChannelOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.SerializationUtils;
import reactor.netty.Connection;
import reactor.netty.tcp.TcpClient;

@Configuration
public class DemoConnectionConfig {

    private final DemoRepository repository;

    public DemoConnectionConfig(DemoRepository repository) {
        this.repository = repository;
    }

    @Bean
    public Connection connect() {
        return TcpClient.create()
                .host("localhost")
                .port(9876)
                .wiretap(true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handle((nettyInbound, nettyOutbound) -> {
                    nettyInbound
                            .receive()
                            .asByteArray()
                            .cache(0)
                            .subscribe(repository::save);

                    return nettyOutbound.neverComplete();
                })
                .connectNow();
    }
}
