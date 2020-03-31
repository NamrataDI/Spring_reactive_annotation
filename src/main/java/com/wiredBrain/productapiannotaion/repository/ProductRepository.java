package com.wiredBrain.productapiannotaion.repository;

import com.wiredBrain.productapiannotaion.model.Product;
import org.reactivestreams.Publisher;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ProductRepository extends ReactiveMongoRepository<Product,String> {

//    Flux<Product> findByName(String productName);
//    Flux<Product> findByNameOrderByPrice(String productName);

    /*
    Or you can write as below
     */

    Flux<Product> findByName(Publisher<String> productName);
    Flux<Product> findByNameOrderByPrice(Publisher<String> productName);
}
