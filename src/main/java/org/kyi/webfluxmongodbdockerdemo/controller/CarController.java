package org.kyi.webfluxmongodbdockerdemo.controller;

import io.netty.handler.codec.DecoderException;
import lombok.RequiredArgsConstructor;
import org.kyi.webfluxmongodbdockerdemo.documents.Car;
import org.kyi.webfluxmongodbdockerdemo.service.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;
import java.util.function.Function;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @GetMapping
    public Flux<Car> all(@RequestParam(required = false, value = "brand") Optional<String> brandOpt) {
        if (brandOpt.isEmpty()) return carService.all();
        return carService.byBrand(brandOpt.get());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<?>> byId(@PathVariable String id) {
        return carService.createCarMonoOpt(carService.byId(id)).flatMap(flatMapMonoCarOK);
    }

    @PostMapping
    public Mono<ResponseEntity<?>> create(@RequestBody Car car, UriComponentsBuilder uriBuilder) {
        try {
            return carService.create(car).flatMap(createdCar -> {
                String location = uriBuilder.path("cars/{id}").buildAndExpand(createdCar.getId()).toUriString();
                return Mono.just(ResponseEntity.created(URI.create(location)).body(createdCar));
            });
        } catch (DecoderException e) {
            System.err.println("error: " + e.getMessage());
            return Mono.just(ResponseEntity.badRequest().body(e.getCause()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return Mono.just(ResponseEntity.internalServerError().body(e.getCause()));
        }
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<?>> update(@PathVariable String id, @RequestBody Car car) {
        try {
            return carService.createCarMonoOpt(carService.update(id, car))
                    .flatMap(flatMapMonoCarOK);
        } catch (DecoderException e) {
            System.err.println("error: " + e.getMessage());
            return Mono.just(ResponseEntity.badRequest().body(e.getCause()));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return Mono.just(ResponseEntity.internalServerError().body(e.getCause()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        carService.deleteById(id).subscribe();
        return ResponseEntity.noContent().build();
    }


    private final Function<Optional<Car>, Mono<ResponseEntity<?>>> flatMapMonoCarOK = carOpt ->
            carOpt.<Mono<ResponseEntity<?>>>map(car -> Mono.just(ResponseEntity.ok(car)))
                    .orElseGet(() -> Mono.just(ResponseEntity.notFound().build()));
}
