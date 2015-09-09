package com.isatimur.boot.reactor;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.Environment;
import reactor.rx.broadcast.Broadcaster;

@SpringBootApplication
public class SpringBootReactorApplication {

    public static void main(String[] args) throws InterruptedException {
//        SpringApplication.run(SpringBootReactorApplication.class, args);
        Environment.initialize();

        Broadcaster<String> sink = Broadcaster.create(Environment.get());

        sink.dispatchOn(Environment.cachedDispatcher())
                .map(String::toUpperCase)
                .consume(s -> System.out.printf("s=%s%n", s));

        sink.onNext("Hello World!");
        sink.onNext("Hello World!");
        sink.onNext("Hello World!");

        Thread.sleep(500);
        sink.onNext("Hello World!");
        sink.onNext("Hello World!");

    }
}
