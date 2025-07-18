package com.ddasum.domain.reservation.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ReservationCreateRequest {
    private Long userId;
    private Long vacantHouseId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int peopleCount;
} 