package com.kodlamaio.paymentservice.api.controllers;

import com.kodlamaio.commonpackage.utils.constants.Roles;
import com.kodlamaio.commonpackage.utils.dto.ClientResponse;
import com.kodlamaio.commonpackage.utils.dto.CreateRentalPaymentRequest;
import com.kodlamaio.paymentservice.business.abstacts.PaymentService;
import com.kodlamaio.paymentservice.business.dto.requests.CreatePaymentRequest;
import com.kodlamaio.paymentservice.business.dto.requests.UpdatePaymentRequest;
import com.kodlamaio.paymentservice.business.dto.responses.CreatePaymentResponse;
import com.kodlamaio.paymentservice.business.dto.responses.GetAllPaymentsResponse;
import com.kodlamaio.paymentservice.business.dto.responses.GetPaymentResponse;
import com.kodlamaio.paymentservice.business.dto.responses.UpdatePaymentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentsController {
    private final PaymentService service;

    @GetMapping
    @PreAuthorize(Roles.AdminOrDeveloper)
    public List<GetAllPaymentsResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize(Roles.AdminOrDeveloper)
    public GetPaymentResponse getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(Roles.AdminOrDeveloper)
    public CreatePaymentResponse add(@Valid @RequestBody CreatePaymentRequest request) {
        return service.add(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize(Roles.AdminOrDeveloper)
    public UpdatePaymentResponse update(@PathVariable UUID id, @Valid @RequestBody UpdatePaymentRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(Roles.AdminOrDeveloper)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }

    @PostMapping("/check")
    public ClientResponse proccessPayment(@RequestBody CreateRentalPaymentRequest request) {
        return service.processPayment(request);
    }
}