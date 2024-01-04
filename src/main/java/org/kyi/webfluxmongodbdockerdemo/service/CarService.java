package org.kyi.webfluxmongodbdockerdemo.service;

import org.kyi.webfluxmongodbdockerdemo.documents.Car;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface CarService {
    Flux<Car> all();
    Mono<Car> byId(String id);
    Flux<Car> byBrand(String brand);
    Mono<Car> create(Car car);
    Mono<Car> update(String id, Car car);
    Mono<Void> deleteById(String id);
    Mono<Optional<Car>> createCarMonoOpt(Mono<Car> carMono);
}
