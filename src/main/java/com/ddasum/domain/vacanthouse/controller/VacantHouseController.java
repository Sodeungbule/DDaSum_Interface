package com.ddasum.domain.vacanthouse.controller;

import com.ddasum.core.api.response.ApiResponse;
import com.ddasum.core.annotation.LoginRequired;
import com.ddasum.core.annotation.TraceLog;
import com.ddasum.domain.vacanthouse.dto.VacantHouseCreateRequest;
import com.ddasum.domain.vacanthouse.dto.VacantHouseUpdateRequest;
import com.ddasum.domain.vacanthouse.dto.VacantHouseResponse;
import com.ddasum.domain.vacanthouse.service.VacantHouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@LoginRequired
@TraceLog
@RestController
@RequestMapping("/api/v1/vacant-houses")
@RequiredArgsConstructor
public class VacantHouseController extends com.ddasum.core.controller.BaseController {
    private final VacantHouseService vacantHouseService;

    @PostMapping
    public ResponseEntity<ApiResponse<VacantHouseResponse>> create(@RequestBody VacantHouseCreateRequest request) {
        try {
            return success(vacantHouseService.create(request));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage(), "VACANT_HOUSE_CREATE_ERROR"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VacantHouseResponse>> update(@PathVariable Long id, @RequestBody VacantHouseUpdateRequest request) {
        try {
            return success(vacantHouseService.update(id, request));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage(), "VACANT_HOUSE_UPDATE_ERROR"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        try {
            vacantHouseService.delete(id);
            return success(null);
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage(), "VACANT_HOUSE_DELETE_ERROR"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VacantHouseResponse>> getById(@PathVariable Long id) {
        try {
            return success(vacantHouseService.getById(id));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage(), "VACANT_HOUSE_GET_ERROR"));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<VacantHouseResponse>>> getAll() {
        try {
            return success(vacantHouseService.getAll());
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage(), "VACANT_HOUSE_LIST_ERROR"));
        }
    }
} 