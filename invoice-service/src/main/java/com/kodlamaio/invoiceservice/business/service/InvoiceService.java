package com.kodlamaio.invoiceservice.business.service;

import com.kodlamaio.commonpackage.events.rental.InvoiceCreatedEvent;
import com.kodlamaio.invoiceservice.business.dto.GetAllInvoiceResponse;
import com.kodlamaio.invoiceservice.business.dto.GetInvoiceResponse;

import java.util.List;
import java.util.UUID;

public interface InvoiceService {
    void create(InvoiceCreatedEvent event);
    List<GetAllInvoiceResponse> getAll();
    GetInvoiceResponse getInvoiceById(UUID id);
    void delete(UUID id);
}