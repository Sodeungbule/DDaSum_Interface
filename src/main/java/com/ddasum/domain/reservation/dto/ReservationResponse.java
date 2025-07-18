package com.ddasum.domain.reservation.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {
    private Long id;
    private Long userId;
    private Long vacantHouseId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int peopleCount;
    private String status;
} 