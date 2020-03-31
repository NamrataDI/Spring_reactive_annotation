package com.wiredBrain.productapiannotaion;

import com.wiredBrain.productapiannotaion.model.Product;
import com.wiredBrain.productapiannotaion.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class ProductApiAnnotaionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductApiAnnotaionApplication.class, args);
		}

	@Bean
	CommandLineRunner init(ProductRepository repository)
	{
		return args -> {
			Flux<Product> productlux = Flux.just(
					new Product(null, "Pro1", 1.99),
					new Product(null, "Pro2", 2.99),
					new Product(null, "Pro2", 3.99))
					.flatMap(p ->repository.save(p));

			productlux.thenMany(repository.findAll())
					.subscribe(System.out::println);
		};
	}



	}


