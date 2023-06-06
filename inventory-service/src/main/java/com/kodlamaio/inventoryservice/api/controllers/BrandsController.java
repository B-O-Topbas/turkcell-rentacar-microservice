package com.kodlamaio.inventoryservice.api.controllers;

import com.kodlamaio.commonpackage.utils.constants.Roles;
import com.kodlamaio.inventoryservice.business.abstracts.BrandService;
import com.kodlamaio.inventoryservice.business.dto.requests.create.CreateBrandRequest;
import com.kodlamaio.inventoryservice.business.dto.requests.update.UpdateBrandRequest;
import com.kodlamaio.inventoryservice.business.dto.responses.create.CreateBrandResponse;
import com.kodlamaio.inventoryservice.business.dto.responses.get.GetAllBrandsResponse;
import com.kodlamaio.inventoryservice.business.dto.responses.get.GetBrandResponse;
import com.kodlamaio.inventoryservice.business.dto.responses.update.UpdateBrandResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/brands")
public class BrandsController {
    private final BrandService service;

    @GetMapping
    @PreAuthorize(Roles.AdminOrDeveloper)
    public List<GetAllBrandsResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize(Roles.AdminOrDeveloper)
    public GetBrandResponse getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(Roles.AdminOrDeveloper)
    public CreateBrandResponse add(@Valid @RequestBody CreateBrandRequest request) {
        return service.add(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize(Roles.AdminOrDeveloper)
    public UpdateBrandResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateBrandRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(Roles.AdminOrDeveloper)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
