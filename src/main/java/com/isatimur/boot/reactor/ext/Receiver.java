package com.isatimur.boot.reactor.ext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.bus.Event;
import reactor.fn.Consumer;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Тимакс on 10.09.2015.
 */
@Service
public class Receiver implements Consumer<Event<Integer>> {
    @Autowired
    CountDownLatch latch;

    RestTemplate restTemplate = new RestTemplate();

    @Override
    public void accept(Event<Integer> ev) {
        QuoteResource quoteResource =
                restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", QuoteResource.class);
        System.out.println("Quote " + ev.getData() + ": " + quoteResource.getValue().getQuote());
        latch.countDown();

    }

//    @Autowired
//    CountDownLatch latch;
//
//    RestTemplate restTemplate = new RestTemplate();
//
//    public void accept(Event<Integer> ev) {
//        QuoteResource quoteResource =
//                restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", QuoteResource.class);
//        System.out.println("Quote " + ev.getData() + ": " + quoteResource.getValue().getQuote());
//        latch.countDown();
//    }

}