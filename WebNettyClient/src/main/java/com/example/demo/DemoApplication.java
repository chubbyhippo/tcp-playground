package com.example.demo;

import io.netty.channel.ChannelOption;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.tcp.TcpClient;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public Connection connect() {
        return TcpClient.create()
                .host("localhost")
                .port(9876)
                .wiretap(true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .connectNow();
    }

}

