package com.ddasum.domain.vacanthouse.dto;

import lombok.Data;

@Data
public class VacantHouseUpdateRequest {
    private String address;
    private String region;
    private String description;
    private int price;
    private boolean available;
    private String imageUrl;
} 