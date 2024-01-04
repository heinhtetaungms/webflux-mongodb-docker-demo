package org.kyi.webfluxmongodbdockerdemo.repository;

import org.kyi.webfluxmongodbdockerdemo.documents.Car;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CarRepository extends ReactiveMongoRepository<Car, String> {
    Flux<Car> findByBrandIgnoreCase(String brand);
    Flux<Car> findByModelIgnoreCase(String model);
}
