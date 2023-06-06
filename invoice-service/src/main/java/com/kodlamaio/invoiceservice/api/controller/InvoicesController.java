package com.kodlamaio.invoiceservice.api.controller;

import com.kodlamaio.commonpackage.utils.constants.Roles;
import com.kodlamaio.invoiceservice.business.dto.GetAllInvoiceResponse;
import com.kodlamaio.invoiceservice.business.dto.GetInvoiceResponse;
import com.kodlamaio.invoiceservice.business.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@CrossOrigin
@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoicesController {
    private final InvoiceService service;

    @GetMapping
    @PreAuthorize(Roles.AdminOrDeveloperOrModerator)
    public ResponseEntity<List<GetAllInvoiceResponse> >getALl(){
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    @PreAuthorize(Roles.AdminOrDeveloper)
    public ResponseEntity<GetInvoiceResponse> getInvoiceById(@PathVariable UUID id){
        return new ResponseEntity<>(service.getInvoiceById(id),HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize(Roles.AdminOrDeveloper)
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}