package com.kodlamaio.rentalservice.api.controllers;

import com.kodlamaio.commonpackage.utils.constants.Roles;
import com.kodlamaio.rentalservice.business.abstracts.RentalService;
import com.kodlamaio.rentalservice.business.dto.requests.CreateRentalRequest;
import com.kodlamaio.rentalservice.business.dto.requests.UpdateRentalRequest;
import com.kodlamaio.rentalservice.business.dto.responses.CreateRentalResponse;
import com.kodlamaio.rentalservice.business.dto.responses.GetAllRentalsResponse;
import com.kodlamaio.rentalservice.business.dto.responses.GetRentalResponse;
import com.kodlamaio.rentalservice.business.dto.responses.UpdateRentalResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/rentals")
public class RentalsController {
    private final RentalService service;

    @GetMapping
    @Secured("ROLE_admin")
//    @PreAuthorize(Roles.AdminOrDeveloperOrModerator)
    public List<GetAllRentalsResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize(Roles.AdminOrDeveloperOrModerator)
    public GetRentalResponse getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(Roles.AdminOrDeveloper)
    public CreateRentalResponse add(@Valid @RequestBody CreateRentalRequest request) {
        return service.add(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize(Roles.AdminOrDeveloper)
    public UpdateRentalResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateRentalRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(Roles.AdminOrDeveloper)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}