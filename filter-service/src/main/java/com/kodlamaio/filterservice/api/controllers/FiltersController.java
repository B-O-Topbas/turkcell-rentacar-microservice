package com.kodlamaio.filterservice.api.controllers;

import com.kodlamaio.commonpackage.utils.constants.Paths;
import com.kodlamaio.commonpackage.utils.constants.Roles;
import com.kodlamaio.filterservice.business.abstracts.FilterService;
import com.kodlamaio.filterservice.business.dto.responses.GetAllFiltersResponse;
import com.kodlamaio.filterservice.business.dto.responses.GetFilterResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/filters")
public class FiltersController {
    private final FilterService service;

    @GetMapping
    @PreAuthorize(Roles.User)
    public List<GetAllFiltersResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize(Roles.User)
    public GetFilterResponse getByIId(@PathVariable UUID id) {
        return service.getById(id);
    }
}
