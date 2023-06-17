package com.kodlamaio.filterservice.repository;

import com.kodlamaio.filterservice.entities.Filter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
public interface FilterRepository extends MongoRepository<Filter, UUID> {
    void deleteByCarId(UUID carId);
    @Transactional
    void deleteAllByBrandId(UUID brandId);
    Filter findByCarId(UUID carId);
}


// payment invoice bende ++