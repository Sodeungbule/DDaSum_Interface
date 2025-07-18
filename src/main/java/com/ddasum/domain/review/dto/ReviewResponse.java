package com.ddasum.domain.review.dto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private Long userId;
    private Long vacantHouseId;
    private int rating;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;
} 