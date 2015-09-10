package com.isatimur.boot.reactor;

import com.isatimur.boot.reactor.ext.Publisher;
import com.isatimur.boot.reactor.ext.Receiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import reactor.Environment;
import reactor.bus.EventBus;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static reactor.bus.selector.Selectors.$;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

    private static final int NUMBER_OF_QUOTES = 10;

    @Autowired
    private EventBus eventBus;

    @Autowired
    private Receiver receiver;

    @Autowired
    private Publisher publisher;

    @Bean
    public CountDownLatch latch() {
        return new CountDownLatch(NUMBER_OF_QUOTES);
    }

    @Bean
    Environment env() {
        return Environment.initializeIfEmpty()
                .assignErrorJournal();
    }

    @Bean
    EventBus createEventBus(Environment env) {
        return EventBus.create(env, Environment.THREAD_POOL);
    }


    @Override
    public void run(String... args) throws Exception {
        eventBus.on($("quotes"), receiver);
        publisher.publishQuotes(NUMBER_OF_QUOTES);
    }

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext app = SpringApplication.run(SpringBootReactorApplication.class, args);

        app.getBean(CountDownLatch.class).await(1, TimeUnit.SECONDS);

        app.getBean(Environment.class).shutdown();
    }

}

//    public static void main(String[] args) throws InterruptedException {
////        SpringApplication.run(SpringBootReactorApplication.class, args);
//        Environment.initialize();
//
//        Broadcaster<String> sink = Broadcaster.create(Environment.get());
//
//        sink.dispatchOn(Environment.cachedDispatcher())
//                .map(String::toUpperCase)
//                .consume(s -> System.out.printf("s=%s%n", s));
//
//        sink.onNext("Hello World!");
//        sink.onNext("Hello World!");
//        sink.onNext("Hello World!");
//
//        Thread.sleep(500);
//        sink.onNext("Hello World!");
//        sink.onNext("Hello World!");
//
//    }
//}
