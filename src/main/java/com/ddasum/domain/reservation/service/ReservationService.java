package com.ddasum.domain.reservation.service;

import com.ddasum.domain.reservation.dto.ReservationCreateRequest;
import com.ddasum.domain.reservation.dto.ReservationUpdateRequest;
import com.ddasum.domain.reservation.dto.ReservationResponse;
import java.util.List;

public interface ReservationService {
    ReservationResponse createReservation(ReservationCreateRequest request);
    ReservationResponse updateReservation(Long id, ReservationUpdateRequest request);
    void deleteReservation(Long id);
    ReservationResponse getByReservationId(Long id);
    List<ReservationResponse> getByReservationAll();
    List<ReservationResponse> getByUserId(Long userId);
    List<ReservationResponse> getByVacantHouseId(Long vacantHouseId);
} 