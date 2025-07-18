package com.ddasum.domain.reservation.controller;

import com.ddasum.core.api.response.ApiResponse;
import com.ddasum.core.annotation.LoginRequired;
import com.ddasum.core.annotation.TraceLog;
import com.ddasum.domain.reservation.dto.*;
import com.ddasum.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@LoginRequired
@TraceLog
@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationController extends com.ddasum.core.controller.BaseController {
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReservationResponse>> create(@RequestBody ReservationCreateRequest request) {
        return success(reservationService.createReservation(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReservationResponse>> update(@PathVariable Long id, @RequestBody ReservationUpdateRequest request) {
        return success(reservationService.updateReservation(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return success(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReservationResponse>> getById(@PathVariable Long id) {
        return success(reservationService.getByReservationId(id));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getAll() {
        return success(reservationService.getByReservationAll());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getByUserId(@PathVariable Long userId) {
        return success(reservationService.getByUserId(userId));
    }

    @GetMapping("/vacant-house/{vacantHouseId}")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getByVacantHouseId(@PathVariable Long vacantHouseId) {
        return success(reservationService.getByVacantHouseId(vacantHouseId));
    }
} 