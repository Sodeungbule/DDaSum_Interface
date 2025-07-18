package com.ddasum.domain.review.dto;
import lombok.Data;

@Data
public class ReviewCreateRequest {
    private Long userId;
    private Long vacantHouseId;
    private int rating;
    private String content;
    private String imageUrl;
} 