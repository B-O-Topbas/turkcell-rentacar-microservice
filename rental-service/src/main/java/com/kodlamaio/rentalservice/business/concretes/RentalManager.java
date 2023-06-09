package com.kodlamaio.rentalservice.business.concretes;

import com.kodlamaio.commonpackage.events.rental.InvoiceCreatedEvent;
import com.kodlamaio.commonpackage.events.rental.RentalCreatedEvent;
import com.kodlamaio.commonpackage.events.rental.RentalDeletedEvent;
import com.kodlamaio.commonpackage.utils.dto.CreateRentalPaymentRequest;
import com.kodlamaio.commonpackage.utils.dto.GetCarResponse;
import com.kodlamaio.commonpackage.utils.kafka.producer.KafkaProducer;
import com.kodlamaio.commonpackage.utils.mappers.ModelMapperService;
import com.kodlamaio.rentalservice.api.clients.CarClient;
import com.kodlamaio.rentalservice.business.abstracts.RentalService;
import com.kodlamaio.rentalservice.business.dto.requests.CreateRentalRequest;
import com.kodlamaio.rentalservice.business.dto.requests.UpdateRentalRequest;
import com.kodlamaio.rentalservice.business.dto.responses.CreateRentalResponse;
import com.kodlamaio.rentalservice.business.dto.responses.GetAllRentalsResponse;
import com.kodlamaio.rentalservice.business.dto.responses.GetRentalResponse;
import com.kodlamaio.rentalservice.business.dto.responses.UpdateRentalResponse;
import com.kodlamaio.rentalservice.business.rules.RentalBusinessRules;
import com.kodlamaio.rentalservice.entities.Rental;
import com.kodlamaio.rentalservice.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RentalManager implements RentalService {
    private final RentalRepository repository;
    private final ModelMapperService mapper;
    private final RentalBusinessRules rules;
    private final KafkaProducer producer;
    private final CarClient client;

    @Override
    public List<GetAllRentalsResponse> getAll() {
        var rentals = repository.findAll();
        return rentals
                .stream()
                .map(rental -> mapper.forResponse().map(rental, GetAllRentalsResponse.class))
                .toList();
    }

    @Override
    public GetRentalResponse getById(UUID id) {
        rules.checkIfRentalExists(id);
        var rental = repository.findById(id).orElseThrow();
        return mapper.forResponse().map(rental, GetRentalResponse.class);
    }

    @Override
    public CreateRentalResponse add(CreateRentalRequest request) {
        rules.ensureCarIsAvailable(request.getCarId());
        var rental = mapper.forRequest().map(request, Rental.class);
        rental.setId(null);
        rental.setTotalPrice(getTotalPrice(rental));
        rental.setRentedAt(LocalDate.now());

        var paymentRequest = new CreateRentalPaymentRequest();
        mapper.forRequest().map(request.getInfo(), paymentRequest);
        paymentRequest.setPrice(getTotalPrice(rental));
        rules.ensurePayment(paymentRequest);

        repository.save(rental);
        sendKafkaRentalCreatedEvent(request.getCarId());

        var carResponse=client.getById(request.getCarId());
        var event=mergeRentalInfo(carResponse,request);
        producer.sendMessage(event,"invoice");



        return mapper.forResponse().map(rental, CreateRentalResponse.class);
    }

    @Override
    public UpdateRentalResponse update(UUID id, UpdateRentalRequest request) {
        rules.checkIfRentalExists(id);
        var rental = mapper.forRequest().map(request, Rental.class);
        rental.setId(id);
        rental.setTotalPrice(getTotalPrice(rental));
        repository.save(rental);
        return mapper.forResponse().map(rental, UpdateRentalResponse.class);
    }

    @Override
    public void delete(UUID id) {
        rules.checkIfRentalExists(id);
        sendKafkaRentalDeletedEvent(id);
        repository.deleteById(id);
    }

    private double getTotalPrice(Rental rental) {
        return rental.getDailyPrice() * rental.getRentedForDays();
    }

    private void sendKafkaRentalCreatedEvent(UUID carId) {
        producer.sendMessage(new RentalCreatedEvent(carId), "rental-created");
    }

    private void sendKafkaRentalDeletedEvent(UUID id) {
        var carId = repository.findById(id).orElseThrow().getCarId();
        producer.sendMessage(new RentalDeletedEvent(carId), "rental-deleted");
    }

    private InvoiceCreatedEvent mergeRentalInfo(GetCarResponse carResponse, CreateRentalRequest request) {
        var event=new InvoiceCreatedEvent();
        event.setBrandName(carResponse.getModelBrandName());
        event.setPlate(carResponse.getPlate());
        event.setRentedAt(LocalDateTime.now());
        event.setDailyPrice(carResponse.getDailyPrice());
        event.setRentedForDays(request.getRentedForDays());
        event.setTotalPrice(request.getRentedForDays() * carResponse.getDailyPrice());
        event.setCardHolder(request.getPaymentRequest().getCardHolder());
        event.setModelYear(carResponse.getModelYear());
        return event;
    }
}
