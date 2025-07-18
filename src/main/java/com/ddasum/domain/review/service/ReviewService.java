package com.ddasum.domain.review.service;

import com.ddasum.domain.review.dto.*;
import java.util.List;

public interface ReviewService {
    ReviewResponse createReview(ReviewCreateRequest request);
    ReviewResponse updateReview(Long id, ReviewUpdateRequest request);
    void deleteReview(Long id);
    ReviewResponse getByReviewId(Long id);
    List<ReviewResponse> getByReviewAll();
    List<ReviewResponse> getByUserId(Long userId);
    List<ReviewResponse> getByVacantHouseId(Long vacantHouseId);
} 