package com.ddasum.domain.vacanthouse.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VacantHouseResponse {
    private Long id;
    private String address;
    private String region;
    private String description;
    private int price;
    private boolean available;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 