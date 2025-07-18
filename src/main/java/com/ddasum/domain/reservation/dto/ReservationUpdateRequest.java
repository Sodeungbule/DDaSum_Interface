package com.ddasum.domain.reservation.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ReservationUpdateRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private int peopleCount;
    private String status;
} 