package org.kyi.webfluxmongodbdockerdemo.service.impl;

import lombok.RequiredArgsConstructor;
import org.kyi.webfluxmongodbdockerdemo.documents.Car;
import org.kyi.webfluxmongodbdockerdemo.repository.CarRepository;
import org.kyi.webfluxmongodbdockerdemo.service.CarService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;

    @Override
    public Flux<Car> all() {
        return carRepository.findAll();
    }

    @Override
    public Mono<Car> byId(String id) {
        return carRepository.findById(id);
    }

    @Override
    public Flux<Car> byBrand(String brand) {
        return carRepository.findByBrandIgnoreCase(brand);
    }

    @Override
    public Mono<Car> create(Car car) {
        return carRepository.save(car);
    }

    @Override
    public Mono<Car> update(String id, Car car) {
        return createCarMonoOpt(carRepository.findById(id))
                .flatMap(carOpt -> {
                    if (carOpt.isEmpty()) return Mono.empty();
                    car.setId(id);
                    return carRepository.save(car);
                });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return carRepository.deleteById(id);
    }


    public Mono<Optional<Car>> createCarMonoOpt(Mono<Car> carMono) {
        return carMono.map(Optional::of).defaultIfEmpty(Optional.empty());
    }
}
