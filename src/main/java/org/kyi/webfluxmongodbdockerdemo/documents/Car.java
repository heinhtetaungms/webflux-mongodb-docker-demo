package org.kyi.webfluxmongodbdockerdemo.documents;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@RequiredArgsConstructor
public class Car {
    @Id
    private String id;
    private String brand;
    private String model;
}
