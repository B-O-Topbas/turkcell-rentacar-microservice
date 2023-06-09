package com.kodlamaio.rentalservice.api.clients;

import com.kodlamaio.commonpackage.utils.dto.ClientResponse;
import com.kodlamaio.commonpackage.utils.dto.GetCarResponse;
import com.kodlamaio.commonpackage.utils.exceptions.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class CarClientFallback implements CarClient {
    @Override
    public ClientResponse checkIfCarAvailable(UUID carId) {
        log.info("INVENTORY SERVICE IS DOWN!");
        throw new BusinessException("INVENTORY SERVICE IS DOWN!");
    }

    @Override
    public GetCarResponse getById(UUID id) {
        log.info("INVENTORY_SERVICE_IS_DOWN");
        throw new BusinessException("INVENTORY_SERVICE_IS_NOT_AVAILABLE_NOW");
    }
}
