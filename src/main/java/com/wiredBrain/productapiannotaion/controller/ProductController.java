package com.wiredBrain.productapiannotaion.controller;

import com.wiredBrain.productapiannotaion.model.Product;
import com.wiredBrain.productapiannotaion.model.ProductEvent;
import com.wiredBrain.productapiannotaion.repository.ProductRepository;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public Flux<Product> getAllProducts(){
        return  productRepository.findAll();
    }


    /*
    If id is not found in DB then empty Mono will return for  Mono<Product> return type

    defaultIfEmpty-->if product with specific id is not found then defaultIfEmpty will run with status NO FOUND
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Product>> getProductById(@PathVariable String id){
        return productRepository.findById(id)
                .map(product -> ResponseEntity.ok(product))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> saveProduct(@RequestBody Product product){
        return  productRepository.save(product);
    }

    @PutMapping("{id}")
    public Mono<ResponseEntity<Product>> updateProduct(@PathVariable String id,
                                                       @RequestBody Product product){
        return  productRepository.findById(id)
                .flatMap(existingProduct->{
                    existingProduct.setName(product.getName());
                    existingProduct.setPrice(product.getPrice());
                    return productRepository.save(existingProduct);
                })
                .map(updateProduct->ResponseEntity.ok(updateProduct))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public  Mono<ResponseEntity<Void>> deleteProduct(@PathVariable String id){
        return productRepository.findById(id)
                .flatMap(existingProduct->
                        productRepository.delete(existingProduct)
                .then(Mono.just(ResponseEntity.ok().<Void>build())))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @DeleteMapping
    public  Mono<Void> deleteProductAll(){
        return  productRepository.deleteAll();
    }


    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductEvent> getProductEvent(){
        return  Flux.interval(Duration.ofSeconds(2))
                .map(val->new ProductEvent(val,"Product Event"));
    }
}
