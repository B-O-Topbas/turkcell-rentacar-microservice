package com.kodlamaio.paymentservice.business.concretes;

import com.kodlamaio.commonpackage.utils.dto.ClientResponse;
import com.kodlamaio.commonpackage.utils.dto.CreateRentalPaymentRequest;
import com.kodlamaio.commonpackage.utils.exceptions.BusinessException;
import com.kodlamaio.commonpackage.utils.mappers.ModelMapperService;
import com.kodlamaio.paymentservice.business.abstacts.PaymentService;
import com.kodlamaio.paymentservice.business.abstacts.PosService;
import com.kodlamaio.paymentservice.business.dto.requests.CreatePaymentRequest;
import com.kodlamaio.paymentservice.business.dto.requests.UpdatePaymentRequest;
import com.kodlamaio.paymentservice.business.dto.responses.CreatePaymentResponse;
import com.kodlamaio.paymentservice.business.dto.responses.GetAllPaymentsResponse;
import com.kodlamaio.paymentservice.business.dto.responses.GetPaymentResponse;
import com.kodlamaio.paymentservice.business.dto.responses.UpdatePaymentResponse;
import com.kodlamaio.paymentservice.business.rules.PaymentBusinessRules;
import com.kodlamaio.paymentservice.entities.Payment;
import com.kodlamaio.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentManager implements PaymentService {
    private final PaymentRepository repository;
    private final ModelMapperService mapper;
    private final PaymentBusinessRules rules;
    private final PosService posService;

    @Override
    public List<GetAllPaymentsResponse> getAll() {
        var payments = repository.findAll();
        return payments
                .stream()
                .map(payment -> mapper.forResponse().map(payment, GetAllPaymentsResponse.class))
                .toList();
    }

    @Override
    public GetPaymentResponse getById(UUID id) {
        rules.checkIfPaymentExists(id);
        var payment = repository.findById(id).orElseThrow();
        return mapper.forResponse().map(payment, GetPaymentResponse.class);
    }

    @Override
    public CreatePaymentResponse add(CreatePaymentRequest request) {
        rules.checkIfCardNumberExists(request.getCardNumber());
        var payment = mapper.forRequest().map(request, Payment.class);
        var createdPayment = repository.save(payment);
        return mapper.forResponse().map(createdPayment, CreatePaymentResponse.class);
    }

    @Override
    public UpdatePaymentResponse update(UUID id, UpdatePaymentRequest request) {
        rules.checkIfPaymentExists(id);
        var payment = mapper.forRequest().map(request, Payment.class);
        payment.setId(id);
        repository.save(payment);
        return mapper.forResponse().map(payment, UpdatePaymentResponse.class);
    }

    @Override
    public void delete(UUID id) {
        rules.checkIfPaymentExists(id);
        repository.deleteById(id);
    }



    /*****/
    @Override
    public ClientResponse processPayment(CreateRentalPaymentRequest request) {
        var response = new ClientResponse();
        processPaymentTransaction(request, response);
        return response;
    }
/*****/
    private void processPaymentTransaction(CreateRentalPaymentRequest request, ClientResponse response) {
        try {
            rules.checkIfPaymentValid(request);
            var payment = repository.findByCardNumber(request.getCardNumber());
            double balance = payment.getBalance();
            rules.checkIfBalanceIsEnough(balance, request.getPrice());
            posService.pay();
            payment.setBalance(balance - request.getPrice());
            repository.save(payment);
            response.setSuccess(true);
        } catch (BusinessException exception) {
            response.setSuccess(false);
            response.setMessage(exception.getMessage());
        }
    }
}