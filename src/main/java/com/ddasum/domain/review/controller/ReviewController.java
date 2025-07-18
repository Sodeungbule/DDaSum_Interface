package com.ddasum.domain.review.controller;

import com.ddasum.core.api.response.ApiResponse;
import com.ddasum.core.annotation.LoginRequired;
import com.ddasum.core.annotation.TraceLog;
import com.ddasum.core.controller.BaseController;
import com.ddasum.domain.review.dto.*;
import com.ddasum.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@LoginRequired
@TraceLog
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController extends BaseController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> create(@RequestBody ReviewCreateRequest request) {
        return success(reviewService.createReview(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponse>> update(@PathVariable Long id, @RequestBody ReviewUpdateRequest request) {
        return success(reviewService.updateReview(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return success(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponse>> getById(@PathVariable Long id) {
        return success(reviewService.getByReviewId(id));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getAll() {
        return success(reviewService.getByReviewAll());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getByUserId(@PathVariable Long userId) {
        return success(reviewService.getByUserId(userId));
    }

    @GetMapping("/vacant-house/{vacantHouseId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getByVacantHouseId(@PathVariable Long vacantHouseId) {
        return success(reviewService.getByVacantHouseId(vacantHouseId));
    }
} 