package com.kodlamaio.maintenanceservice.api.controllers;

import com.kodlamaio.commonpackage.utils.constants.Roles;
import com.kodlamaio.maintenanceservice.business.abstracts.MaintenanceService;
import com.kodlamaio.maintenanceservice.business.dto.requests.create.CreateMaintenanceRequest;
import com.kodlamaio.maintenanceservice.business.dto.requests.update.UpdateMaintenanceRequest;
import com.kodlamaio.maintenanceservice.business.dto.responses.create.CreateMaintenanceResponse;
import com.kodlamaio.maintenanceservice.business.dto.responses.get.GetAllMaintenancesResponse;
import com.kodlamaio.maintenanceservice.business.dto.responses.get.GetMaintenanceResponse;
import com.kodlamaio.maintenanceservice.business.dto.responses.update.UpdateMaintenanceResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/maintenances")
public class MaintenancesController {
    private final MaintenanceService service;

    @GetMapping
    @PreAuthorize(Roles.AdminOrDeveloperOrModerator)
    public List<GetAllMaintenancesResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize(Roles.AdminOrDeveloperOrModerator)
    public GetMaintenanceResponse getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(Roles.AdminOrDeveloper)
    public CreateMaintenanceResponse add(@RequestBody CreateMaintenanceRequest request) {
        return service.add(request);
    }

    @PutMapping("/return")
    @PreAuthorize(Roles.AdminOrDeveloperOrModerator)
    public GetMaintenanceResponse returnCarFromMaintenance(@RequestParam UUID carId) {
        return service.returnCarFromMaintenance(carId);
    }

    @PutMapping("/{id}")
    @PreAuthorize(Roles.AdminOrDeveloperOrModerator)
    public UpdateMaintenanceResponse update(@PathVariable UUID id, @RequestBody UpdateMaintenanceRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(Roles.Admin)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}